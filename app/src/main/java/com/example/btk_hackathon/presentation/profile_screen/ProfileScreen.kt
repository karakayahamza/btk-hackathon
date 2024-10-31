package com.example.btk_hackathon.presentation.profile_screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.btk_hackathon.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Composable
fun ProfileScreen() {


}

@Composable
fun BookSurveyScreen(modifier: Modifier = Modifier.padding()) {
    val pagerState = rememberPagerState(pageCount = { 6 }) // Sayfa sayısını artırdık
    val genres = listOf(
        "Roman",
        "Bilim Kurgu",
        "Polisiye",
        "Tarih",
        "Felsefe",
        "Psikoloji",
        "Biyografi",
        "Kişisel Gelişim",
        "Korku/Gerilim"
    )
    val themes = listOf(
        "Macera",
        "Aşk",
        "Gizem",
        "Tarihsel Olaylar",
        "Kişisel Gelişim",
        "Psikoloji",
        "Felsefe",
        "Bilim ve Teknoloji"
    )
    val lengths = listOf(
        "Kısa hikayeler (50-150 sayfa)",
        "Orta uzunlukta kitaplar (150-300 sayfa)",
        "Uzun kitaplar (300+ sayfa)"
    )
    val readingFrequency = listOf(
        "Günde 1 kitap",
        "Haftada 1 kitap",
        "Aylık 1 kitap",
        "Yılda birkaç kitap"
    )
    val publicationTypes = listOf(
        "Basılı kitap",
        "E-kitap",
        "Sesli kitap"
    )
    val interests = listOf(
        "Seyahat",
        "Bilim",
        "Sanat",
        "Tarih",
        "Teknoloji"
    )

    var selectedGenres = remember { mutableStateListOf<String>() }
    var selectedTheme by remember { mutableStateOf("") }
    var selectedLength by remember { mutableStateOf("") }
    var selectedAuthors = remember { mutableStateListOf<String>() }
    var selectedReadingFrequency by remember { mutableStateOf("") }
    var selectedPublicationType by remember { mutableStateOf("") }
    var selectedInterests = remember { mutableStateListOf<String>() }
    var isLoading by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (result.isNotEmpty()) {

            val books = parseBooks(result)
            LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                item {
                    Text(
                        text = "Önerilen Kitaplar:",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                items(books.size) { book ->
                    BookItem(book = books[book])

                }
            }
        } else {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                when (page) {
                    0 -> GenreQuestion(genres, selectedGenres)
                    1 -> ThemeQuestion(themes, selectedTheme) { theme -> selectedTheme = theme }
                    2 -> LengthQuestion(lengths, selectedLength) { length ->
                        selectedLength = length
                    }

                    3 -> AuthorQuestion(selectedAuthors)
                    4 -> ReadingFrequencyQuestion(
                        readingFrequency,
                        selectedReadingFrequency
                    ) { frequency -> selectedReadingFrequency = frequency }

                    5 -> InterestQuestion(interests, selectedInterests) // Yeni ilgi alanları sorusu
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            val coroutineScope = rememberCoroutineScope()
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if (pagerState.currentPage > 0) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }
                    },
                    enabled = pagerState.currentPage > 0
                ) {
                    Text("Önceki")
                }

                Button(
                    onClick = {
                        if (pagerState.currentPage < 5) { // Güncel sayfa kontrolü
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            isLoading = true
                            val prompt = buildPrompt(
                                selectedTheme,
                                selectedLength,
                                selectedGenres,
                                selectedAuthors,
                                selectedReadingFrequency,
                                selectedPublicationType,
                                selectedInterests
                            )
                            fetchBookAdvice(prompt) { response ->
                                result = response
                                isLoading = false
                            }
                        }
                    }
                ) {
                    Text(if (pagerState.currentPage < 5) "Sonraki" else "Bitir")
                }
            }
        }
    }
}


@Composable
fun BookItem(book: Book2) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = book.bookname,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = book.why,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun GenreQuestion(genres: List<String>, selectedGenres: MutableList<String>) {
    Column {
        Text("1. Hangi tür kitapları okumayı seversiniz?", fontSize = 18.sp)
        genres.forEach { genre ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = selectedGenres.contains(genre),
                    onCheckedChange = { checked ->
                        if (checked) selectedGenres.add(genre) else selectedGenres.remove(genre)
                    }
                )
                Text(genre)
            }
        }
    }
}

@Composable
fun ThemeQuestion(themes: List<String>, selectedTheme: String, onThemeSelected: (String) -> Unit) {
    Column {
        Text("2. Bir kitapta en çok hangi temayı tercih edersiniz?", fontSize = 18.sp)
        themes.forEach { theme ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedTheme == theme,
                    onClick = { onThemeSelected(theme) }
                )
                Text(theme)
            }
        }
    }
}

