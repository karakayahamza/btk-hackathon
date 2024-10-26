package com.example.btk_hackathon.presentation.views.home_screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.btk_hackathon.BuildConfig
import com.example.btk_hackathon.data.local.model.BookAdvice
import com.example.btk_hackathon.data.local.model.Response
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.FunctionType
import com.google.ai.client.generativeai.type.Schema
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun HomeScreen() {
    // State to hold book advice and loading state
    var bookAdvice by remember { mutableStateOf<Response?>(null) }
    var isLoading by remember { mutableStateOf(false) }





    // Box to center the content
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (isLoading) {
            // Show loading indicator while fetching data
            CircularProgressIndicator()
        } else {
            // Display book advice or a button to fetch advice
            bookAdvice?.response?.let { advice ->
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Safely display the book title, providing a default if null
                    Text(
                        text = advice.kitap_adi?.uppercase() ?: "Kitap Adı Yok",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    InformationCard(title = "Yazar", content = advice.yazar)
                    InformationCard(title = "Yayın Yılı", content = advice.yayın_yılı.toString())
                    InformationCard(title = "Özet", content = advice.ozet)
                    InformationCard(
                        title = "Ana Karakterler",
                        content = advice.ana_karakterler.joinToString(", ")
                    )
                    InformationCard(title = "Ortalama Puan", content = advice.ortalama_puan.toString())

                    // Button to fetch new book advice
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            // Fetch new book advice when button is clicked
                            isLoading = true // Set loading state
                            fetchBookAdvice { newAdvice ->
                                bookAdvice = newAdvice
                                isLoading = false // Reset loading state after fetching
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = "Yeni Kitap Önerisi Al",
                            color = Color.White // Change text color to white
                        )
                    }
                }
            } ?: run {
                // If no book advice is available, show button to fetch advice
                Button(
                    onClick = {
                        // Fetch book advice when button is clicked
                        isLoading = true // Set loading state
                        fetchBookAdvice { newAdvice ->
                            bookAdvice = newAdvice
                            isLoading = false // Reset loading state after fetching
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Rastgele Kitap Önerisi Al",
                        color = Color.White // Change text color to white
                    )
                }
            }
        }
    }
}


@Composable
fun InformationCard(title: String, content: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium // Rounded corners
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// Function to fetch book advice
fun fetchBookAdvice(onResult: (Response?) -> Unit) {
    // Use a coroutine to fetch book advice in a background thread
    CoroutineScope(Dispatchers.IO).launch {
        val response = getRandomBookAdvice2() // Fetch book advice
        onResult(response) // Return the result
    }
}
suspend fun getRandomBookAdvice2(): Response {
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
            systemInstruction = content { text("Sana verilecek bilgilere göre rastgele bir kitap önerisi ver.") }
        )

        val chatHistory = listOf(
            content("user") {
                text("\"Kullanıcının ilgi alanları şunlardır: Bilim Kurgu, Kişisel Gelişim, Tarih, Romantik, Polisiye. Kullanıcının en sevdiği kitaplar: 'Dune, Kayıp Zamanın İzinde', en sevdiği yazarlar: 'Isaac Asimov, Haruki Murakami', tercih ettiği kitap uzunluğu: 'orta', okuma tarzı: 'kurgusal', daha önce okuduğu ve sevdiği kitaplardan biri: '1984'.")
            }
        )

        val chat = model.startChat(chatHistory)
        val prompt =
            "Kullanıcının ilgi alanları şunlardır: Bilim Kurgu, Kişisel Gelişim, Tarih, Romantik, Polisiye. " +
                    "Kullanıcının en sevdiği kitaplar: 'Dune, Kayıp Zamanın İzinde', " +
                    "en sevdiği yazarlar: 'Isaac Asimov, Haruki Murakami', " +
                    "tercih ettiği kitap uzunluğu: 'orta', okuma tarzı: 'kurgusal', " +
                    "daha önce okuduğu ve sevdiği kitaplardan biri: '1984'."

        val response = chat.sendMessage(prompt)
        Log.d("Test", response.text.toString())

        // Parse JSON response to BookAdvice object
        val bookAdvice = Gson().fromJson(response.text, BookAdvice::class.java)

        // Create the final response structure
        return@withContext Response(
            response = bookAdvice
        )
    }
}




suspend fun getRandomBookAdvice(): Response {
    return withContext(Dispatchers.IO) {
        // Configure the generative model for getting book advice
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.API_KEY,
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = Schema(
                    name = "bookAdvice",
                    description = "Sana verilecek bilgilere göre rastgele bir kitap önerisi ver. Kullanıcının verdiği bilgilerde geçen kitapları verme sakın!.",
                    type = FunctionType.OBJECT,
                    properties = mapOf(
                        "kitap_adi" to Schema(
                            name = "kitap_adi",
                            description = "Name of the book",
                            type = FunctionType.STRING,
                            nullable = false
                        ),
                        "yazar" to Schema(
                            name = "yazar",
                            description = "Author of the book",
                            type = FunctionType.STRING,
                            nullable = false
                        ),
                        "yayın_yili" to Schema(
                            name = "yayın_yili",
                            description = "Publication year",
                            type = FunctionType.NUMBER,
                            nullable = false
                        ),
                        "ana_karakterler" to Schema(
                            name = "ana_karakterler",
                            description = "Main characters",
                            type = FunctionType.ARRAY,
                            items = Schema(
                                name = "character",
                                description = "A character in the book",
                                type = FunctionType.STRING,
                                nullable = false
                            )
                        ),
                        "ozet" to Schema(
                            name = "ozet",
                            description = "Summary of the book",
                            type = FunctionType.STRING,
                            nullable = false
                        ),
                        "ortalama_puan" to Schema(
                            name = "ortalama_puan",
                            description = "Average rating of the book",
                            type = FunctionType.NUMBER,
                            nullable = false
                        )
                    ),
                    required = listOf("kitap_adi", "yazar", "yayın_yili", "ozet")
                )
            }
        )

        val prompt =
            "Kullanıcının ilgi alanları şunlardır: Bilim Kurgu, Kişisel Gelişim, Tarih, Romantik, Polisiye. " +
                    "Kullanıcının en sevdiği kitaplar: 'Dune, Kayıp Zamanın İzinde', " +
                    "en sevdiği yazarlar: 'Isaac Asimov, Haruki Murakami', " +
                    "tercih ettiği kitap uzunluğu: 'orta', okuma tarzı: 'kurgusal', " +
                    "daha önce okuduğu ve sevdiği kitaplardan biri: '1984'."

        // Get the response from the generative model
        val response = generativeModel.generateContent(prompt)

        Log.d("Test", response.text.toString())

        // Parse JSON response to BookAdvice object
        val bookAdvice = Gson().fromJson(response.text, BookAdvice::class.java)

        // Create the final response structure
        return@withContext Response(
            response = bookAdvice
        )
    }
}

