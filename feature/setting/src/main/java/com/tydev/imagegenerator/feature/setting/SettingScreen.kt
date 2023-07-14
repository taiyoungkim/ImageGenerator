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

package com.tydev.imagegenerator.feature.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tydev.imagegenerator.core.designsystem.theme.ImageGeneratorTheme
import com.tydev.imagegenerator.core.model.data.UserData

@Composable
internal fun SettingRoute(
    modifier: Modifier = Modifier,
    viewModel: SettingViewModel = hiltViewModel(),
) {
    val settingState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingScreen(
        settingState = settingState,
        modifier = modifier,
        onSetApikey = viewModel::setApiKey,
    )
}

@Composable
internal fun SettingScreen(
    settingState: SettingUiState,
    modifier: Modifier = Modifier,
    onSetApikey: (String) -> Unit,
) {
    var key by remember { mutableStateOf("") }
    var enabled: Boolean

    when (settingState) {
        is SettingUiState.Loading -> {
            CircularProgressIndicator(
                modifier
                    .fillMaxWidth()
                    .wrapContentSize(),
            )
        }
        is SettingUiState.Success -> {
            key = settingState.userData.apiKey
            enabled = settingState.userData.apiKey.isEmpty()

            SetSettingView(
                key = key,
                enabled = enabled,
                onSetApikey = onSetApikey,
                onKeyChange = { key = it },
                onEnableChange = { enabled = true }
            )
        }
        is SettingUiState.Error -> {
            SetSettingView(
                key = key,
                enabled = true,
                onSetApikey = onSetApikey,
                onKeyChange = { key = it },
                onEnableChange = { enabled = true }
            )
        }
    }
}

@Composable
fun SetSettingView(
    key: String,
    enabled: Boolean,
    onSetApikey: (String) -> Unit,
    onKeyChange: (String) -> Unit,
    onEnableChange: () -> Unit,
) {
    Column {
        Row (
            modifier = Modifier.fillMaxWidth()
        ) {
            BasicTextField(
                value = key,
                enabled = enabled,
                singleLine = true,
                onValueChange = onKeyChange,
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .padding(2.dp)
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(5.dp),
                    )
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
                    .padding(end = 16.dp)
            )
            if (enabled) {
                Button(
                    onClick = { onSetApikey(key) },
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier
                        .padding(2.dp),
                ) {
                    Text(text = stringResource(id = R.string.save))
                }
            } else {
                Button(
                    onClick = onEnableChange,
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier
                        .padding(2.dp),
                ) {
                    Text(text = stringResource(id = R.string.modify))
                }
            }
        }
    }
}

@Preview
@Composable
fun SettingPreview() {
    ImageGeneratorTheme {
        SettingScreen(
            settingState = SettingUiState.Success(UserData("apiKey")),
            onSetApikey = {},
        )
    }
}
