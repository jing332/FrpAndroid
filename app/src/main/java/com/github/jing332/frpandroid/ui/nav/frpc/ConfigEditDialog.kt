package com.github.jing332.frpandroid.ui.nav.frpc

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.github.jing332.frpandroid.ui.widgets.DenseOutlinedField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigEditDialog(
    onDismissRequest: () -> Unit,
    section: String,
    key: String,
    value: String,
    onSave: (String) -> Unit
) {
    AlertDialog(onDismissRequest = onDismissRequest) {
        Surface(tonalElevation = 8.dp, shape = MaterialTheme.shapes.medium) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.edit_config),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(8.dp)
                )
                HorizontalDivider()

                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    section,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                var text by remember { mutableStateOf(value) }
                DenseOutlinedField(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 24.dp, horizontal = 24.dp)
                        .fillMaxWidth(),
                    value = text,
                    onValueChange = { text = it },
                    singleLine = true,
                    label = { Text(key) }
                )

                Row(
                    Modifier
                        .align(Alignment.End)
                        .padding(end = 8.dp, bottom = 8.dp)
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { onSave(text) }) {
                        Text(text = stringResource(id = R.string.save))
                    }
                }
            }

        }
    }
}