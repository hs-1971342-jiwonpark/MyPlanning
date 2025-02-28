plugins {
    alias(libs.plugins.com.example.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.com.example.android.hilt)
    alias(libs.plugins.com.example.android.room)
    id("kotlin-parcelize")
    kotlin("plugin.serialization")
}

android {
    namespace = "com.example.data"

}

dependencies {
    implementation(libs.androidx.runtime)
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-storage")
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.navigation.runtime.ktx)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization.json.v160)

    implementation ("com.google.code.gson:gson:2.11.0")
}