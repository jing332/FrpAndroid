package com.github.jing332.frpandroid.ui

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.github.jing332.frpandroid.BuildConfig
import com.github.jing332.frpandroid.R
import com.github.jing332.frpandroid.model.frp.Frpc
import com.github.jing332.frpandroid.ui.widgets.AppDialog

@Composable
fun AboutDialog(onDismissRequest: () -> Unit) {
    val context = LocalContext.current

    AppDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.ic_app_launcher_foreground),
                    contentDescription = "Logo",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Text(
                    stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 8.dp)
                )
            }
        },
        content = {
            fun openUrl(uri: String) {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW).apply {
                        data = uri.toUri()
                    }
                )
            }

            Column {
                val frpVersion = remember { Frpc(context).version() }
                SelectionContainer {
                    Column {
                        Text("APP - ${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})")
                        Text("Frp - $frpVersion")
                    }
                }
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                Text(
                    "Github - FrpAndroid",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable {
                            openUrl("https://github.com/jing332/FrpAndroid")
                        }
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Github - Frp",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable {
                            openUrl("https://github.com/fatedier/frp/releases/${frpVersion}")
                        }
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                )

            }
        },
        buttons = {
            TextButton(onClick = {
                onDismissRequest()
                context.startActivity(
                    Intent(context, LibrariesActivity::class.java).setAction(
                        Intent.ACTION_VIEW
                    )
                )
            }) {
                Text(text = stringResource(id = R.string.open_source_license))
            }

            TextButton(onClick = { onDismissRequest() }) {
                Text(stringResource(id = R.string.ok))
            }
        })
}