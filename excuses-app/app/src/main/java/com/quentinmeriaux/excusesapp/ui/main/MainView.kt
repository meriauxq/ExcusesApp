package com.quentinmeriaux.excusesapp.ui.main

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.quentinmeriaux.excusesapp.R
import com.quentinmeriaux.excusesapp.ui.components.AddExcuseView
import com.quentinmeriaux.excusesapp.ui.components.ExcuseView
import com.quentinmeriaux.excusesapp.ui.components.LostView
import com.quentinmeriaux.excusesapp.ui.viewmodel.ExcusesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(
    viewModel: ExcusesViewModel
) {
    val scope = rememberCoroutineScope()

    // UI state values
    var query by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }
    var lost by rememberSaveable { mutableStateOf(false) }
    var showAddExcuseDialog by rememberSaveable { mutableStateOf(false) }

    // State Flows
    val randomExcuse by viewModel.randomExcuse.collectAsState(initial = null)
    val searchedExcuse by viewModel.searchedExcuse.collectAsState(initial = null)
    val loading by viewModel.loading.collectAsState(initial = false)
    val error by viewModel.error.collectAsState(initial = null)

    /**
     * Reset the search view and deactivate it
     */
    fun resetSearch() {
        query = ""
        lost = false
        active = false
    }

    val context = LocalContext.current

    // Handle error messages (trivial)
    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                query = query,
                onQueryChange = { query = it },
                onSearch = {
                    viewModel.getExcuse(it.toIntOrNull())
                },
                active = active,
                onActiveChange = { active = it },
                placeholder = { Text(stringResource(R.string.search_placeholder)) },
                leadingIcon = {
                    if (active) {
                        Icon(
                            modifier = Modifier.clickable {
                                resetSearch()
                            },
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    } else {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                },
                trailingIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            active = true
                            lost = true
                        },
                        imageVector = Icons.Default.Help,
                        contentDescription = null
                    )
                }
            ) {
                if (loading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    if (lost) {
                        LostView(::resetSearch)
                    } else {
                        searchedExcuse?.let {
                            ExcuseView(excuse = it)
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddExcuseDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                )
            } else {
                if (showAddExcuseDialog) {
                    AddExcuseView(
                        onAddExcuse = { excuse ->
                            // Call your ViewModel function to add the excuse
                            viewModel.addExcuse(excuse)
                            // Dismiss the dialog
                            showAddExcuseDialog = false
                        },
                        onDismiss = { showAddExcuseDialog = false }
                    )
                }
                randomExcuse?.let {
                    ExcuseView(excuse = it)
                }
                Button(
                    modifier = Modifier
                        .padding(16.dp),
                    onClick = {
                        scope.launch {
                            viewModel.generateRandomExcuse()
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.generate_an_excuse))
                }
            }
        }
    }
}