package com.example.btk_hackathon.presentation.views.profile_screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.btk_hackathon.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ProfileScreen() {
    var bookTitle = "Harry Potter"
    var coverEditionKey = "Harry Potter"

    var bookName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var advice by remember { mutableStateOf("") }  // Sonuç için bir state değişkeni

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Kitap: $bookTitle",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = bookName,
            onValueChange = { bookName = it },
            label = { Text("İlgi Alanları (örn: Bilim Kurgu, Kişisel Gelişim)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                isLoading = true
                fetchBookAdvice(bookTitle) { result ->
                    isLoading = false
                    advice = result
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Öneri Al")
        }

        if (isLoading) {
            CircularProgressIndicator()
        } else {

            Log.d("Advice", advice.toString())

            Text(
                text = advice,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

fun fetchBookAdvice(
    title: String,
    onResult: (String) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        val response = getRandomQuestions(title)
        onResult(response)
    }
}

suspend fun getRandomQuestions(bookName: String): String {
    return withContext(Dispatchers.IO) {
        val model = GenerativeModel(
            "gemini-1.5-flash",
            BuildConfig.API_KEY,
            generationConfig = generationConfig {
                temperature = 1f
                topK = 64
                topP = 0.95f
                maxOutputTokens = 8192
                responseMimeType = "application/json"
            },
            systemInstruction = content { text("Sana bir kitap ismi verilcek." +
                    " Bu isim türkçe veya ingilizce olabilir. " +
                    "Bana bu kitabın uzun ve detaylı bir Türkçe özetini," +
                    " kitabın yazarını ve kısa biografisini , kitabın yayın tarihini, kitabın türünü döndür.") },
        )

        val chatHistory = emptyList<Content>()
        val chat = model.startChat(chatHistory)

        val response = chat.sendMessage(bookName)
        Log.d("Dest", response.text.toString())
        return@withContext response.text.toString()
    }
}
