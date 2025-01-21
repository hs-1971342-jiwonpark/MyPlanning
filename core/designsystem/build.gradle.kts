plugins {
    alias(libs.plugins.com.example.android.library.compose)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.designsystem"
}

dependencies {
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.androidx.compose.bom))
    implementation (libs.accompanist.flowlayout)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.graphics)

    implementation(libs.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(project(":core:data"))


}