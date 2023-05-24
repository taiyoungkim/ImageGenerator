plugins {
    id("tydev.android.library")
    id("tydev.android.library.jacoco")
    id("tydev.android.hilt")
}

android {
    namespace = "com.tydev.imagegenerator.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
}