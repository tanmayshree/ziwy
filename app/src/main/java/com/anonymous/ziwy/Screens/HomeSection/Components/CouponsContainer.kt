package com.anonymous.ziwy.Screens.HomeSection.Components

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.anonymous.ziwy.GenericModels.LoadingScreenState
import com.anonymous.ziwy.Screens.HomeSection.Models.Coupon
import com.anonymous.ziwy.Screens.HomeSection.ViewModel.MainStore
import com.anonymous.ziwy.Screens.HomeSection.ViewModel.MainViewModel
import com.anonymous.ziwy.Screens.RootComponent.Components.SplashPage
import com.anonymous.ziwy.Utilities.Utils.transitionColor
import com.anonymous.ziwy.Utilities.ZColors.grey
import com.anonymous.ziwy.Utilities.ZConstants

@Composable
fun CouponsContainer(
    viewModel: MainViewModel,
    state: MainStore,
    filteredList: List<Coupon>,
    onCouponClick: (String) -> Unit,
) {
    LazyVerticalGrid(
        contentPadding = PaddingValues(horizontal = 16.dp),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(bottom = if (state.isEmailSynced) 0.dp else 46.dp)
    ) {

        item(span = { GridItemSpan(2) }) {
            AnimatedVisibility(
                visible = state.carouselImagesList.isNotEmpty(),
//                    modifier = Modifier.background(grey),
                enter = fadeIn() + slideInVertically { -it / 2 },
                exit = fadeOut() + slideOutVertically { -it / 2 }
            ) {
                CarouselRow(state)
            }
        }
        if (state.imageUri != null && state.imageUri != Uri.EMPTY) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(transitionColor(), RoundedCornerShape(10.dp))
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = state.imageUri),
                        contentDescription = null,
                        modifier = Modifier
                            .alpha(.4f)
                            .fillMaxWidth()
//                            .width(150.dp)
                            .height(180.dp)
                            .padding(1.dp),
                        contentScale = ContentScale.FillBounds
                    )
                    //add shimmer

                }
            }
        }

        if (state.loadingScreenState == LoadingScreenState(true, ZConstants.FETCH_COUPONS)) {
            items(8) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .width(150.dp)
                        .height(180.dp)
                        .background(transitionColor(), RoundedCornerShape(10.dp))
                ) {

                }
            }
        } else {
            items(filteredList) { item ->
                CouponItemCardFront(item, onCouponClick)

                /*val isCardFlipped = remember { mutableStateOf(false) }
                AnimatedContent(
                    targetState = isCardFlipped.value,
                    transitionSpec = {
                        if (targetState) {
                            // Flip to back: front -> back
                            (fadeIn() + slideInHorizontally { it / 2 }).togetherWith(fadeOut() + slideOutHorizontally { -it / 2 })
                        } else {
                            // Flip to front: back -> front
                            (fadeIn() + slideInHorizontally { -it / 2 }).togetherWith(fadeOut() + slideOutHorizontally { it / 2 })
                        }
                    }, label = ""
                ) { flipped ->
                    if (flipped) {
                        CouponItemCardBack(item, isCardFlipped, context, viewModel, state)
                    } else {
                        CouponItemCardFront(item, isCardFlipped)
                    }
                }*/
            }
        }
    }
    if (state.imageUri != null && state.imageUri != Uri.EMPTY) {

    } else if (state.loadingScreenState == LoadingScreenState(true, ZConstants.FETCH_COUPONS)) {

    } else if (filteredList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Share your first coupon screenshot and show the world your savings game!",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                color = grey,
                modifier = Modifier
                    .padding(20.dp, 10.dp)
            )
        }
    }
}