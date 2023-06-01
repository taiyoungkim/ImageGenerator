plugins {
    id("tydev.android.library")
    id("tydev.android.hilt")
}

android {
    namespace = "com.tydev.imagegenerator.core.datastore.test"
}

dependencies {
    api(project(":core:datastore"))

    api(libs.androidx.dataStore.core)
    api(libs.hilt.android.testing)
}
