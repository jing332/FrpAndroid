package com.github.jing332.frpandroid.ui.nav.frpc

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.jing332.frpandroid.R
import com.github.jing332.frpandroid.constant.AppConst
import com.github.jing332.frpandroid.model.frp.Frpc
import com.github.jing332.frpandroid.ui.widgets.AppDialog
import com.github.jing332.frpandroid.ui.widgets.DenseOutlinedField
import com.github.jing332.frpandroid.util.ToastUtils.longToast
import java.io.File

@Composable
fun ConfigScreen(
    modifier: Modifier,
    key: String,
    onIniFilePath: () -> String = { "" },

    vm: ConfigViewModel = viewModel(key = key + "_config"),
    listVM: ConfigListViewModel = viewModel(key = key + "_configList"),
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(vm.hashCode()) {
//        scope.launch(Dispatchers.IO) {
//            vm.init(context)
//        }
    }

    var showMtTips by rememberSaveable {
        mutableStateOf(true)
    }

    fun openIniConfig() {
        if (showMtTips) {
            context.longToast(R.string.recommended_use_mt_for_editing)
            showMtTips = false
        }
        runCatching {
            val uri =
                FileProvider.getUriForFile(
                    /* context = */ context,
                    /* authority = */ AppConst.fileProviderAuthor,
                    /* file = */ File(onIniFilePath())
                )
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                setDataAndType(uri, "text/*")
            }

            context.startActivity(
                Intent.createChooser(
                    intent,
                    context.getString(R.string.edit_config_file,
                        onIniFilePath().split("/").last { it.isNotBlank() })
                )
            )
        }.onFailure {
            context.longToast(it.toString())
        }
    }

    var showConfigEditDialog by remember { mutableStateOf<ConfigListViewModel.Item?>(null) }
    if (showConfigEditDialog != null) {
        val item = showConfigEditDialog!!
        ConfigEditDialog(
            section = item.section,
            key = item.key,
            value = item.value,

            onDismissRequest = {
                showConfigEditDialog = null
            }, onSave = {
                showConfigEditDialog = null
                listVM.saveConfig(item.section, item.key, it)
            }
        )
    }

    var showRepeatWarnDialog by remember { mutableStateOf<ConfigListViewModel.Item?>(null) }
    if (showRepeatWarnDialog != null) {
        AppDialog(title = { Text(stringResource(id = R.string.warn)) }, content = {
            Text(stringResource(R.string.config_add_has_repeat))
        }, buttons = {
            TextButton(onClick = {
                showRepeatWarnDialog = null
            }) {
                Text(stringResource(id = R.string.cancel))
            }

            TextButton(onClick = {
                listVM.saveConfig(
                    showRepeatWarnDialog!!.section,
                    showRepeatWarnDialog!!.key,
                    showRepeatWarnDialog!!.value
                )
                showRepeatWarnDialog = null
            }) {
                Text("覆盖")
            }
        }, onDismissRequest = {
            showRepeatWarnDialog = null
        })
    }

    var showSelectionDialog by remember { mutableStateOf(false) }
    if (showSelectionDialog)
        ConfigSelectionDialog(onDismissRequest = {
            showSelectionDialog = false
        }) {
            showSelectionDialog = false

            listVM.hasRepeat(it.section, it.key).let { b ->
                if (b) {
                    showRepeatWarnDialog = it
                    return@ConfigSelectionDialog
                } else {
                    listVM.saveConfig(it.section, it.key, it.value)
                }
            }
        }

    Column(modifier) {
        var filterKey by remember { mutableStateOf("") }
        Box(Modifier.fillMaxWidth()) {
            Row(Modifier.align(Alignment.Center)) {
                DenseOutlinedField(
                    value = filterKey,
                    onValueChange = {
                        filterKey = it
                    },
                    modifier = Modifier
                        .padding(8.dp),
                    label = { Text(stringResource(R.string.search_filter)) }
                )

                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = {
                        showSelectionDialog = true
                    }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add_config)
                    )
                }

                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = { openIniConfig() }) {
                    Icon(painterResource(id = R.drawable.edit), "Edit")
                }
            }
        }

        ConfigListScreen(
            Modifier.fillMaxSize(),
            filterKey = filterKey,
            iniFilePath = Frpc(context).getConfigFilePath(),
            onClickItem = {
                showConfigEditDialog = it
            },
            vm = listVM,
        )
    }
}

