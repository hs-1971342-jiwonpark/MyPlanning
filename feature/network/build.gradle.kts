plugins {
    alias(libs.plugins.com.example.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.com.example.android.hilt)
}

android {
    namespace = "com.example.network"

}

dependencies {
    implementation(libs.androidx.runtime)
    implementation(libs.firebase.firestore.ktx)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(project(":core:data"))
}