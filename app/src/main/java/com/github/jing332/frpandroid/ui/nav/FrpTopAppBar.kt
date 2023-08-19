package com.github.jing332.frpandroid.ui.nav

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.github.jing332.frpandroid.R
import com.github.jing332.frpandroid.ui.AboutDialog
import com.github.jing332.frpandroid.ui.LocalMainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrpTopAppBar(type: String, subTitle: String, version: String, onAddShortcut: () -> Unit) {
    val context = LocalContext.current
    val mainVM = LocalMainViewModel.current

    var showMoreOptions by remember { mutableStateOf(false) }

    var showAboutDialog by remember { mutableStateOf(false) }
    if (showAboutDialog) {
        AboutDialog {
            showAboutDialog = false
        }
    }

    TopAppBar(
        title = {
            Column {
                Text("$type - v${version}", maxLines = 1)
                Text(subTitle, style = MaterialTheme.typography.bodyMedium)
            }
        },
        actions = {
            IconButton(onClick = {
                onAddShortcut()
            }) {
                Icon(
                    Icons.Default.AddBusiness,
                    stringResource(R.string.add_desktop_shortcut)
                )
            }


            IconButton(onClick = {
                showMoreOptions = true
            }) {
                DropdownMenu(
                    expanded = showMoreOptions,
                    onDismissRequest = { showMoreOptions = false }) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.check_update)) },
                        onClick = {
                            showMoreOptions = false
                            mainVM.checkAppUpdate()
                        }
                    )

                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.about)) },
                        onClick = {
                            showMoreOptions = false
                            showAboutDialog = true
                        }
                    )
                }
                Icon(Icons.Default.MoreVert, stringResource(R.string.more_options))
            }
        }
    )
}