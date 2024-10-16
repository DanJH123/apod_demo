package org.dapps.nasaapod.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kmpalette.palette.graphics.Palette
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.animation.circular.CircularRevealPlugin
import com.skydoves.landscapist.coil3.CoilImage
import com.skydoves.landscapist.coil3.CoilImageState
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.palette.PalettePlugin
import com.skydoves.landscapist.placeholder.shimmer.Shimmer
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin

@Composable
fun ExpandableImage(
    imageUrl: String,
    hdImageUrl: String?,
    contentDescription: String,
    imageHeight: Dp,
    backgroundColour: Color,
    textColour: Color,
    modifier: Modifier = Modifier,
    onImageLoaded: () -> Unit = {},
    onImageLoading: () -> Unit = {},
    onImageFailed: () -> Unit = {},
    onPaletteChange: (Palette) -> Unit = {},
) {
    var imageEnlarged by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(imageHeight)
            .background(backgroundColour)
            .clickable { imageEnlarged = true }
    ) {
        CoilImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
                .align(Alignment.TopCenter),
            imageModel = { imageUrl },
            onImageStateChanged = {
                when (it) {
                    is CoilImageState.Success -> onImageLoaded()
                    is CoilImageState.Loading -> onImageLoading()
                    is CoilImageState.Failure -> onImageFailed()
                    is CoilImageState.None -> {}
                }
            },
            imageOptions = ImageOptions(
                contentDescription = contentDescription,
                contentScale = ContentScale.FillHeight,
                alignment = Alignment.TopCenter,
            ),
            component = rememberImageComponent {
                +PalettePlugin { onPaletteChange(it) }
                +CircularRevealPlugin(duration = 350)
                +ShimmerPlugin(
                    Shimmer.Fade(
                        baseColor = Color.Black,
                        highlightColor = Color.LightGray,
                        duration = 400
                    ),
                )
            },
            failure = {
                Box(modifier = Modifier.fillMaxSize()
                    .align(Alignment.Center)) {
                    Text(
                        "Failed to load image",
                        color = textColour,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        )
    }
    if (imageEnlarged) {
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = { imageEnlarged = false }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColour)
            ) {
                CoilImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { imageEnlarged = false },
                    imageModel = { hdImageUrl ?: imageUrl },
                    imageOptions = ImageOptions(
                        contentDescription = contentDescription,
                        contentScale = ContentScale.Fit,
                        alignment = Alignment.Center
                    ),
                    component = rememberImageComponent {
                        +ShimmerPlugin(
                            Shimmer.Fade(
                                baseColor = Color.Black,
                                highlightColor = Color.LightGray,
                                duration = 400
                            ),
                        )
                    },
                    failure = {
                        Box(modifier = Modifier.fillMaxSize()
                            .align(Alignment.Center)) {
                            Text(
                                "Failed to load image",
                                color = textColour,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                )
            }
        }
    }
}