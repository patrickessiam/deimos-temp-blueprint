package com.deimosdev.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.deimosdev.components.CircularIndeterminateProgressBar
import com.deimosdev.components.DefaultSnackbar

private val lightThemeColors = lightColors(
    primary = Blue600,
    primaryVariant = Blue400,
    onPrimary = Black2,
    secondary = Color.White,
    secondaryVariant = Teal300,
    onSecondary = Color.Black,
    error = RedErrorDark,
    onError = RedErrorLight,
    background = Grey1,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Black2,
)

private val darkThemeColors = darkColors(
    primary = Blue700,
    primaryVariant = Color.White,
    onPrimary = Color.White,
    secondary = Black1,
    onSecondary = Color.White,
    error = RedErrorLight,
    background = Color.Black,
    onBackground = Color.White,
    surface = Black1,
    onSurface = Color.White,
)

@ExperimentalMaterialApi
@Composable
fun AppTheme(
    darkTheme: Boolean,
    displayProgressBar: Boolean,
    scaffoldState: ScaffoldState,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = if (darkTheme) darkThemeColors else lightThemeColors,
        typography = NunitoTypography,
        shapes = AppShapes
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = if (!darkTheme) Grey1 else Color.Black)
        ) {
            content()
            CircularIndeterminateProgressBar(isDisplayed = displayProgressBar, 0.3f)
            DefaultSnackbar(
                snackbarHostState = scaffoldState.snackbarHostState, onDismiss = {
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                }, modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}





























