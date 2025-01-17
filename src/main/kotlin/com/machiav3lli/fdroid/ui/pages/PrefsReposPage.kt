package com.machiav3lli.fdroid.ui.pages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.machiav3lli.fdroid.R
import com.machiav3lli.fdroid.ui.activities.PrefsActivityX
import com.machiav3lli.fdroid.ui.compose.RepositoriesRecycler
import com.machiav3lli.fdroid.ui.fragments.EditRepositorySheetX
import com.machiav3lli.fdroid.ui.fragments.RepositorySheetX
import com.machiav3lli.fdroid.ui.viewmodels.RepositoriesViewModelX
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrefsReposPage(viewModel: RepositoriesViewModelX) {
    val context = LocalContext.current
    val prefsActivityX = context as PrefsActivityX
    val repos by viewModel.repositories.collectAsState()

    LaunchedEffect(key1 = viewModel.showSheet) {
        viewModel.showSheet.collectLatest {
            if (it.editMode) {
                EditRepositorySheetX(it.repositoryId).showNow(
                    prefsActivityX.supportFragmentManager,
                    "Repository ${it.repositoryId}"
                )
            } else {
                RepositorySheetX(it.repositoryId).showNow(
                    prefsActivityX.supportFragmentManager,
                    "Repository $it"
                )
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {
                viewModel.showRepositorySheet(
                    editMode = true,
                    addNew = true
                )
            }) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(id = R.string.add_repository)
                )
                Text(text = stringResource(id = R.string.add_repository))
            }
        }
    ) { paddingValues ->
        val sortedRepoList = remember(repos) { repos.sortedBy { !it.enabled } }
        RepositoriesRecycler(
            modifier = Modifier.padding(paddingValues),
            repositoriesList = sortedRepoList,
            onClick = {
                CoroutineScope(Dispatchers.Default).launch {
                    prefsActivityX.syncConnection.binder?.setEnabled(it, it.enabled)
                }
            },
            onLongClick = { viewModel.showRepositorySheet(it.id) }
        )

    }
}
