pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "MyPlanning2"
include(":app")
include(":core")
include(":core:designsystem")
include(":feature")
include(":feature:login")
include(":core:data")
include(":feature:planet")
include(":feature:mypage")
include(":feature:network")
include(":feature:myaccount")
include(":feature:rule")
include(":feature:makeplanet")
include(":common")
include(":common:navigation")
include(":common:shared")
