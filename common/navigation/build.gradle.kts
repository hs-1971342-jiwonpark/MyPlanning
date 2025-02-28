plugins {
    alias(libs.plugins.com.example.android.library.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.com.example.android.presentation.feature)
    alias(libs.plugins.com.example.android.hilt)
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.navigation"

}

dependencies {
    implementation(libs.androidx.runtime)
    implementation(libs.firebase.firestore.ktx)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.kotlinx.serialization.json.v160)
    implementation(project(":core:data"))
}