package com.github.jing332.frpandroid.ui.nav.frpc

import android.content.IntentFilter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.drake.net.utils.withIO
import com.github.jing332.frpandroid.R
import com.github.jing332.frpandroid.data.appDb
import com.github.jing332.frpandroid.data.entities.FrpLog
import com.github.jing332.frpandroid.model.frp.Frpc
import com.github.jing332.frpandroid.service.FrpServiceManager.frpcSwitch
import com.github.jing332.frpandroid.service.FrpcService
import com.github.jing332.frpandroid.ui.LocalMainViewModel
import com.github.jing332.frpandroid.ui.nav.FrpTopAppBar
import com.github.jing332.frpandroid.ui.widgets.LocalBroadcastReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrpScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val mainVM = LocalMainViewModel.current
    val view = LocalView.current
    var running by remember { mutableStateOf(false) }

    LocalBroadcastReceiver(intentFilter = IntentFilter(FrpcService.ACTION_STATUS_CHANGED)) {
        println(it?.action)

        if (it?.action == FrpcService.ACTION_STATUS_CHANGED) {
            running = FrpcService.frpcRunning
        }
    }

    fun switch() {
        context.frpcSwitch()
    }

    Scaffold(modifier = Modifier.imePadding(),
        topBar = {
            var version by remember { mutableStateOf("") }
            LaunchedEffect(Unit) {
                scope.launch(Dispatchers.Main) {
                    version = withIO { Frpc(context).version() }
                }
            }
            FrpTopAppBar(type = stringResource(id = R.string.frpc), version = version) {

            }
        }
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 8.dp)
                .padding(bottom = 16.dp)
        ) {
            ServerLogScreen(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                appDb.frpLogDao.flowAll(FrpLog.Type.FRPC)
            )

            Column(
                Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Bottom,
            ) {
                Switch(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    checked = running,
                    onCheckedChange = { switch() },
                )
            }
        }
    }
}