package com.mertdev.yournews.presentation.profile

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mertdev.yournews.R
import com.mertdev.yournews.app.ui.theme.MyColor
import com.mertdev.yournews.domain.usecase.SaveSelectedCategories
import com.mertdev.yournews.helpers.Constants
import com.mertdev.yournews.presentation.onboarding.CategoryItem

private const val MAX_SELECTION_COUNT = 4
private const val ROW_COUNT_SIZE = 2

@Composable
fun ProfileScreen(
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val chunkedCategories = Constants.NEWS_CATEGORIES.chunked(ROW_COUNT_SIZE)
    val selectedCategories = remember {
        mutableStateListOf<String>()
    }
    selectedCategories.addAll(uiState.selectedCategories)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Update Your Category Selection")
        Spacer(modifier = Modifier.padding(10.dp))

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

        Spacer(modifier = Modifier.padding(10.dp))
        NextButton(viewModel = viewModel, selectedCategories = selectedCategories)
        CollectSaveEvent(viewModel = viewModel)
    }
}

@Composable
fun NextButton(viewModel: ProfileScreenViewModel, selectedCategories: List<String>) {
    Card(
        modifier = Modifier.clickable {
            viewModel.saveCategories(selectedCategories)
        }, colors = CardDefaults.cardColors(MyColor.Blue), shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = "Save",
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            modifier = Modifier.padding(end = 35.dp, start = 35.dp, top = 10.dp, bottom = 10.dp),
            color = Color.White
        )
    }
}


@Composable
private fun CollectSaveEvent(
    viewModel: ProfileScreenViewModel
) {
    val event = viewModel.saveEvent.collectAsStateWithLifecycle(null).value
    val errorMessage = stringResource(R.string.non_exist_category)
    val successMessage = stringResource(R.string.success_selection)
    val context = LocalContext.current
    event?.let { saveEvent ->
        LaunchedEffect(key1 = saveEvent) {
            when (saveEvent) {
                is SaveSelectedCategories.SaveEvent.ShowErrorMessage -> Toast.makeText(
                    context, errorMessage, Toast.LENGTH_SHORT
                ).show()

                else -> Toast.makeText(
                    context, successMessage, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}