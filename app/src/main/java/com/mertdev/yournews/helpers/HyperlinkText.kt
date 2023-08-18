package com.mertdev.yournews.helpers

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun HyperlinkText(fullText: String, linkText: String, url: String) {
    val context = LocalContext.current

    val annotatedString = buildAnnotatedString {
        append(fullText)

        val startIndex = fullText.indexOf(linkText)
        val endIndex = startIndex + linkText.length

        if (startIndex >= 0 && endIndex <= fullText.length) {
            addStyle(
                style = SpanStyle(
                    textDecoration = TextDecoration.Underline, color = Color.Blue
                ), start = startIndex, end = endIndex
            )
            addStringAnnotation(
                tag = "URL", annotation = url, start = startIndex, end = endIndex
            )
        }
    }

    ClickableText(text = annotatedString, onClick = { offset ->
        annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
            .firstOrNull()?.let {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.item)))
            }
    })
}