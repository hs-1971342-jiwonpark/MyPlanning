import org.apache.commons.logging.LogFactory.release

plugins {
    alias(libs.plugins.com.example.android.application.compose)
    alias(libs.plugins.com.example.android.hilt)
    id("com.google.gms.google-services")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.myplanning"

    signingConfigs {
        create("release") {
            storeFile = file("C:/Users/PC/Desktop/MyPlanning/app/keystore/key.jks")
            storePassword = "990329" // keystore 비밀번호
            keyAlias = "upload" // 키 별칭
            keyPassword = "990329" // 키 비밀번호
        }
    }
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
        }
    }
}

dependencies {

    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(project(":feature:rule"))
    implementation(project(":feature:myaccount"))
    implementation(project(":feature:makeplanet"))
    implementation(project(":common:navigation"))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.runtime)
    implementation(libs.googleid)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)
    androidTestImplementation(libs.androidx.espresso.core)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.ui.test.junit4)

    androidTestImplementation(platform(libs.androidx.compose.bom))

    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore.ktx)
    implementation(platform(libs.firebase.bom))
    implementation(libs.kotlinx.serialization.json.v160)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.firebase.analytics)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui.graphics)
    testImplementation(libs.junit)
    implementation (libs.v2.user)

    implementation(project(":core:designsystem"))
    implementation(project(":core:data"))
    implementation(project(":feature:login"))
    implementation(project(":feature:planet"))
    implementation(project(":feature:mypage"))
}