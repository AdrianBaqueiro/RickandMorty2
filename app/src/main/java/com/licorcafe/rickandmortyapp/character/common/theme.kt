package com.licorcafe.rickandmortyapp.character.common

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ExtendedColors(
    val primary: Color,
    val tertiary: Color,
    val onTertiary: Color
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        primary = Color(0xFF49be25),
        tertiary = Color.Unspecified,
        onTertiary = Color.Unspecified
    )
}

@Composable
fun ExtendedTheme(
    content: @Composable () -> Unit
) {
    val extendedColors = ExtendedColors(
        primary = Color(0xFF49be25),
        tertiary = Color(0xFF49be25),
        onTertiary = Color(0xFF002021)
    )
    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            content = content
        )
    }
}

// Use with eg. ExtendedTheme.colors.tertiary
object ExtendedTheme {
    val colors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
}