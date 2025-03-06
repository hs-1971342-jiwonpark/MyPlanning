plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
}

kotlin {
    androidTarget()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                implementation("io.ktor:ktor-client-core:2.0.0")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("androidx.core:core-ktx:1.9.0")
                implementation("androidx.compose.ui:ui:1.4.0")
            }
        }
        val iosMain by creating {
            dependencies {
                implementation("io.ktor:ktor-client-ios:2.0.0")
            }
        }
    }

    ios {
        binaries {
            framework {
                baseName = "shared"  // ✅ iOS 프레임워크 설정 추가
            }
        }
    }
}

android {
    namespace = "com.example.shared"  // ✅ 네임스페이스 추가 (실제 패키지명으로 변경)
    compileSdk = 34  // ✅ Android SDK 버전 추가

    defaultConfig {
        minSdk = 24
    }
}
