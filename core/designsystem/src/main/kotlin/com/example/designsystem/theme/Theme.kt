package com.example.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.designsystem.R

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)


val CustomFontFamily = FontFamily(
    Font(R.font.pretendard_regular),
    Font(R.font.pretendard_bold, FontWeight.Bold),
    Font(R.font.pretendard_thin, FontWeight.Normal),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),
    Font(R.font.pretendard_extrabold, FontWeight.ExtraBold),
    Font(R.font.pretendard_medium, FontWeight.Medium),
)

val customAppBar = TextStyle(
    fontFamily = CustomFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 24.sp,
    color = Color.White
)

val customPlanetItemText = TextStyle(
    fontFamily = CustomFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 12.sp,
    color = Color.White
)

@Composable
fun MyPlanningTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }


    // Custom Typography 정의
    val AppTypography = Typography(
        // Display styles (헤드라인 큰 스타일)
        displayLarge = TextStyle(
            fontFamily = CustomFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 57.sp
        ),
        displayMedium = TextStyle(
            fontFamily = CustomFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 45.sp
        ),
        displaySmall = TextStyle(
            fontFamily = CustomFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 36.sp
        ),

        // Headline styles (중간~작은 헤드라인)
        headlineLarge = TextStyle(
            fontFamily = CustomFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 32.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = CustomFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 28.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = CustomFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp
        ),

        // Title styles (제목, 서브타이틀)
        titleLarge = TextStyle(
            fontFamily = CustomFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp
        ),
        titleMedium = TextStyle(
            fontFamily = CustomFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        ),
        titleSmall = TextStyle(
            fontFamily = CustomFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        ),

        // Body styles (본문 텍스트)
        bodyLarge = TextStyle(
            fontFamily = CustomFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = CustomFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        ),
        bodySmall = TextStyle(
            fontFamily = CustomFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        ),

        // Label styles (라벨, 버튼 텍스트)
        labelLarge = TextStyle(
            fontFamily = CustomFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        ),
        labelMedium = TextStyle(
            fontFamily = CustomFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        ),
        labelSmall = TextStyle(
            fontFamily = CustomFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp
        )
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )

}