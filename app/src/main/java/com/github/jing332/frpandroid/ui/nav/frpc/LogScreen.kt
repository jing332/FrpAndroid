package com.github.jing332.frpandroid.ui.nav.frpc

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.jing332.frpandroid.R
import com.github.jing332.frpandroid.constant.LogLevel
import com.github.jing332.frpandroid.constant.LogLevel.Companion.toLevelString
import com.github.jing332.frpandroid.data.entities.FrpLog
import kotlinx.coroutines.flow.Flow

@Composable
fun LogScreen(modifier: Modifier, flowList: Flow<List<FrpLog>>, paddingBottom: Dp = 48.dp) {
//    val list by appDb.serverLogDao.flowAll().collectAsState(initial = emptyList())
    val list by flowList.collectAsState(initial = emptyList())

    var showDescDialog by remember { mutableStateOf<String?>(null) }
    if (showDescDialog != null)
        AlertDialog(
            onDismissRequest = { showDescDialog = null },
            title = { Text(stringResource(R.string.description)) },
            text = { Text(showDescDialog.toString()) },
            confirmButton = {
                TextButton(
                    onClick = { showDescDialog = null }) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    LazyColumn(
        Modifier
            .padding(8.dp)
            .fillMaxSize(),
    ) {
        itemsIndexed(list, key = { _, v -> v.id }) { index, item ->
            SelectionContainer {
                Text(
                    "[${item.level.toLevelString()}] " + item.message,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    color = when (item.level) {
                        LogLevel.DEBUG -> MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.5f
                        )

                        LogLevel.INFO -> MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.8f
                        )

                        LogLevel.WARN -> MaterialTheme.colorScheme.tertiary
                        LogLevel.ERROR -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.primary
                    },
                    overflow = TextOverflow.Visible,
                    lineHeight = LocalTextStyle.current.lineHeight * 0.8
                )
            }

            if (index == list.size - 1) // 防止浮动按钮遮挡log
                Spacer(modifier = Modifier.height(paddingBottom))
        }
    }
}