package com.mertdev.yournews.presentation.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mertdev.yournews.domain.model.Article


@Composable
fun DetailScreen(article: Article) {
    Text(
        text = article.title.toString(), modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    )
}
