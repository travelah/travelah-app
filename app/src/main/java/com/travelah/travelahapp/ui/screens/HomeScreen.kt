package com.travelah.travelahapp.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.travelah.travelahapp.view.ViewModelFactory
import com.travelah.travelahapp.view.main.MainViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.travelah.travelahapp.ui.components.contents.HomeContent

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ),
) {
    // TO DO: Fetch Data from Backend
    HomeContent(modifier = modifier.padding(20.dp).fillMaxWidth())
}