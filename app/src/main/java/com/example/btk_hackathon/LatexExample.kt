package com.example.btk_hackathon

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

// Composable function to display LaTeX expressions
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ComplexLatexExample(latexExpression: String) {
    val latex = """
        <html>
            <head>
                <script type="text/javascript" async
                src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.7/MathJax.js?config=TeX-AMS_HTML">
                </script>
            </head>
            <body>
                <p>
                    \[$latexExpression\]
                </p>
            </body>
        </html>"""

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT // Height can be adjusted here
                )
            }
        },
        update = { webView ->
            webView.loadData(latex, "text/html", "UTF-8")
        },
        modifier = Modifier
            .fillMaxWidth() // Fill width of the parent
            .height(100.dp)
    )
}