@Composable
fun LengthQuestion(
    lengths: List<String>,
    selectedLength: String,
    onLengthSelected: (String) -> Unit
) {
    Column {
        Text("3. En çok hangi kitap uzunluğunu tercih edersiniz?", fontSize = 18.sp)
        lengths.forEach { length ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedLength == length,
                    onClick = { onLengthSelected(length) }
                )
                Text(length)
            }
        }
    }
}

@Composable
fun AuthorQuestion(selectedAuthors: MutableList<String>) {
    Column {
        Text("4. Hangi yazarları seviyorsunuz? (Virgül ile ayırarak girin)", fontSize = 18.sp)

        // Kullanıcının yazarları girmesi için bir TextField
        var text by remember { mutableStateOf("") }

        TextField(
            value = text,
            onValueChange = { newText ->
                text = newText
            },
            label = { Text("Yazarları girin") },
            modifier = Modifier.fillMaxWidth()
        )

        // Kullanıcı girdisini işleyerek listeye eklemek için bir buton
        Button(
            onClick = {
                // Girdiyi virgülle ayır ve listeye ekle
                val authorsToAdd = text.split(",").map { it.trim() }
                selectedAuthors.clear() // Önceki seçimleri temizle
                selectedAuthors.addAll(authorsToAdd.filter { it.isNotEmpty() }) // Boş olanları ekleme
                text = "" // Girdiyi temizle
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Yazarları Ekle")
        }

        // Eklenen yazarların listesini göster
        Spacer(modifier = Modifier.height(16.dp))
        Text("Eklenen Yazarlar:", fontSize = 18.sp)
        selectedAuthors.forEach { author ->
            Text("- $author")
        }
    }
}


@Composable
fun ReadingFrequencyQuestion(
    frequencies: List<String>,
    selectedFrequency: String,
    onFrequencySelected: (String) -> Unit
) {
    Column {
        Text("5. Ne sıklıkta kitap okuyorsunuz?", fontSize = 18.sp)
        frequencies.forEach { frequency ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedFrequency == frequency,
                    onClick = { onFrequencySelected(frequency) }
                )
                Text(frequency)
            }
        }
    }
}

@Composable
fun InterestQuestion(interests: List<String>, selectedInterests: MutableList<String>) {
    Column {
        Text("6. Hangi ilgi alanlarına sahipsiniz?", fontSize = 18.sp)
        interests.forEach { interest ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = selectedInterests.contains(interest),
                    onCheckedChange = { checked ->
                        if (checked) selectedInterests.add(interest) else selectedInterests.remove(
                            interest
                        )
                    }
                )
                Text(interest)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBookSurveyScreen() {
    Scaffold { padding ->
        BookSurveyScreen(modifier = Modifier.padding(padding))
    }
}

fun buildPrompt(
    theme: String,
    length: String,
    genres: List<String>,
    authors: List<String>,
    readingFrequency: String,
    publicationType: String,
    interests: List<String>
): String {
    return "Tema: $theme, Uzunluk: $length, Türler: ${genres.joinToString(", ")}, Yazarlar: ${
        authors.joinToString(
            ", "
        )
    }, Okuma Sıklığı: $readingFrequency, Yayın Türü: $publicationType, İlgi Alanları: ${
        interests.joinToString(
            ", "
        )
    }"
}

fun fetchBookAdvice(
    prompt: String,
    onResult: (String) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        val response = getRandomQuestions(prompt)
        onResult(response)
    }
}

suspend fun getRandomQuestions(userinfo: String): String {
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
            systemInstruction = content {
                text(
                    "Please give another sample books (at least 10 books) by examining the answers given by the user. Give it like this;\n" +
                            "{\n" +
                            "  \"books\": [\n" +
                            "    {\n" +
                            "      \"bookname\": \"\",\n" +
                            "      \"why\": \"\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"bookname\": \"\",\n" +
                            "      \"why\": \"\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"bookname\": \"\",\n" +
                            "      \"why\": \"\"\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}\n" +
                            "1-What kind of books do you like to read?\n" +
                            "2-Which theme do you most prefer in a book?\n" +
                            "3-Which book length do you prefer the most?\n" +
                            "4-Which authors do you like?\n" +
                            "5-How often do you read books?\n" +
                            "6-What interests do you have?"
                )
            })

        val chatHistory = emptyList<Content>()
        val chat = model.startChat(chatHistory)

        val response = chat.sendMessage(userinfo)
        Log.d("Dest", response.text.toString())
        return@withContext response.text.toString()
    }
}

@Serializable
data class BookResponse2(
    val books: List<Book2>
)

@Serializable
data class Book2(
    val bookname: String,
    val why: String
)

fun parseBooks(jsonString: String): List<Book2> {
    return try {
        val response = Json.decodeFromString<BookResponse2>(jsonString)
        response.books
    } catch (e: Exception) {
        Log.e("JSON Error", "Failed to parse JSON: ${e.message}")
        emptyList()
    }
}