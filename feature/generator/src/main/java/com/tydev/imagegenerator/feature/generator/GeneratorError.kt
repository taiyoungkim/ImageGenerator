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

enum class GeneratorError(val code: String, val messageResId: Int) {
    InvalidAuthentication("401", R.string.generate_error_invalid_authentication),
    RateLimitReached("429", R.string.generate_error_rate_limit_reached),
    ServerError("500", R.string.generate_error_server_error),
    ServerOverload("503", R.string.generate_error_server_overload),
    WordEmpty("1", R.string.generate_error_word_empty),
    WordOverLimit("2", R.string.generate_error_word_over_limit),
    PromptEmpty("3", R.string.generate_error_prompt_empty),
    UnexpectedError("-1", R.string.generate_error_unexpected), ;

    companion object {
        fun fromCode(code: String): GeneratorError {
            return values().find { it.code == code } ?: UnexpectedError
        }
    }
}
