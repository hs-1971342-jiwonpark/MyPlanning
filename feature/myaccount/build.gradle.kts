plugins {
    alias(libs.plugins.com.example.android.library.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.com.example.android.presentation.feature)
    alias(libs.plugins.com.example.android.hilt)
}

android {
    namespace = "com.example.myaccount"
}

dependencies {
    implementation(libs.androidx.material3)
    implementation (libs.androidx.credentials)
    implementation (libs.androidx.credentials.play.services.auth)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.googleid)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)

    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore.ktx)
    implementation(platform(libs.firebase.bom))

    implementation(libs.firebase.analytics)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(project(":core:designsystem"))
    implementation(project(":core:data"))

    implementation(libs.compose)

}