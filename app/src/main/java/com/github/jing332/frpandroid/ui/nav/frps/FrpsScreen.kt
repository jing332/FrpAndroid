package com.github.jing332.frpandroid.ui.nav.frps

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.github.jing332.frpandroid.R
import com.github.jing332.frpandroid.model.frp.Frps
import com.github.jing332.frpandroid.ui.nav.FrpTopAppBar

@Composable
fun FrpsScreen() {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            FrpTopAppBar(
                type = stringResource(id = R.string.frps),
                subTitle = "Server",
                version = Frps(context).version()
            ) {
            }
        }
    ) {

    }
}