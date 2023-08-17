package com.mertdev.yournews.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mertdev.yournews.R
import com.mertdev.yournews.app.ui.theme.FontSize
import com.mertdev.yournews.domain.model.Article
import com.mertdev.yournews.helpers.extension.convertToCustomDateFormat

@Composable
fun ArticleItem(article: Article, onClicked: (Article) -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(vertical = 10.dp)
        .clickable {
            onClicked(article)
        }) {
        CustomAsyncImage(
            article.urlToImage ?: "",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clip(RoundedCornerShape(20.dp))
                .size(100.dp)
        )
        Spacer(modifier = Modifier.padding(10.dp))
        ArticleContent(article)
    }
}

@Composable
private fun ArticleContent(article: Article) = with(article) {
    Column {
        Text(
            text = title ?: "",
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            fontSize = FontSize.Small,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.padding(6.dp))
        Text(
            text = publishedAt?.convertToCustomDateFormat() ?: "",
            fontSize = FontSize.Small,
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 10.dp)
        )
    }
}
