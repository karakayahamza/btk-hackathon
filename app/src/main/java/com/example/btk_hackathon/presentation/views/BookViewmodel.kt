package com.example.btk_hackathon.presentation.views

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.btk_hackathon.BuildConfig
import com.example.btk_hackathon.data.local.model.Book
import com.example.btk_hackathon.data.local.model.BookResponse
import com.example.btk_hackathon.domain.repository.BookRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(private val noteRepository: BookRepository) :
    AndroidViewModel(application = Application()) {
    private val allBooks: Flow<List<Book>> = noteRepository.getBooks()


    init {
        viewModelScope.launch {
            allBooks.collect { books ->
                if (books.isNotEmpty()) {
                    Log.d("Books", "İlk kitap: ${books[0].kitap_adi}")
                }
            }
        }
    }

    fun insert(book: Book) {
        Log.d("Book", book.kitap_adi.toString())
        viewModelScope.launch {
            noteRepository.insert(book)
        }
    }

    suspend fun getRandomBookAdvice(
        interests: String,
        favoriteBooks: String,
        favoriteAuthors: String,
        readingStyle: String,
    ): List<Book> {
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
                    text("\"Kullanıcının ilgi alanları şunlardır: Bilim Kurgu, Kişisel Gelişim, Tarih, Romantik, Polisiye. Kullanıcının en sevdiği kitaplar: 'Dune, Kayıp Zamanın İzinde', en sevdiği yazarlar: 'Isaac Asimov, Haruki Murakami', tercih ettiği kitap uzunluğu: 'orta', okuma tarzı: 'kurgusal', daha önce okuduğu ve sevdiği kitaplardan biri: '1984'.")
                },
                content("model") {
                    text(
                        "```json\n{\"response\": [{\"ana_karakterler\": [\"Winston Smith\", \"Julia\", \"O'Brien\"], \"kitap_adi\": \"1984\", \"ortalama_puan\": 4.2, \"ozet\": \"Distopik bir gelecekte, Totaliter bir rejim altında yaşayan Winston Smith, aşk, özgürlük ve bireysellik arayışında olan bir adamdır. Ancak, devletin baskıcı kontrolü altında, Winston'ın hayalleri ve isyanı tehlikeli bir mücadeleye dönüşür.\", \"yayin_yili\": 1949, \"yazar\": \"George Orwell\"}, {\"ana_karakterler\": [\"Paul Atreides\", \"Lady Jessica\", \"Duke Leto Atreides\"], \"kitap_adi\": \"Dune\", \"ortalama_puan\": 4.5, \"ozet\": \"Uzak bir gelecekte, çöl gezegeni Arrakis'te geçen epik bir bilim kurgu romanı. Güçlü Atreides ailesinin gezegeni ele geçirmek için verdikleri mücadele, siyasi entrikalar, dini çatışmalar ve gizemli kum solucanları ile dolu bir maceraya dönüşür.\", \"yayin_yili\": 1965, \"yazar\": \"Frank Herbert\"}, {\"ana_karakterler\": [\"Marcel Proust\", \"Odette de Crécy\", \"Charles Swann\"], \"kitap_adi\": \"Kayıp Zamanın İzinde\", \"ortalama_puan\": 4.1, \"ozet\": \"Fransız edebiyatının en önemli eserlerinden biri olarak kabul edilen 'Kayıp Zamanın İzinde', anı, zaman, bilinç ve aşk üzerine derinlemesine bir inceleme sunuyor. Proust, kendi hayatından yola çıkarak, geçmişin anıları ve insan ilişkilerinin karmaşıklığını ustaca betimliyor.\", \"yayin_yili\": 1913, \"yazar\": \"Marcel Proust\"}, {\"ana_karakterler\": [\"Harry Seldon\", \"Gaal Dornick\", \"R. Daneel Olivaw\"], \"kitap_adi\": \"Vakıf\", \"ortalama_puan\": 4.3, \"ozet\": \"Bilim kurgu edebiyatının en önemli eserlerinden biri olan 'Vakıf', insanlığın geleceğini şekillendirecek bir imparatorluğun çöküşünü ve bir grup bilim insanının yeni bir medeniyet kurma çabalarını anlatıyor.\", \"yayin_yili\": 1951, \"yazar\": \"Isaac Asimov\"}, {\"ana_karakterler\": [\"Noriko Watanabe\", \"Kenji Watanabe\", \"Shigeki Fuji\"], \"kitap_adi\": \"Noriko'nun Aşk Hikayesi\", \"ortalama_puan\": 3.8, \"ozet\": \"Bir Japon kadının, savaş sonrası toplumunda yaşadığı aşk, kayıp ve umut dolu bir yolculuğu anlatan dokunaklı bir roman. Noriko, kendi hayatını ve sevdiklerinin kaderini şekillendiren zorlu kararlar almak zorunda kalır.\", \"yayin_yili\": 2005, \"yazar\": \"Sayaka Murata\"}, {\"ana_karakterler\": [\"John Watson\", \"Sherlock Holmes\", \"Inspector Lestrade\"], \"kitap_adi\": \"Dört Dörtlük\", \"ortalama_puan\": 4.0, \"ozet\": \"Arthur Conan Doyle'un ünlü dedektif Sherlock Holmes'un maceralarını anlatan bir polisiye romanı. Holmes, bir dizi cinayet vakasını çözmek için zekasını ve gözlem yeteneğini kullanır.\", \"yayin_yili\": 1890, \"yazar\": \"Arthur Conan Doyle\"}, {\"ana_karakterler\": [\"Jane Eyre\", \"Mr. Rochester\", \"St. John Rivers\"], \"kitap_adi\": \"Jane Eyre\", \"ortalama_puan\": 4.2, \"ozet\": \"19. yüzyıl İngiltere'sinde geçen bir aşk ve toplumsal eleştiri romanı. Yetim bir kız olan Jane Eyre, hayatta kalmak ve kendi kimliğini bulmak için mücadele ederken, aşk, kaybetme ve özgürlük kavramlarıyla yüzleşir.\", \"yayin_yili\": 1847, \"yazar\": \"Charlotte Brontë\"}, {\"ana_karakterler\": [\"Holden Caulfield\", \"Phoebe Caulfield\", \"Sally Hayes\"], \"kitap_adi\": \"Çavdar Tarlasında Çocuk\", \"ortalama_puan\": 3.9, \"ozet\": \"Genç bir erkeğin, okuldan atıldıktan sonra New York sokaklarında dolaşarak, hayatın sahtekarlığı ve saflığın arayışı üzerine yaptığı yolculuğu anlatan bir roman. Holden, yetişkin dünyasına karşı isyanını ve çocukluğa özlemini ifade eder.\", \"yayin_yili\": 1951, \"yazar\": \"J.D. Salinger\"}, {\"ana_karakterler\": [\"Atticus Finch\", \"Scout Finch\", \"Jem Finch\"], \"kitap_adi\": \"Bülbül Ötmesin\", \"ortalama_puan\": 4.5, \"ozet\": \"Amerikan Güneyinde geçen bir çocukluk ve toplumsal adalet romanı. Küçük Scout Finch, babası Atticus'un, ırkçılığa ve ayrımcılığa karşı mücadelesini izler.\", \"yayin_yili\": 1960, \"yazar\": \"Harper Lee\"}, {\"ana_karakterler\": [\"Gregor Samsa\", \"Grete Samsa\", \"Mr. Samsa\"], \"kitap_adi\": \"Dönüşüm\", \"ortalama_puan\": 4.1, \"ozet\": \"Franz Kafka'nın ünlü eseri, bir sabah böceğe dönüşen Gregor Samsa'nın hikayesini anlatıyor. Bu tuhaf dönüşüm, Gregor'un ailesiyle ve toplumla olan ilişkilerini sarsıyor ve ailenin hayatta kalma mücadelesini gözler önüne seriyor.\", \"yayin_yili\": 1915, \"yazar\": \"Franz Kafka\"}]}\n\n```"
                    )
                },
                content("user") {
                    text("Ama sana kullanıcının verdiği kitapları döndürmemeni söyledim.")
                },
                content("model") {
                    text(
                        "```json\n{\"response\": [{\"ana_karakterler\": [\"Arthur Dent\", \"Ford Prefect\", \"Zaphod Beeblebrox\"], \"kitap_adi\": \"Otostopçunun Galaksi Rehberi\", \"ortalama_puan\": 4.2, \"ozet\": \"Dünya'nın yıkımından kurtulan Arthur Dent, galaksideki maceralarına otostop çekerek başlar. Ford Prefect, galaktik rehber kitabını tamamlamak için Dünya'ya gelen bir uzaylıdır ve Arthur'un bu macerada rehberi olur. Komik ve absürt bir şekilde insanlığın yerini, evrenin sırrını ve yaşamın anlamını sorgular.\", \"yayin_yili\": 1979, \"yazar\": \"Douglas Adams\"}, {\"ana_karakterler\": [\"Katniss Everdeen\", \"Peeta Mellark\", \"Haymitch Abernathy\"], \"kitap_adi\": \"Açlık Oyunları\", \"ortalama_puan\": 4.3, \"ozet\": \"Distopik bir gelecekte, Açlık Oyunları adlı ölümcül bir yarışmada hayatta kalmak için savaşan genç Katniss Everdeen'in hikayesini anlatır. Katniss, sevdiklerini korumak ve özgürlük için mücadele etmek zorunda kalır.\", \"yayin_yili\": 2008, \"yazar\": \"Suzanne Collins\"}, {\"ana_karakterler\": [\"Harry Potter\", \"Hermione Granger\", \"Ron Weasley\"], \"kitap_adi\": \"Harry Potter ve Felsefe Taşı\", \"ortalama_puan\": 4.5, \"ozet\": \"Yetim bir çocuk olan Harry Potter, 11. yaş gününde büyücülük dünyasıyla tanışır ve Hogwarts Cadılık ve Büyücülük Okulu'na kabul edilir. Burada arkadaşlık, macera ve kötülüğe karşı mücadeleyle dolu bir hayat başlar.\", \"yayin_yili\": 1997, \"yazar\": \"J.K. Rowling\"}, {\"ana_karakterler\": [\"Jane Eyre\", \"Mr. Rochester\", \"St. John Rivers\"], \"kitap_adi\": \"Jane Eyre\", \"ortalama_puan\": 4.2, \"ozet\": \"19. yüzyıl İngiltere'sinde geçen bir aşk ve toplumsal eleştiri romanı. Yetim bir kız olan Jane Eyre, hayatta kalmak ve kendi kimliğini bulmak için mücadele ederken, aşk, kaybetme ve özgürlük kavramlarıyla yüzleşir.\", \"yayin_yili\": 1847, \"yazar\": \"Charlotte Brontë\"}, {\"ana_karakterler\": [\"Holden Caulfield\", \"Phoebe Caulfield\", \"Sally Hayes\"], \"kitap_adi\": \"Çavdar Tarlasında Çocuk\", \"ortalama_puan\": 3.9, \"ozet\": \"Genç bir erkeğin, okuldan atıldıktan sonra New York sokaklarında dolaşarak, hayatın sahtekarlığı ve saflığın arayışı üzerine yaptığı yolculuğu anlatan bir roman. Holden, yetişkin dünyasına karşı isyanını ve çocukluğa özlemini ifade eder.\", \"yayin_yili\": 1951, \"yazar\": \"J.D. Salinger\"}, {\"ana_karakterler\": [\"Atticus Finch\", \"Scout Finch\", \"Jem Finch\"], \"kitap_adi\": \"Bülbül Ötmesin\", \"ortalama_puan\": 4.5, \"ozet\": \"Amerikan Güneyinde geçen bir çocukluk ve toplumsal adalet romanı. Küçük Scout Finch, babası Atticus'un, ırkçılığa ve ayrımcılığa karşı mücadelesini izler.\", \"yayin_yili\": 1960, \"yazar\": \"Harper Lee\"}, {\"ana_karakterler\": [\"Gregor Samsa\", \"Grete Samsa\", \"Mr. Samsa\"], \"kitap_adi\": \"Dönüşüm\", \"ortalama_puan\": 4.1, \"ozet\": \"Franz Kafka'nın ünlü eseri, bir sabah böceğe dönüşen Gregor Samsa'nın hikayesini anlatıyor. Bu tuhaf dönüşüm, Gregor'un ailesiyle ve toplumla olan ilişkilerini sarsıyor ve ailenin hayatta kalma mücadelesini gözler önüne seriyor.\", \"yayin_yili\": 1915, \"yazar\": \"Franz Kafka\"}, {\"ana_karakterler\": [\"Sansa Stark\", \"Arya Stark\", \"Jon Snow\"], \"kitap_adi\": \"Taht Oyunları\", \"ortalama_puan\": 4.4, \"ozet\": \"Ortaçağ dünyasında geçen fantastik bir epik roman serisi. Güçlü ailelerin, büyücülerin ve canavarların yaşadığı Westeros'ta geçen bir taht mücadelesi ve hayatta kalma mücadelesini konu alıyor.\", \"yayin_yili\": 1996, \"yazar\": \"George R. R. Martin\"}, {\"ana_karakterler\": [\"Jay Gatsby\", \"Nick Carraway\", \"Daisy Buchanan\"], \"kitap_adi\": \"Büyük Gatsby\", \"ortalama_puan\": 4.0, \"ozet\": \"1920'lerin Amerika'sında, zengin ve gizemli Jay Gatsby'nin aşk, kayıp ve hayal kırıklığı dolu hikayesini anlatıyor. Gatsby, geçmiş aşkının peşinden giderken, Amerikan Rüyası'nın gerçekleri ve aldatıcı doğasıyla yüzleşir.\", \"yayin_yili\": 1925, \"yazar\": \"F. Scott Fitzgerald\"}, {\"ana_karakterler\": [\"Bilbo Baggins\", \"Gandalf\", \"Frodo Baggins\"], \"kitap_adi\": \"Hobbit\", \"ortalama_puan\": 4.3, \"ozet\": \"Orta Dünya'da geçen bir fantastik roman. Hobbit Bilbo Baggins, beklenmedik bir şekilde hazine avına çıkar ve yolculuğu sırasında ejderhalar, orklar ve cücelerle karşılaşır. Bu macera, Bilbo'nun cesaretini keşfetmesini ve dünyayı kurtarmak için önemli bir rol oynamasını sağlar.\", \"yayin_yili\": 1937, \"yazar\": \"J.R.R. Tolkien\"}]}\n\n```"
                    )
                },
            )
            val chat = model.startChat(chatHistory)
            val prompt =
                "Kullanıcının ilgi alanları şunlardır: ${interests} " +
                        "Kullanıcının en sevdiği kitaplar: '${favoriteBooks}', " +
                        "en sevdiği yazarlar: '${favoriteAuthors}', " +
                        "okuma tarzı: '$readingStyle'"

            val response = chat.sendMessage(prompt)
            Log.d("Test", response.text.toString())

            val bookResponse = Gson().fromJson(response.text, BookResponse::class.java)

            return@withContext bookResponse.response
        }
    }
}