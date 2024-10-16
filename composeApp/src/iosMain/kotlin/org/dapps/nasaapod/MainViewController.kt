package org.dapps.nasaapod

import androidx.compose.ui.window.ComposeUIViewController
import org.dapps.nasaapod.di.initKoin

fun MainViewController() = ComposeUIViewController(configure = { initKoin() }) { App() }