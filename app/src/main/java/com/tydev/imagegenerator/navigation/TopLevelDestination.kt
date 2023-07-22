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

package com.tydev.imagegenerator.navigation

import androidx.annotation.DrawableRes
import com.tydev.imagegenerator.R
import com.tydev.imagegenerator.feature.generator.R as generatorR
import com.tydev.imagegenerator.feature.setting.R as settingR

enum class TopLevelDestination(
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    GENERATOR(
        selectedIcon = generatorR.drawable.selected_generator,
        unselectedIcon = generatorR.drawable.unselected_generator,
        iconTextId = generatorR.string.generator,
        titleTextId = R.string.app_name,
    ),
    SETTING(
        selectedIcon = settingR.drawable.selected_setting,
        unselectedIcon = settingR.drawable.unselected_setting,
        iconTextId = settingR.string.setting,
        titleTextId = R.string.app_name,
    ),
}
