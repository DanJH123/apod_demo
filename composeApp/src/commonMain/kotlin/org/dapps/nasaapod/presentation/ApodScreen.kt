package org.dapps.nasaapod.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import apod.composeapp.generated.resources.Res
import apod.composeapp.generated.resources.apod_is_video
import apod.composeapp.generated.resources.retry_from_error
import com.kmpalette.palette.graphics.Palette
import com.skydoves.landscapist.palette.rememberPaletteState
import org.dapps.nasaapod.util.MediaType
import org.jetbrains.compose.resources.stringResource

@Composable
fun ApodStack(viewModel: ApodViewModel, modifier: Modifier = Modifier) {
    var palette by rememberPaletteState(null)
    val backgroundColour = palette?.dominantSwatch?.rgb?.let { Color(it) }
        ?: MaterialTheme.colors.background

    Box(
        modifier = modifier
            .background(backgroundColour)
            .fillMaxSize()
    ) {
        SwipeCard(
            onSwipeLeft = { viewModel.previousDay() },
            onSwipeRight = { viewModel.nextDay() }
        ) {
            ApodView(
                viewModel = viewModel,
                palette = palette,
                onPaletteChange = { palette = it }
            )
        }
    }
}

@Composable
fun ApodView(
    viewModel: ApodViewModel,
    palette: Palette?,
    modifier: Modifier = Modifier,
    onPaletteChange: (Palette) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val uiState by viewModel.apodUiState.collectAsStateWithLifecycle()
        when (uiState) {
            is ApodUiState.Initial,
            is ApodUiState.Loading -> ApodLoading()
            is ApodUiState.Error -> ApodError(
                uiState as ApodUiState.Error,
                onRetryPressed = { viewModel.reload() }
            )
            is ApodUiState.Success -> ApodContent(
                uiState = uiState as ApodUiState.Success,
                palette = palette,
                onPaletteChange = onPaletteChange
            )
        }
    }
}


@Composable
fun ApodLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator()
    }
}

@Composable
fun ApodError(uiState: ApodUiState.Error, modifier: Modifier = Modifier, onRetryPressed: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(stringResource(uiState.message))
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetryPressed) {
            Text(stringResource(Res.string.retry_from_error))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ApodContent(
    uiState: ApodUiState.Success,
    palette: Palette?,
    modifier: Modifier = Modifier,
    onPaletteChange: (Palette) -> Unit = {},
) {
    val state = uiState.apod
    val isVideo = state.mediaType is MediaType.Video

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed,
        )
    )

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val maxHeightDp = maxHeight
        val imageHeight = maxHeightDp * 2f / 3f
        val sheetPeekHeight = maxHeightDp * 1f / 3f
        val heightAnimDurationMillis = 350

        val isPaletteReady by derivedStateOf { palette != null }
        var isImageReady by rememberSaveable { mutableStateOf(false) }

        val backgroundColour = remember(palette) {
            palette?.dominantSwatch?.rgb?.let { Color(it) } ?: Color.Black
        }
        val textColour = remember(palette) {
            palette?.dominantSwatch?.bodyTextColor?.let { Color(it) } ?: Color.White
        }
        val secondaryColour = remember(palette) {
            palette?.mutedSwatch?.rgb?.let { Color(it) } ?: Color.Gray
        }

        val animatedImageHeight by animateDpAsState(
            targetValue = if (isImageReady) imageHeight else maxHeightDp,
            animationSpec = tween(durationMillis = heightAnimDurationMillis)
        )
        val animatedSheetPeekHeight by animateDpAsState(
            targetValue = if (isImageReady) sheetPeekHeight else 0.dp,
            animationSpec = tween(durationMillis = heightAnimDurationMillis)
        )

        val animatedBgColour by animateColorAsState(
            targetValue = if (isPaletteReady) backgroundColour else MaterialTheme.colors.background,
            label = "animatedBgColour"
        )
        val animatedTextColour by animateColorAsState(
            targetValue = if (isPaletteReady) textColour else MaterialTheme.colors.onBackground,
            label = "animatedTextColour"
        )
        val animatedSecondaryColour by animateColorAsState(
            targetValue = if (isPaletteReady) secondaryColour else MaterialTheme.colors.background,
            label = "animatedSecondaryColour"
        )

        BottomSheetScaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = bottomSheetScaffoldState,
            sheetPeekHeight = animatedSheetPeekHeight,
            sheetBackgroundColor = animatedBgColour,
            backgroundColor = animatedBgColour,
            sheetContent = {
                BasicBottomSheet(state.title, state.explanation, animatedTextColour)
            },
        ) {
            Box(
                modifier = Modifier
                    .height(animatedImageHeight)
                    .fillMaxWidth()
            ) {
                if (isVideo) {
                    Text(
                        stringResource(Res.string.apod_is_video),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        color = animatedTextColour,
                    )
                } else {
                    ExpandableImage(
                        modifier = Modifier.fillMaxSize(),
                        imageUrl = state.mediaType.url,
                        hdImageUrl = state.mediaType.hdUrl,
                        contentDescription = state.title,
                        imageHeight = animatedImageHeight,
                        backgroundColour = animatedSecondaryColour,
                        textColour = animatedTextColour,
                        onImageLoaded = { isImageReady = true },
                        onImageLoading = { isImageReady = false },
                        onPaletteChange = onPaletteChange
                    )
                }
                Text(
                    color = animatedTextColour,
                    text = state.date,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                )
                state.copyright?.let {
                    Text(
                        color = animatedTextColour,
                        text = it,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 8.dp)
                    )
                }
            }
        }
    }
}