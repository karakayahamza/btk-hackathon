package com.example.btk_hackathon.presentation.views.settings_screen

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
fun SettingsScreen() {
    var bookName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }



    Column {

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            TextField(
                value = bookName,
                onValueChange = { bookName = it },
                label = { Text("Özetini almak istediğin kitap; (Harry Potter Ateş Kadehi...)") },
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
                Text(text = "Kitap Özetini Al!", color = Color.White)
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
            systemInstruction = content { text("Sana bir kitap ismi verilecek. Bu kitabın özetini kullanıcıya Türkçe olarak döndür. Uzun bir özet olsun.") },
        )

        val chatHistory = listOf(
            content("user") {
                text("Harry Potter Ateş Kadehi")
            },
            content("model") {
                text(
                    "```json\n{\"summary\": \"Harry Potter ve Ateş Kadehi, J.K. Rowling'in yazdığı Harry Potter serisinin dördüncü kitabıdır. Hikaye, Hogwarts Cadılık ve Büyücülük Okulu'nun üç büyücü okulu ile birlikte düzenlediği, yüzyıllardır süren geleneksel bir turnuva olan Triwizard Turnuvası'na odaklanır. Turnuva, her okuldan bir şampiyonun seçilmesini ve ölümcül üç görevi tamamlamasını gerektirir. Harry Potter, 17 yaşına bile gelmeden, üç büyücü okulunun şampiyonu olarak seçilir ve kendisini korkunç ve tehlikeli görevlerle karşı karşıya bulur.  \\n\\nBu kitapta, Harry Potter ve arkadaşları Ron ve Hermione, büyücü dünyasının en karanlık sırlarından biriyle karşı karşıya gelir: Lord Voldemort'un geri dönüşü. Voldemort'un dönüşü, Harry Potter'ı ve arkadaşlarını tehlikeye sokar ve onların sihir yeteneklerini ve cesaretlerini en üst düzeye çıkarır. \\n\\nHarry Potter ve Ateş Kadehi, seriye hem macera hem de gerilim katarak yeni bir boyuta taşır. Turnuva, Harry Potter'ı ve arkadaşlarını büyücülük dünyasının en tehlikeli yaratıklarıyla karşı karşıya getirirken, Voldemort'un geri dönüşü ise tüm dünyanın kaderini tehlikeye atar. Kitap, Harry Potter'ın arkadaşlarıyla olan bağını ve Voldemort ile mücadele etmek için gerekli cesareti göstermesini anlatıyor. \\n\\nHarry Potter ve Ateş Kadehi,  Harry Potter'ın hayatında önemli bir dönüm noktasıdır. Kitap, Harry Potter'ı gençliğinden yetişkinliğe geçiş sürecinde gösterirken, aynı zamanda büyücü dünyasının karmaşık ve tehlikeli yönlerini ortaya koyuyor.\"}\n\n```"
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
