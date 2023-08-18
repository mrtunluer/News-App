package com.mertdev.yournews.presentation.news_by_category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mertdev.yournews.R
import com.mertdev.yournews.app.ui.theme.FontSize
import com.mertdev.yournews.app.ui.theme.MyColor
import com.mertdev.yournews.domain.model.Article
import com.mertdev.yournews.helpers.openWebPage
import com.mertdev.yournews.presentation.common.ArticleItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewsByCategoriesScreen(
    viewModel: NewsByCategoriesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isLoading = uiState.isLoading
    val listState = rememberLazyListState()
    val categories = listOf("All").plus(uiState.selectedCategories)
    val pullRefreshState =
        rememberPullRefreshState(isLoading, onRefresh = viewModel::fetchNewsBySelectedCategories)
    var currentCategory by remember { mutableStateOf("All") }

    Box(
        Modifier.pullRefresh(pullRefreshState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Header(listState = listState)

            CategoriesRow(listState = listState,
                categories = categories,
                currentCategory = currentCategory,
                onCategoryClick = { category ->
                    currentCategory = category
                    if (category == "All") viewModel.fetchNewsBySelectedCategories()
                    else viewModel.fetchNewsBySelectedCategory(currentCategory)
                })

            Spacer(modifier = Modifier.padding(10.dp))

            ArticleList(listState, uiState.articles)
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
fun ArticleList(listState: LazyListState, articles: List<Article>) {
    val context = LocalContext.current
    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
        items(articles.size) { i ->
            ArticleItem(articles[i], onClicked = { url ->
                openWebPage(context = context, url = url)
            })
        }
    }
}

@Composable
fun CategoriesRow(
    listState: LazyListState,
    categories: List<String>,
    currentCategory: String,
    onCategoryClick: (String) -> Unit
) {
    LazyRow {
        items(categories.size) { index ->
            CategoryItem(
                listState = listState,
                categoryName = categories[index],
                isSelected = categories[index] == currentCategory,
                onClick = onCategoryClick
            )
        }
    }
}

@Composable
fun CategoryItem(
    listState: LazyListState, categoryName: String, isSelected: Boolean, onClick: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    Text(
        text = categoryName, modifier = Modifier
            .background(
                if (isSelected) MyColor.Blue else Color.Transparent,
                shape = RoundedCornerShape(40.dp)
            )
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .clickable {
                if (isSelected) coroutineScope.launch(Dispatchers.Main) {
                    listState.animateScrollToItem(0)
                } else onClick(categoryName)
            }, color = if (isSelected) Color.White else Color.Gray, fontSize = FontSize.Small
    )
}

@Composable
fun Header(listState: LazyListState) {
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 20.dp)
        .clickable {
            coroutineScope.launch(Dispatchers.Main) {
                listState.animateScrollToItem(0)
            }
        }) {
        Text(
            text = stringResource(R.string.discover),
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            color = Color.Black,
            fontSize = FontSize.Large
        )
        Text(
            text = stringResource(R.string.discover_sub_title),
            color = Color.Gray,
            fontSize = FontSize.Small
        )
    }
}
