package com.example.btk_hackathon.presentation.screens.welcome_screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
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
import com.example.btk_hackathon.presentation.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(navController: NavHostController, context: MainActivity) {
    val animations =
        listOf(R.raw.woman_reading_book_under_the_tree, R.raw.online_food_order, R.raw.lotti_anim)
    val titles = listOf(
        stringResource(R.string.welcome_to_the_world_of_books),
        stringResource(R.string.quick_access_to_book_abstracts),
        stringResource(R.string.ask_anything_you_ve_ever_wondered),
        stringResource(R.string.discover_new_books),
    )
    val descriptions = listOf(
        stringResource(R.string.with_this_app_you_can_access_book_summaries_ask_questions_about_books_and_get_personalized_recommendations),
        stringResource(R.string.easily_access_the_summaries_of_the_books_you_are_curious_about_and_read_them_without_wasting_time_get_quick_information_about_books),
        stringResource(R.string.do_you_have_questions_about_books_ask_anything_you_want_about_books_authors_content_and_more_and_get_instant_answers_with_gemini_ai_support_in_the_app),
        stringResource(R.string.get_personalized_book_recommendations_based_on_your_interests_meet_new_books_and_expand_your_reading_list)
    )
    val pagerState = rememberPagerState(pageCount = { animations.size })

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        val animationSize = screenHeight * 0.4f

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
                    modifier = Modifier.size(animationSize)
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
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (pagerState.currentPage != 0) {
                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                val prevPage = pagerState.currentPage - 1
                                if (prevPage >= 0) {
                                    pagerState.scrollToPage(prevPage)
                                }
                            }
                        }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Back",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                } else {
                    Box(modifier = Modifier.width(0.dp))
                }

                OutlinedButton(
                    onClick = {
                        scope.launch {
                            pagerState.scrollToPage(pagerState.currentPage + 1)
                        }
                    }
                ) {
                    Text(
                        text = stringResource(R.string.next),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = stringResource(R.string.next))
                }
            }


        }
    } else {
        Row(modifier = Modifier.padding(vertical = 16.dp)) {
            OutlinedButton(
                onClick = {
                    setOnboardingFinished(context)
                    navController.navigate(Screen.MainScreen.route) {
                        popUpTo(Screen.OnBoardScreen.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.let_s_get_started),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
fun PageIndicator(pageCount: Int, currentPage: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) {
            IndicatorSingleDot(isSelected = it == currentPage)
        }
    }
}

@Composable
fun IndicatorSingleDot(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(if (isSelected) 12.dp else 8.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondary)
    )
}

private fun setOnboardingFinished(context: MainActivity) {
    val sharedPreferences = context.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
    sharedPreferences.edit().putBoolean("isFinished", true).apply()
}



