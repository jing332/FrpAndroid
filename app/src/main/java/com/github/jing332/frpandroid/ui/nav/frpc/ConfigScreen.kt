package com.github.jing332.frpandroid.ui.nav.frpc

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.jing332.frpandroid.R
import com.github.jing332.frpandroid.model.frp.Frpc
import com.github.jing332.frpandroid.ui.widgets.DenseOutlinedField
import com.github.jing332.frpandroid.util.ToastUtils.longToast

@Composable
fun ConfigScreen(
    modifier: Modifier,
    key: String,

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

    var showSelectionDialog by remember { mutableStateOf(false) }
    if (showSelectionDialog)
        ConfigSelectionDialog(onDismissRequest = {
            showSelectionDialog = false
        }) {
            context.longToast(it.toString())
        }

    Column(modifier) {
        var filterKey by remember { mutableStateOf("") }
        Box(Modifier.fillMaxWidth()) {
            Row(Modifier.align(Alignment.Center)) {
                DenseOutlinedField(
                    value = filterKey,
                    onValueChange = {
                        filterKey = it
//                        vm.filter(searchKey)
                    },
                    modifier = Modifier
                        .padding(8.dp),
                    label = { Text(stringResource(R.string.search_filter)) }
                )

                IconButton(onClick = {
                    showSelectionDialog = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add_config)
                    )
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

