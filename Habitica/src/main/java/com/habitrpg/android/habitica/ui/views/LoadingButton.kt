package com.habitrpg.android.habitica.ui.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.habitrpg.android.habitica.R
import com.habitrpg.android.habitica.ui.theme.HabiticaTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class LoadingButtonState {
    CONTENT,
    DISABLED,
    LOADING,
    FAILED,
    SUCCESS
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoadingButton(
    state : LoadingButtonState,
    onClick : () -> Unit,
    modifier : Modifier = Modifier,
    elevation : ButtonElevation? = ButtonDefaults.elevation(0.dp),
    shape : Shape = MaterialTheme.shapes.medium,
    border : BorderStroke? = null,
    colors : ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = HabiticaTheme.colors.tintedUiSub,
        contentColor = Color.White
    ),
    contentPadding : PaddingValues = ButtonDefaults.ContentPadding,
    successContent : (@Composable RowScope.() -> Unit)? = null,
    failedContent : (@Composable RowScope.() -> Unit)? = null,
    content : @Composable RowScope.() -> Unit
) {
    val colorSpec = tween<Color>(350)
    val backgroundColor = animateColorAsState(
        targetValue = when (state) {
            LoadingButtonState.FAILED -> HabiticaTheme.colors.errorBackground
            LoadingButtonState.SUCCESS -> Color.Transparent
            else -> colors.backgroundColor(enabled = state != LoadingButtonState.DISABLED).value
        },
        animationSpec = colorSpec
    )
    val contentColor = animateColorAsState(
        targetValue = when (state) {
            LoadingButtonState.FAILED -> Color.White
            LoadingButtonState.SUCCESS -> HabiticaTheme.colors.successColor
            else -> colors.contentColor(enabled = state != LoadingButtonState.DISABLED).value
        },
        animationSpec = colorSpec
    )
    val borderWidth = animateDpAsState(
        targetValue = if (state == LoadingButtonState.SUCCESS) {
            3.dp
        } else {
            border?.width ?: 0.dp
        }
    )

    val buttonColors = ButtonDefaults.buttonColors(
        backgroundColor = backgroundColor.value,
        contentColor = contentColor.value
    )
    Button(
        {
            if (state == LoadingButtonState.CONTENT || state == LoadingButtonState.FAILED) {
                onClick()
            }
        },
        modifier
            .requiredHeight(40.dp)
            .animateContentSize(tween(350)),
        state != LoadingButtonState.DISABLED,
        elevation = elevation,
        shape = shape,
        border = if (state == LoadingButtonState.SUCCESS) BorderStroke(
            borderWidth.value,
            HabiticaTheme.colors.successBackground
        ) else border,
        colors = buttonColors,
        contentPadding = PaddingValues(0.dp)
    ) {
        AnimatedContent(
            targetState = state,
            transitionSpec = {
                val isInitialShowingContent =
                    initialState == LoadingButtonState.CONTENT || initialState == LoadingButtonState.DISABLED || (initialState == LoadingButtonState.SUCCESS && successContent == null)
                val isTargetShowingContent =
                    targetState == LoadingButtonState.CONTENT || targetState == LoadingButtonState.DISABLED || (targetState == LoadingButtonState.SUCCESS && successContent == null)
                if (targetState == LoadingButtonState.FAILED) {
                    fadeIn(
                        animationSpec = tween(220, delayMillis = 90)
                    ) +
                        slideInHorizontally(
                            animationSpec = spring(
                                dampingRatio = 0.2f,
                                stiffness = StiffnessMediumLow,
                            )
                        ) with
                        fadeOut(animationSpec = tween(90))
                } else if (isInitialShowingContent && isTargetShowingContent) {
                    fadeIn() with fadeOut()
                } else {
                    fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                        scaleIn(
                            initialScale = 0.92f,
                            animationSpec = tween(220, delayMillis = 90, FastOutSlowInEasing)
                        ) with
                        fadeOut(animationSpec = tween(90))
                }
            },
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(contentPadding)
        ) { state ->
            when (state) {
                LoadingButtonState.LOADING ->
                    CircularProgressIndicator(
                        color = contentColor.value,
                        modifier = Modifier.size(16.dp)
                    )

                LoadingButtonState.SUCCESS -> successContent?.let { it() } ?: content()
                LoadingButtonState.FAILED ->
                    failedContent?.let { it() } ?: Image(
                        painterResource(R.drawable.failed_loading),
                        stringResource(R.string.failed),
                        Modifier.padding(horizontal = 8.dp)
                    )
                else -> content()
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    var state : LoadingButtonState by remember { mutableStateOf(LoadingButtonState.CONTENT) }
    val scope = rememberCoroutineScope()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .width(200.dp)
            .padding(8.dp)
    ) {
        LoadingButton(state, {
            scope.launch {
                state = LoadingButtonState.LOADING
                delay(2000)
                state = LoadingButtonState.FAILED
                delay(2000)
                state = LoadingButtonState.LOADING
                delay(2000)
                state = LoadingButtonState.SUCCESS
                delay(2000)
                state = LoadingButtonState.DISABLED
                delay(2000)
                state = LoadingButtonState.CONTENT
            }
        }, successContent = {
            Text("I did it!")
        }, content = {
            Text("Do something")
        }, modifier = Modifier.fillMaxWidth())
        LoadingButton(LoadingButtonState.LOADING, {}, content = {
            Text("Do something")
        })
        LoadingButton(LoadingButtonState.LOADING, {}, content = {
            Text("Do something")
        }, modifier = Modifier.fillMaxWidth())
        LoadingButton(LoadingButtonState.FAILED, {}, content = {
            Text("Do something")
        })
        LoadingButton(LoadingButtonState.FAILED, {}, failedContent = {
            Text("Didn't work :(")
        }, content = {
            Text("Do something")
        })
        LoadingButton(LoadingButtonState.SUCCESS, {}, content = {
            Text("Do something")
        })
        LoadingButton(LoadingButtonState.SUCCESS, {}, successContent = {
            Text("Success!")
        }, content = {
            Text("Do something")
        })
    }
}