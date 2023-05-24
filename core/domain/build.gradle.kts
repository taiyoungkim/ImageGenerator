plugins {
    id("tydev.android.library")
    id("tydev.android.library.jacoco")
    kotlin("kapt")
}

android {
    namespace = "com.tydev.imagegenerator.core.domain"
}

dependencies {

    implementation(project(":core:data"))

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}