package com.example.btk_hackathon.presentation.welcome_screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.btk_hackathon.MainActivity
import com.example.btk_hackathon.R
import com.example.btk_hackathon.presentation.Screen
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(navController: NavHostController, context: MainActivity) {
    val animations =
        listOf(R.raw.woman_reading_book_under_the_tree, R.raw.online_food_order, R.raw.lotti_anim)
    val titles = listOf(
        "Kitap dünyasına hoş geldiniz!",
        "Kitap Özetlerine Hızlı Erişim",
        "Merak Ettiğiniz Her Şeyi Sorun",
        "Yeni Kitaplar Keşfedin"
    )
    val descriptions = listOf(
        "Bu uygulama sayesinde kitap özetlerine ulaşabilir, kitaplarla ilgili sorular sorabilir ve size özel öneriler alabilirsiniz.",
        "Merak ettiğiniz kitapların özetlerine kolayca ulaşın ve zaman kaybetmeden okuyun. Kitaplar hakkında hızlı bilgi edinin.",
        "Kitaplar hakkında sorularınız mı var? Uygulamadaki Gemini AI desteği ile kitaplar, yazarlar, içerikler ve daha fazlası hakkında istediğiniz her şeyi sorun, anında cevap alın.",
        "İlgi alanınıza göre size özel kitap önerileri alın. Yeni kitaplarla tanışın ve okuma listenizi genişletin."
    )
    val pagerState = rememberPagerState(pageCount = { animations.size })

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { currentPage ->
            Column(
                Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animations[currentPage]))
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.size(200.dp)
                )
                Text(
                    text = titles[currentPage],
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = descriptions[currentPage],
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp)
                )
            }
        }

        PageIndicator(
            pageCount = animations.size,
            currentPage = pagerState.currentPage
        )

        ButtonsSection(pagerState = pagerState, navController = navController, context = context)
    }
}

@Composable
fun ButtonsSection(
    pagerState: PagerState,
    navController: NavHostController,
    context: MainActivity
) {
    val scope = rememberCoroutineScope()

    if (pagerState.currentPage < pagerState.pageCount - 1) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (pagerState.currentPage != 0) {
                Text(
                    text = "Geri",
                    modifier = Modifier
                        .clickable {
                            scope.launch {
                                val prevPage = pagerState.currentPage - 1
                                if (prevPage >= 0) {
                                    pagerState.scrollToPage(prevPage)
                                }
                            }
                        },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            }

            Text(
                text = "İleri",
                modifier = Modifier
                    .clickable {
                        scope.launch { pagerState.scrollToPage(pagerState.currentPage + 1) }
                    },
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }
    } else {
        Row(modifier = Modifier.padding(vertical = 12.dp)) {
            OutlinedButton(
                onClick = {
                    setOnboardingFinished(context)
                    navController.navigate(Screen.MainScreen.route) {
                        popUpTo(Screen.OnBoardScreen.route) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0x25E92F1E)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Hadi Başlayalım",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun PageIndicator(pageCount: Int, currentPage: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        //modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) {
            IndicatorSingleDot(isSelected = it == currentPage)
        }
    }
}

@Composable
fun IndicatorSingleDot(isSelected: Boolean) {
    val color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
    Box(
        modifier = Modifier
            .size(if (isSelected) 12.dp else 8.dp)
            .clip(CircleShape)
            .background(color)
    )
}

private fun setOnboardingFinished(context: MainActivity) {
    val sharedPreferences = context.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
    sharedPreferences.edit().putBoolean("isFinished", true).apply()
}



