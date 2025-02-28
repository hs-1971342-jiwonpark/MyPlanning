plugins {
    alias(libs.plugins.com.example.android.library.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.com.example.android.presentation.feature)
    alias(libs.plugins.com.example.android.hilt)
}

android {
    namespace = "com.example.login"
}

dependencies {
    implementation(libs.androidx.material3)
    implementation (libs.androidx.credentials)
    implementation (libs.androidx.credentials.play.services.auth)
    implementation (libs.googleid)
    implementation(project(":common:navigation"))
    implementation(project(":common:navigation"))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.googleid)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)

    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore.ktx)
    implementation(platform(libs.firebase.bom))

    implementation(libs.kotlinx.serialization.json.v160)
    implementation (libs.v2.user)
    implementation(libs.firebase.analytics)
    implementation(libs.androidx.lifecycle.runtime.ktx)
}