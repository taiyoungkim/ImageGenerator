pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ImageGenerator"
include(":app")
include(":core:data")
include(":core:domain")
include(":core:common")
include(":core:network")
include(":feature:setting")
include(":feature:generator")
include(":core:model")
include(":core:datastore")
include(":core:datastore-test")
