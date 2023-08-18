package com.github.jing332.frpandroid.ui.nav.frpc

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.jing332.frpandroid.R
import com.github.jing332.frpandroid.ui.widgets.DenseOutlinedField
import com.github.jing332.frpandroid.util.FileUtils.readAllText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigSelectionDialog(
    onDismissRequest: () -> Unit,
    onClick: (ConfigListViewModel.Item) -> Unit
) {
    val context = LocalContext.current
    val iniString = remember {
        context.assets.open("defaultData/frpc_full.ini").readAllText()
    }

    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .imePadding()
        ) {
            var searchKey by remember { mutableStateOf("") }
            DenseOutlinedField(
                value = searchKey,
                onValueChange = { searchKey = it },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                label = { Text(stringResource(id = R.string.search_filter)) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            ConfigListScreen(
                modifier = Modifier.fillMaxSize(),
                iniString = iniString,
                filterKey = searchKey,
                onClickItem = onClick
            )
        }
    }
}