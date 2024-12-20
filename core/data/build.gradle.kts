plugins {
    alias(libs.plugins.com.example.android.library)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.data"

}

dependencies {
    implementation(libs.androidx.runtime)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.lifecycle.runtime.ktx)
}