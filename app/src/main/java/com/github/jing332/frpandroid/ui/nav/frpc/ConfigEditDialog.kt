package com.github.jing332.frpandroid.ui.nav.frpc

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.jing332.frpandroid.R
import com.github.jing332.frpandroid.ui.widgets.AppDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigEditDialog(
    onDismissRequest: () -> Unit,
    section: String,
    key: String,
    value: String,
    onSave: (String) -> Unit
) {
    var text by remember { mutableStateOf(value) }
    AppDialog(
        title = {
            Text(
                text = stringResource(R.string.edit_config),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(8.dp)
            )
        },
        content = {
//            Text(
//                section,
//
//                modifier = Modifier.align(Alignment.Center)
//            )

            OutlinedTextField(
                modifier = Modifier
                    .padding(vertical = 24.dp, horizontal = 24.dp)
                    .fillMaxWidth(),
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                label = {
                    Text(
                        key,
                        fontWeight = FontWeight.Bold,
                    )
                }
            )
        },
        buttons = {
            Row(
                Modifier
//                    .padding(end = 12.dp, bottom = 12.dp)
            ) {
                TextButton(onClick = onDismissRequest) {
                    Text(text = stringResource(id = R.string.cancel))
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = { onSave(text) }) {
                    Text(text = stringResource(id = R.string.save))
                }
            }
        },
        onDismissRequest = onDismissRequest
    )
}