package com.anonymous.ziwy.Screens.CardsSection.Components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.anonymous.ziwy.Screens.CardsSection.ViewModel.CardsStore
import com.anonymous.ziwy.Screens.CardsSection.ViewModel.CardsViewModel
import com.anonymous.ziwy.Utilities.ZColors.blue
import com.anonymous.ziwy.Utilities.ZColors.lightBlueTransparent
import com.anonymous.ziwy.Utilities.ZColors.orange
import com.anonymous.ziwy.Utilities.ZColors.transparent
import com.anonymous.ziwy.Utilities.ZColors.white
import com.anonymous.ziwy.Utilities.ZConstants

@Composable
fun AddCardsPage(
    viewModel: CardsViewModel,
    state: CardsStore,
    cardsNavController: NavHostController
) {
    val newCardsList = state.cardsList?.filter { !it.isSelected.value }?.map { it.deepCopy() } ?: emptyList()

    Scaffold(
        topBar = {
            Surface(
                color = lightBlueTransparent,
                shadowElevation = 0.3.dp,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 16.dp, bottom = 16.dp)
                            .clickable {
                                cardsNavController.popBackStack()
                            },
                        tint = blue
                    )
                    Text(
                        text = "Which of these cards do you own?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding()
                            .weight(1f),
                        color = blue
                    )

                    /*Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More",
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 10.dp)
                            .clickable {
                            },
                        tint = blue
                    )*/
                }
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 15.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .offset(y = 1.dp)
                        .shadow(2.dp, RoundedCornerShape(40.dp))
                        .clip(RoundedCornerShape(40.dp))
                )
                Surface(
                    color = orange,
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(10.dp, RoundedCornerShape(40.dp))
                        .clip(RoundedCornerShape(40.dp))
                        .clickable {
                            viewModel.setSelectedCards(newCardsList.filter { it.isSelected.value }.map { it.cardID })
                            cardsNavController.popBackStack()
                        },
                ) {
                    Text(
                        text = "Save", fontSize = 24.sp,
                        modifier = Modifier.padding(
                            start = 10.dp,
                            top = 12.dp,
                            bottom = 13.dp,
                            end = 10.dp
                        ),
                        textAlign = TextAlign.Center,
                        color = white,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        containerColor = transparent
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (state.loadingScreenState?.isLoading == true && state.loadingScreenState.screen == ZConstants.FETCH_CARDS) {
                    items(8) {
                        CardItemShimmer()
                    }
                } else {
                    items(items = newCardsList) { card ->
                        CardItem(card = card)
                    }
                }
            }
        }
    }
}