# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile



# ColorKt와 관련된 클래스 유지
-keep class com.example.designsystem.theme.ColorKt { *; }
-keep class com.example.designsystem.theme.ThemeKt { *; }

# Login 관련 클래스 유지
-keep class com.example.login.LoginNavigationKt { *; }
-keep class com.example.login.LoginRoute { *; }

# MyPage 관련 클래스 유지
-keep class com.example.mypage.MyPageRoute { *; }
-keep class com.example.mypage.MypPageNavigationKt { *; }

# Planet 관련 클래스 유지
-keep class com.example.planet.navigation.PlanetNavigationKt { *; }
-keep class com.example.planet.navigation.PlanetRoute { *; }

# Keep classes related to Text components from designsystem module
-keep class com.example.designsystem.component.text.TextKt { *; }
-keep class com.example.designsystem.component.text.ItemKt { *; }
-keep class com.example.designsystem.component.text.RadioButtonKt { *; }
-keep class com.example.designsystem.component.text.TextFieldKt { *; }

# Firebase 관련 규칙 추가
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# Firebase 인증 관련 규칙
-keep class com.google.firebase.auth.** { *; }
-keep class com.google.firebase.firestore.** { *; }

# Firebase Analytics 관련 규칙
-keep class com.google.firebase.analytics.** { *; }

-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**