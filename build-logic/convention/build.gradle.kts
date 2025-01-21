plugins {
    `kotlin-dsl`
}

group = "com.example.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "com.example.android.application"
            implementationClass = "com.example.convention.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "com.example.android.library"
            implementationClass = "com.example.convention.AndroidLibraryConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "com.example.android.application.compose"
            implementationClass = "com.example.convention.AndroidComposeConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "com.example.android.library.compose"
            implementationClass = "com.example.convention.AndroidLibraryComposeConventionPlugin"
        }
        register("androidPresentationUI") {
            id = "com.example.android.presentation.ui"
            implementationClass = "com.example.convention.AndroidPresentationUIConventionPlugin"
        }
        register("hilt") {
            id = "com.example.android.hilt"
            implementationClass = "com.example.convention.HiltConventionPlugin"
        }
        register("androidFeature") {
            id = "com.example.android.presentation.feature"
            implementationClass = "com.example.convention.AndroidFeatureConventionPlugin"
        }
        register("room") {
            id = "com.example.android.room"
            implementationClass = "com.example.convention.RoomConventionPlugin"
        }
    }
}