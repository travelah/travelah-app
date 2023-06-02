package com.travelah.travelahapp.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.travelah.travelahapp.view.ViewModelFactory
import com.travelah.travelahapp.view.main.MainViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.travelah.travelahapp.data.remote.models.Profile
import com.travelah.travelahapp.ui.components.contents.HomeContent
import com.travelah.travelahapp.data.Result
import com.travelah.travelahapp.R
import com.travelah.travelahapp.data.remote.models.Post
import com.travelah.travelahapp.ui.components.elements.ErrorText

@Composable
fun HomeScreen(
    result: Result<List<Post>>,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ),
) {
    val profileState: State<Profile?> = viewModel.getProfile().observeAsState()

    when (result) {
        is Result.Loading -> {
            Text(text = stringResource(R.string.loading))
        }
        is Result.Success -> {
            HomeContent(
                listPost = result.data,
                profileName = profileState.value?.fullName ?: "",
                modifier = modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            )
        }
        is Result.Error -> {
            ErrorText(text = stringResource(id = R.string.failed_error))
        }
    }
}