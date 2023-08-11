package com.mertdev.yournews.presentation.onboarding

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.mertdev.yournews.R
import com.mertdev.yournews.app.ui.theme.MyColor
import com.mertdev.yournews.app.ui.theme.FontSize
import com.mertdev.yournews.domain.usecase.SaveSelectedCategories
import com.mertdev.yournews.helpers.Constants
import com.mertdev.yournews.helpers.extension.navigateAndClearBackStack
import com.mertdev.yournews.helpers.hyperlink_text.HyperlinkText
import com.mertdev.yournews.presentation.common.LottieAnim
import com.mertdev.yournews.presentation.common.Screen

private const val MAX_SELECTION_COUNT = 4
private const val ROW_COUNT_SIZE = 2

@Composable
fun CategorySelectionScreen(
    viewModel: CategorySelectionViewModel = hiltViewModel(), navController: NavHostController
) {
    val selectedState = viewModel.selectedState.collectAsStateWithLifecycle().value
    if (selectedState is CategorySelectionViewModel.SelectedState.AlreadySelected) {
        LaunchedEffect(key1 = selectedState) {
            navController.navigateAndClearBackStack(
                route = Screen.HomeScreen.route, popUpToRoute = Screen.CategorySelectionScreen.route
            )
        }
    } else if (selectedState is CategorySelectionViewModel.SelectedState.NotSelected) {
        CategorySelectionScreenContent(viewModel)
    }
    CollectSaveEvent(viewModel, navController)
}

@Composable
fun CategorySelectionScreenContent(viewModel: CategorySelectionViewModel) {
    val chunkedCategories = Constants.NEWS_CATEGORIES.chunked(ROW_COUNT_SIZE)
    val selectedCategories = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header()

        Spacer(
            modifier = Modifier.padding(10.dp)
        )

        Text(
            text = stringResource(R.string.select_category)
        )

        Spacer(
            modifier = Modifier.padding(10.dp)
        )

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(chunkedCategories.size) { i ->
                Row {
                    for (category in chunkedCategories[i]) {
                        CategoryItem(
                            category = category, isSelected = category in selectedCategories
                        ) { isSelected ->
                            if (isSelected) {
                                if (selectedCategories.size < MAX_SELECTION_COUNT) selectedCategories.add(
                                    category
                                )
                            } else selectedCategories.remove(category)
                        }
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.padding(10.dp)
        )

        NextButton(viewModel, selectedCategories)
    }

}

@Composable
fun Header() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        LottieAnim(
            animationFileName = R.raw.news_animation, modifier = Modifier
                .width(80.dp)
                .height(80.dp)
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.title),
                fontSize = FontSize.Large,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )
            HyperlinkText(
                fullText = stringResource(R.string.sub_title),
                linkText = stringResource(R.string.link_text_sub_title),
                url = stringResource(R.string.news_api_link)
            )
        }
    }

}

@Composable
fun CategoryItem(
    category: String, isSelected: Boolean, onSelectionChanged: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                onSelectionChanged(!isSelected)
            },
        colors = CardDefaults.cardColors(containerColor = if (isSelected) MyColor.LightBlack else Color.White),
        border = if (isSelected) null else BorderStroke(1.5.dp, MyColor.LightBlack),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = category,
            fontFamily = FontFamily(Font(R.font.montserrat_medium)),
            modifier = Modifier.padding(10.dp),
            color = if (isSelected) Color.White else Color.Black
        )
    }
}

@Composable
fun NextButton(viewModel: CategorySelectionViewModel, selectedCategories: List<String>) {
    Card(
        modifier = Modifier.clickable {
            viewModel.saveCategories(selectedCategories)
        }, colors = CardDefaults.cardColors(MyColor.Blue), shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = stringResource(R.string.next),
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            modifier = Modifier.padding(end = 35.dp, start = 35.dp, top = 10.dp, bottom = 10.dp),
            color = Color.White
        )
    }
}

@Composable
private fun CollectSaveEvent(
    viewModel: CategorySelectionViewModel, navController: NavHostController
) {
    val event = viewModel.saveEvent.collectAsStateWithLifecycle(null).value
    val errorMessage = stringResource(R.string.non_exist_category)
    val context = LocalContext.current
    event?.let { saveEvent ->
        LaunchedEffect(key1 = saveEvent) {
            when (saveEvent) {
                is SaveSelectedCategories.SaveEvent.GoHome -> navController.navigateAndClearBackStack(
                    route = Screen.HomeScreen.route,
                    popUpToRoute = Screen.CategorySelectionScreen.route
                )

                else -> Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}