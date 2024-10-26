package com.example.btk_hackathon.presentation.views.profile_screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ProfileScreen() {
    var bookName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }



    Column {

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            TextField(
                value = bookName,
                onValueChange = { bookName = it },
                label = { Text("İlgi Alanları (örn: Bilim Kurgu, Kişisel Gelişim)") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    isLoading = true


                    fetchBookAdvice(
                        bookName
                    ) {
                        isLoading = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Rastgele Kitap Önerisi Al", color = Color.White)
            }
        }
    }
}


fun fetchBookAdvice(
    booName: String,
    onResult: (String) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        val response = getRandomQuestions(
            booName
        )
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
            systemInstruction = content { text("Sana verilecek bilgilere göre rastgele 10 tane kitap önerisi ver.") },
        )

        val chatHistory = listOf(
            content("user") {
                text("Harry Potter Ateş Kadehi")
            },
            content("model") {
                text(
                    "```json\n{\"questions\": [{\"cevaplar\": [\"Cedric Diggory\", \"Viktor Krum\", \"Fleur Delacour\", \"Harry Potter\"], \"dogru_cevap\": \"Harry Potter\", \"soru\": \"Hangi büyücü Ateş Kadehi'ni kazandı?\"}, {\"cevaplar\": [\"Hogwarts\", \"Beauxbatons\", \"Durmstrang\", \"Poudlard\"], \"dogru_cevap\": \"Durmstrang\", \"soru\": \"Viktor Krum hangi büyücülük okulundan?\"}, {\"cevaplar\": [\"Albus Dumbledore\", \"Barty Crouch Jr.\", \"Alastor Moody\", \"Severus Snape\"], \"dogru_cevap\": \"Barty Crouch Jr.\", \"soru\": \"Kim Harry Potter'ı Turnuva'ya girmeye zorladı?\"}, {\"cevaplar\": [\"Cedric Diggory\", \"Viktor Krum\", \"Fleur Delacour\", \"Harry Potter\"], \"dogru_cevap\": \"Cedric Diggory\", \"soru\": \"Turnuva'da kim öldürüldü?\"}, {\"cevaplar\": [\"Lord Voldemort\", \"Peter Pettigrew\", \"Barty Crouch Jr.\", \"Bellatrix Lestrange\"], \"dogru_cevap\": \"Lord Voldemort\", \"soru\": \"Turnuva'nın arkasındaki gerçek kötü kim?\"}, {\"cevaplar\": [\"Harry Potter\", \"Hermione Granger\", \"Ron Weasley\", \"Neville Longbottom\"], \"dogru_cevap\": \"Harry Potter\", \"soru\": \"Kim Turnuva'nın son görevini tamamladı?\"}, {\"cevaplar\": [\"Ejderha\", \"Su Perisi\", \"Labirent\", \"Mezarlık\"], \"dogru_cevap\": \"Ejderha\", \"soru\": \"Turnuva'nın ilk görevi neydi?\"}, {\"cevaplar\": [\"Cedric Diggory\", \"Viktor Krum\", \"Fleur Delacour\", \"Harry Potter\"], \"dogru_cevap\": \"Fleur Delacour\", \"soru\": \"Hangi büyücü su perisi görevini tamamlayamadı?\"}, {\"cevaplar\": [\"Albus Dumbledore\", \"Minerva McGonagall\", \"Rubeus Hagrid\", \"Severus Snape\"], \"dogru_cevap\": \"Rubeus Hagrid\", \"soru\": \"Kim Turnuva'nın baş hakemiydi?\"}, {\"cevaplar\": [\"Hogwarts\", \"Beauxbatons\", \"Durmstrang\", \"Poudlard\"], \"dogru_cevap\": \"Hogwarts\", \"soru\": \"Hangi okul Turnuva'yı kazandı?\"}]}\n\n```"
                )
            },
        )

        val chat = model.startChat(chatHistory)
        val prompt = bookName

        val response = chat.sendMessage(prompt)
        Log.d("Dest", response.text.toString())
        return@withContext response.text.toString()
    }
}