package com.example.rule

import android.content.Context
import android.widget.TextView
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import io.noties.markwon.Markwon
import io.noties.markwon.ext.tables.TablePlugin

@Composable
fun RuleScreen(navController: NavController) {
    val context = LocalContext.current
    val terms: String = context.getString(R.string.rule)

    RuleContent(terms, context)
}

@Composable
fun RuleContent(terms: String, context: Context) {
    val markwon = remember {
        Markwon.builder(context)
            .usePlugin(TablePlugin.create(context)) // 테이블 렌더링 지원 추가
            .build()
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            AndroidView(factory = { ctx ->
                TextView(ctx).apply {
                    textSize = 11f // 텍스트 크기 조절
                }
            }, update = { textView ->
                markwon.setMarkdown(textView, terms) // 마크다운 적용 (테이블 지원 포함)
            })
        }
    }
}

