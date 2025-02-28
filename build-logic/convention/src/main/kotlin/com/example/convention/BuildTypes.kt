package com.example.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.example.convention.com.example.convention.ExtensionType
import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

internal fun Project.configureBuildTypes(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    extensionType: ExtensionType
) {
    commonExtension.run {
        buildFeatures {
            buildConfig = true
        }

        val apiKey = gradleLocalProperties(rootDir, providers).getProperty("API_KEY")
        val kakaoKey = gradleLocalProperties(rootDir, providers).getProperty("KAKAO_API_KEY")

        when (extensionType) {
            ExtensionType.APPLICATION -> {
                extensions.configure<ApplicationExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(apiKey, kakaoKey)
                            buildConfigField("String", "KAKAO_API_KEY", "\"${gradleLocalProperties(rootDir, providers).getProperty("KAKAO_API_KEY")}\"")
                            manifestPlaceholders["KAKAO_API_KEY"] = kakaoKey

                        }
                        create("staging") {
                            configureStagingBuildType(apiKey)
                            buildConfigField("String", "KAKAO_API_KEY", "\"${gradleLocalProperties(rootDir, providers).getProperty("KAKAO_API_KEY")}\"")
                            manifestPlaceholders["KAKAO_API_KEY"] = kakaoKey
                        }
                        release {
                            configureReleaseBuildType(commonExtension, apiKey, kakaoKey,true)
                            buildConfigField("String", "KAKAO_API_KEY", "\"${gradleLocalProperties(rootDir, providers).getProperty("KAKAO_API_KEY")}\"")
                            manifestPlaceholders["KAKAO_API_KEY"] = kakaoKey
                        }
                    }
                }
            }

            ExtensionType.LIBRARY -> {
                extensions.configure<LibraryExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(apiKey,kakaoKey)
                        }
                        create("staging") {
                            configureStagingBuildType(apiKey)
                        }
                        release {
                            configureReleaseBuildType(commonExtension, apiKey, kakaoKey, false)
                        }
                    }
                }
            }
        }
    }
}

private fun BuildType.configureDebugBuildType(apiKey: String,kakaoKey : String) {
    buildConfigField("String", "API_KEY", "\"$apiKey\"")
    buildConfigField("String", "BASE_URL", "\"DEBUG_API_URL\"")
    buildConfigField("String", "KAKAO_API_KEY", "\"$kakaoKey\"")
}

private fun BuildType.configureStagingBuildType(apiKey: String) {
    buildConfigField("String", "API_KEY", "\"$apiKey\"")
    buildConfigField("String", "BASE_URL", "\"STAGING_API_URL\"")
}

private fun BuildType.configureReleaseBuildType(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    apiKey: String,
    kakaoKey : String,
    isMinifyEnabled: Boolean
) {
    buildConfigField("String", "API_KEY", "\"$apiKey\"")
    buildConfigField("String", "BASE_URL", "\"RELEASE_API_URL\"")
    buildConfigField("String", "KAKAO_API_KEY", "\"$kakaoKey\"")

    this.isMinifyEnabled = isMinifyEnabled // 설정에 따라 minifyEnabled 값 적용
    proguardFiles(
        commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )
}


internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.run {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = libs.findVersion("composeCompiler").get().toString()
        }

        dependencies {
            val bom = libs.findLibrary("androidx.compose.bom").get()
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))
            add("debugImplementation", libs.findLibrary("androidx.ui.tooling.preview").get())
        }
    }
}

fun DependencyHandlerScope.addUILayerDependencies(project: Project) {
    add("implementation", project(":core:presentation:designsystem"))
    add("implementation", project.libs.findBundle("compose").get())
    add("debugImplementation", project.libs.findBundle("compose.debug").get())
    add("androidTestImplementation", project.libs.findLibrary("androidx.ui.test.junit4").get())
}
