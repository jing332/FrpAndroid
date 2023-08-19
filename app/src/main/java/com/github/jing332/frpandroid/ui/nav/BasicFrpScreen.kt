package com.github.jing332.frpandroid.ui.nav

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.jing332.frpandroid.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BasicFrpScreen(
    modifier: Modifier,
    configScreen: @Composable () -> Unit,
    logScreen: @Composable () -> Unit,
    pageIndex: Int,
    onPageIndexChanged: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()

    Column(modifier) {
        val tabs = remember { listOf(R.string.config, R.string.log) }
        val pagerState = rememberPagerState(pageIndex) { tabs.size }

        LaunchedEffect(pageIndex) {
            pagerState.scrollToPage(pageIndex)
        }

        LaunchedEffect(pagerState.currentPage) {
            onPageIndexChanged(pagerState.currentPage)
        }

        ElevatedCard {
            TabRow(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions ->
                    TabIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage])
                    )
                },
                divider = {
                    HorizontalDivider()
                }
            ) {
                tabs.forEachIndexed { index, strId ->
                    val selected = index == pagerState.currentPage
                    Tab(
                        text = {
                            Text(
                                stringResource(id = strId),
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selected = selected,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.tertiary
                    )
                }
            }

            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) {
                when (it) {
                    0 -> configScreen()
                    1 -> logScreen()
                }
            }
        }
    }
}


@Composable
fun TabIndicator(color: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    Box(
        modifier
            .height(4.dp)
            .clip(RoundedCornerShape(8.dp))
            .padding(horizontal = 28.dp)
            .background(
                color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp)
            )
    )
}