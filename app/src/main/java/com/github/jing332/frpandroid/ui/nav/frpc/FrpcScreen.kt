package com.github.jing332.frpandroid.ui.nav.frpc

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.drake.net.utils.withIO
import com.github.jing332.frpandroid.R
import com.github.jing332.frpandroid.config.AppConfig
import com.github.jing332.frpandroid.constant.FrpType
import com.github.jing332.frpandroid.data.appDb
import com.github.jing332.frpandroid.model.frp.Frpc
import com.github.jing332.frpandroid.service.FrpServiceManager.frpcSwitch
import com.github.jing332.frpandroid.service.FrpcService
import com.github.jing332.frpandroid.ui.LocalMainViewModel
import com.github.jing332.frpandroid.ui.MyTools
import com.github.jing332.frpandroid.ui.SwitchFrpActivity
import com.github.jing332.frpandroid.ui.nav.BasicFrpScreen
import com.github.jing332.frpandroid.ui.nav.FrpTopAppBar
import com.github.jing332.frpandroid.ui.widgets.LocalBroadcastReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FrpcScreen() {
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

    LaunchedEffect(Unit) {
        AppConfig.frpPageType.setValueWithoutSave(FrpType.FRPC)
    }

    fun switch() {
        context.frpcSwitch()
    }

    Scaffold(
        topBar = {
            var version by remember { mutableStateOf("") }
            LaunchedEffect(Unit) {
                scope.launch(Dispatchers.Main) {
                    version = withIO { Frpc(context).version() }
                }
            }
            FrpTopAppBar(type = stringResource(id = R.string.frpc), "Client", version = version) {
                MyTools.addShortcut(
                    ctx = context,
                    name = "frpc",
                    id = "frpc",
                    iconResId = R.drawable.ic_frpc,
                    launcherIntent = Intent(context, SwitchFrpActivity::class.java).apply {
                        type = "frpc"
                    }
                )
            }
        }
    ) { paddingValues ->
        var currentPageIndex by remember { AppConfig.subPageIndex }
        Box(
            Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .fillMaxSize()
        ) {
            BasicFrpScreen(
                modifier = Modifier
                    .padding(8.dp),
                configScreen = {
                    ConfigScreen(Modifier.fillMaxSize(), key = "frpc", onIniFilePath = {
                        Frpc(context).getConfigFilePath()
                    })
                },
                logScreen = {
                    LogScreen(
                        modifier = Modifier
                            .fillMaxWidth(),
                        appDb.frpLogDao.flowAll(FrpType.FRPC),
                        paddingBottom = 48.dp
                    )
                },
                pageIndex = currentPageIndex,
                onPageIndexChanged = { currentPageIndex = it }
            )

            SwitchFloatingButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                switch = running
            ) {
                switch()
            }
        }

    }
}

@Composable
fun SwitchFloatingButton(modifier: Modifier, switch: Boolean, onSwitchChange: (Boolean) -> Unit) {
    val scope = rememberCoroutineScope()

    val targetIcon =
        if (switch) Icons.Filled.Stop else Icons.Filled.Send
    val rotationAngle by animateFloatAsState(targetValue = if (switch) 360f else 0f, label = "")

    val color =
        animateColorAsState(
            targetValue = if (switch) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.primaryContainer,
            label = "",
            animationSpec = tween(500, 0, LinearEasing)
        )

    FloatingActionButton(
        modifier = modifier,
        elevation = FloatingActionButtonDefaults.elevation(8.dp),
        shape = CircleShape,
        containerColor = color.value,
        onClick = { onSwitchChange(!switch) }) {

        Crossfade(targetState = targetIcon, label = "") {
            Icon(
                imageVector = it,
                contentDescription = stringResource(id = if (switch) R.string.shutdown else R.string.start),
                modifier = Modifier
                    .rotate(rotationAngle)
                    .graphicsLayer {
                        rotationZ = rotationAngle
                    }
                    .size(if (switch) 42.dp else 32.dp)
            )
        }

    }
}