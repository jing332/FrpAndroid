package com.github.jing332.frpandroid.ui.nav.frpc

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.drake.net.utils.withIO
import com.github.jing332.frpandroid.R
import com.github.jing332.frpandroid.ui.widgets.DenseOutlinedField
import com.github.jing332.frpandroid.util.AndroidUtils.openUri
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConfigScreen(modifier: Modifier, vm: ConfigViewModel = viewModel()) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(vm.hashCode()) {
        scope.launch(Dispatchers.IO) {
            vm.init(context)
        }
    }

    var showDocumentDialog by remember { mutableStateOf("") }
    fun findDocument(name: String) {
        scope.launch(Dispatchers.Main) {
            showDocumentDialog = withIO { vm.findDocumentToMarkdown(name) }
        }
    }

    if (showDocumentDialog != "") {
        AlertDialog(
            onDismissRequest = { showDocumentDialog = "" },
            title = {
                Text(text = stringResource(R.string.help_document))
            },
            text = {
                SelectionContainer {
                    MarkdownText(
                        markdown = showDocumentDialog, modifier = Modifier.verticalScroll(
                            rememberScrollState()
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showDocumentDialog = "" }) {
                    Text(stringResource(id = R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    context.openUri("https://gofrp.org/docs/reference")
                }) {
                    Text(stringResource(R.string.frp_online_document))
                }
            },
        )
    }

    Column(modifier) {
        var searchKey by remember { mutableStateOf("") }
        Box(Modifier.fillMaxWidth()) {
            DenseOutlinedField(
                value = searchKey,
                onValueChange = { searchKey = it },
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(8.dp),
                label = { Text("搜索过滤") }
            )
        }
        LazyColumn(Modifier.fillMaxSize()) {
            vm.groupedItems.forEach { entry ->
                stickyHeader(key = entry.key.section) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                    ) {
                        Text(
                            text = entry.key.section,
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.Center),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                itemsIndexed(entry.value, key = { _, v -> v.id }) { index, item ->
                    IniConfigItem(
                        item.section,
                        item.key,
                        item.value,
                        onEdit = { /*vm.editConfig(item)*/ },
                        onDelete = { /*vm.deleteConfig(item)*/ },
                        onClick = {
                            findDocument(item.key)
                        }
                    )

                    if (index == entry.value.size - 1) {
                        Spacer(Modifier.height(48.dp))
                    } else {
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
fun IniConfigItem(
    section: String,
    key: String,
    value: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit,
) {
    Column(
        Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple()
            ) {
                onClick()
            }
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Text(key, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}