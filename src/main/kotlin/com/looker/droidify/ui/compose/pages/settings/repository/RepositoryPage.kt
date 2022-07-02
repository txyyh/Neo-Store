package com.looker.droidify.ui.compose.pages.settings.repository

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.looker.droidify.ui.compose.RepositoriesRecycler
import com.looker.droidify.ui.viewmodels.RepositoriesViewModelX

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryPage(viewModel: RepositoriesViewModelX) {
    val repos by viewModel.repositories.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = { viewModel.showRepositorySheet(editMode = true) }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add Repository")
                Text(text = "Add Repository")
            }
        }
    ) {
        val sortedRepoList = remember(repos) { repos.sortedBy { !it.enabled } }
        RepositoriesRecycler(
            repositoriesList = sortedRepoList,
            onClick = { viewModel.toggleRepository(it, it.enabled) },
            onLongClick = { viewModel.showRepositorySheet(it.id) }
        )

    }
}