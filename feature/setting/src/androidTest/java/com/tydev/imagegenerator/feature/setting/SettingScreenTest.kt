package com.tydev.imagegenerator.feature.setting

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.tydev.imagegenerator.core.model.data.UserData
import org.junit.Rule
import org.junit.Test

class SettingScreenTest {
    @get:Rule(order = 0)
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun settingScreen_whenSuccess_showsApikey_andModifyButtonWhenDisabled() {
        val apiKey = "test-api-key"
        val userData = UserData(apiKey)

        composeTestRule.setContent {
            SettingScreen(
                settingState = SettingUiState.Success(userData),
                onSetApikey = { /*do nothing in this test*/ },
            )
        }

        composeTestRule.onNodeWithText(apiKey)
            .assertExists()

        composeTestRule.onNodeWithText(
            composeTestRule.activity.resources.getString(R.string.modify)
        ).assertExists()
    }

    @Test
    fun settingScreen_whenSuccess_showsEmptyApikey_andSaveButtonWhenEnabled() {
        val userData = UserData("")

        composeTestRule.setContent {
            SettingScreen(
                settingState = SettingUiState.Success(userData),
                onSetApikey = { /*do nothing in this test*/ },
            )
        }

        composeTestRule.onNodeWithText(
            composeTestRule.activity.resources.getString(R.string.save)
        ).assertExists()
    }

    @Test
    fun settingScreen_whenError_showsEmptyApikey_andSaveButtonWhenEnabled() {
        composeTestRule.setContent {
            SettingScreen(
                settingState = SettingUiState.Error(Exception()),
                onSetApikey = { /*do nothing in this test*/ },
            )
        }

        composeTestRule.onNodeWithText(
            composeTestRule.activity.resources.getString(R.string.save)
        ).assertExists()
    }
}