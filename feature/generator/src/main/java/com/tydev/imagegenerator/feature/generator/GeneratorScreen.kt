/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tydev.imagegenerator.feature.generator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tydev.imagegenerator.core.designsystem.theme.ImageGeneratorTheme
import com.tydev.imagegenerator.feature.generator.components.SearchTextField

@Composable
internal fun GeneratorRoute(
    modifier: Modifier = Modifier,
    viewModel: GeneratorViewModel = hiltViewModel(),
) {
    val generatorState by viewModel.generatorState.collectAsStateWithLifecycle()

    GeneratorScreen(
        generatorState = generatorState,
        modifier = modifier,
        onGeneratePrompt = viewModel::searchForPrompt,
    )
}

@Composable
internal fun GeneratorScreen(
    generatorState: GeneratorUiState,
    modifier: Modifier = Modifier,
    onGeneratePrompt: (String) -> Unit,
) {
    var query by remember { mutableStateOf("") }

    Column {
        SearchTextField(
            text = query,
            onValueChange = { query = it },
            onSearch = { onGeneratePrompt(query) },
            shouldShowHint = query.isEmpty(),
            onFocusChanged = {},
        )

        when (generatorState) {
            is GeneratorUiState.Initial -> {}
            GeneratorUiState.Loading ->
                CircularProgressIndicator(
                    modifier
                        .fillMaxWidth()
                        .wrapContentSize()
                        .testTag("myProgressBar"),
                )

            is GeneratorUiState.Success ->
                if (generatorState.chat.choices.isNotEmpty() &&
                    generatorState.image.data.isNotEmpty()
                ) {
                    GeneratorView(
                        generatorState = generatorState,
                    )
                }

            is GeneratorUiState.Error -> {
                Text(
                    text = stringResource(
                        id = GeneratorError.fromCode(generatorState.exception.message.toString()).messageResId,
                    ),
                    color = Color.Red,
                )
            }
        }
    }
}

@Composable
private fun GeneratorView(
    generatorState: GeneratorUiState.Success,
) {
    Text(text = generatorState.chat.choices[0].message.content)
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
    ) {
        generatorState.image.data.forEach {
            val url = it.url
            item(key = url) {
                GeneratedImage(url)
            }
        }
    }
}

@Composable
fun GeneratedImage(image: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(image)
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )
}

@Preview
@Composable
fun GeneratorPreview() {
    ImageGeneratorTheme {
        GeneratorScreen(
            generatorState = GeneratorUiState.Initial,
            modifier = Modifier,
            onGeneratePrompt = {},
        )
    }
}
