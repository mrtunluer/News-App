package com.mertdev.yournews.presentation.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mertdev.yournews.R
import com.mertdev.yournews.app.ui.theme.MyColor
import com.mertdev.yournews.domain.model.Article
import com.mertdev.yournews.helpers.openWebPage
import com.mertdev.yournews.presentation.common.ArticleItem
import com.mertdev.yournews.presentation.common.CustomAsyncImage

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState()
    val isLoading = uiState.isLoading

    val pagerArticles = with(viewModel) {
        if (searchQuery.isNotBlank()) uiState.searchedArticles.take(3)
        else uiState.articles.take(3)
    }
    val columnArticles = with(viewModel) {
        if (searchQuery.isNotBlank()) uiState.searchedArticles.drop(3)
        else uiState.articles.drop(3)
    }

    val pullRefreshState = rememberPullRefreshState(
        isLoading,
        onRefresh = if (viewModel.searchQuery.isNotBlank()) viewModel::getSearchedNews else viewModel::getBreakingNews
    )

    Box(
        Modifier
            .pullRefresh(pullRefreshState)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 20.dp, horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SearchBar(viewModel)
            Pager(pagerState, pagerArticles)
            PagerIndicator(pagerState, pagerArticles.size)
            Text(
                text = viewModel.searchQuery.ifBlank { stringResource(R.string.breaking_news) },
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(vertical = 10.dp),
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )
            ArticleList(columnArticles)
        }
        PullRefreshIndicator(isLoading, pullRefreshState, Modifier.align(Alignment.TopCenter))
        when {
            uiState.errorMessage != null -> Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(10.dp),
                text = uiState.errorMessage.toString(),
                textAlign = TextAlign.Center,
                color = Color.Red
            )
        }
    }
}

@Composable
fun ArticleList(columnArticles: List<Article>) {
    val context = LocalContext.current
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(columnArticles.size) { i ->
            ArticleItem(columnArticles[i], onClicked = { url ->
                openWebPage(context = context, url = url)
            })
        }
    }
}

@Composable
fun SearchBar(
    viewModel: HomeScreenViewModel
) {
    TextField(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxWidth(),
        value = viewModel.searchQuery,
        onValueChange = { queryText ->
            viewModel.searchQuery = queryText
            viewModel.getSearchedNews()
        },
        maxLines = 1,
        placeholder = { Text(text = "Search") },
        leadingIcon = {
            Icon(Icons.Rounded.Search, contentDescription = null)
        },
        shape = RoundedCornerShape(20.dp),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Black
        )
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Pager(pagerState: PagerState, pagerArticles: List<Article>) {
    HorizontalPager(
        pageCount = pagerArticles.size,
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 10.dp)
    ) { i ->
        val alphaValue = getAlphaForPage(pagerState, i)
        val scaleValue = getScaleForPage(pagerState, i)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .graphicsLayer(scaleX = scaleValue, scaleY = scaleValue, alpha = alphaValue)
        ) {
            CustomAsyncImage(
                imageUrl = pagerArticles[i].urlToImage ?: "",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
            )

            Text(
                text = pagerArticles[i].title ?: "",
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 20.dp, end = 100.dp, bottom = 20.dp),
                color = Color.White,
                overflow = TextOverflow.Ellipsis,
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                maxLines = 2
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerIndicator(pagerState: PagerState, pageCount: Int) {
    Row(
        horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()
    ) {
        repeat(pageCount) { pageIndex ->
            val lineLength = if (pagerState.currentPage == pageIndex) 30.dp else 10.dp
            val animatedLength by animateDpAsState(targetValue = lineLength, label = "")
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .width(animatedLength)
                    .height(5.dp)
                    .background(
                        color = if (pagerState.currentPage == pageIndex) MyColor.Blue else Color.Gray,
                        shape = RoundedCornerShape(30.dp)
                    )
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun getAlphaForPage(pagerState: PagerState, pageIndex: Int): Float {
    return animateFloatAsState(
        targetValue = if (pagerState.currentPage == pageIndex) 1f else 0.5f, label = ""
    ).value
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun getScaleForPage(pagerState: PagerState, pageIndex: Int): Float {
    return animateFloatAsState(
        targetValue = if (pagerState.currentPage == pageIndex) 1f else 0.8f, label = ""
    ).value
}