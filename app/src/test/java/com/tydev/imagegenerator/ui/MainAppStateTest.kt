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

package com.tydev.imagegenerator.ui

import android.content.Context
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import androidx.navigation.testing.TestNavHostController
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(instrumentedPackages = ["androidx.loader.content"], sdk = [31])
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainAppStateTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var state: MainAppState
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = RuntimeEnvironment.getApplication().applicationContext
    }

    @Test
    fun mainAppState_currentDestination() = runTest {
        var currentDestination: String? = null

        composeTestRule.setContent {
            val navController = rememberTestNavController()
            state = remember(navController) {
                MainAppState(
                    navController = navController,
                    windowSizeClass = getCompactWindowClass(),
                )
            }

            currentDestination = state.currentDestination?.route

            LaunchedEffect(Unit) {
                navController.setCurrentDestination("b")
            }
        }

        assertEquals("b", currentDestination)
    }

    @Test
    fun mainAppState_destinations() = runTest {
        composeTestRule.setContent {
            state = rememberMainAppState(
                windowSizeClass = getCompactWindowClass(),
            )
        }

        assertEquals(2, state.topLevelDestinations.size)
        assertTrue(state.topLevelDestinations[0].name.contains("generator", true))
        assertTrue(state.topLevelDestinations[1].name.contains("setting", true))
    }

    @Test
    fun mainAppState_showBottomBar_compact() = runTest {
        composeTestRule.setContent {
            state = MainAppState(
                navController = NavHostController(context),
                windowSizeClass = getCompactWindowClass(),
            )
        }

        assertTrue(state.shouldShowBottomBar)
        assertFalse(state.shouldShowNavRail)
    }

    @Test
    fun mainAppState_showNavRail_medium() = runTest {
        composeTestRule.setContent {
            state = MainAppState(
                navController = NavHostController(context),
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(800.dp, 800.dp)),
            )
        }

        assertTrue(state.shouldShowNavRail)
        assertFalse(state.shouldShowBottomBar)
    }

    @Test
    fun mainAppState_showNavRail_large() = runTest {
        composeTestRule.setContent {
            state = MainAppState(
                navController = NavHostController(context),
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(900.dp, 1200.dp)),
            )
        }

        assertTrue(state.shouldShowNavRail)
        assertFalse(state.shouldShowBottomBar)
    }

    private fun getCompactWindowClass() = WindowSizeClass.calculateFromSize(DpSize(500.dp, 300.dp))
}

@Composable
private fun rememberTestNavController(): TestNavHostController {
    val navController = remember {
        TestNavHostController(RuntimeEnvironment.getApplication().applicationContext).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
            graph = createGraph(startDestination = "a") {
                composable("a") { }
                composable("b") { }
            }
        }
    }
    return navController
}
