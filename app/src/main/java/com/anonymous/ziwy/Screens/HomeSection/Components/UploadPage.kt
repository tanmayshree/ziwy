package com.anonymous.ziwy.Screens.HomeSection.Components

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.anonymous.ziwy.MainActivity
import com.anonymous.ziwy.Screens.HomeSection.Models.ExtractCouponImageRequestModel
import com.anonymous.ziwy.Screens.HomeSection.ViewModel.MainStore
import com.anonymous.ziwy.Screens.HomeSection.ViewModel.MainViewModel
import com.anonymous.ziwy.Utilities.Utils.fileUriToBase64
import com.anonymous.ziwy.Utilities.ZColors.orange
import com.anonymous.ziwy.Utilities.ZColors.transparent
import com.anonymous.ziwy.Utilities.ZColors.white
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun UploadPage(
    navController: NavHostController,
    viewModel: MainViewModel,
    state: MainStore
) {
    val result = remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        result.value = it
        println("620555 Uri: $it")
        if (it != null && it != Uri.EMPTY) {
            CoroutineScope(Dispatchers.IO).launch {
                println("620555 Base64 Image: $it")
                viewModel.extractCouponImage(
                    context = context,
                    imageUri = it,
                    payload = ExtractCouponImageRequestModel(
                        fileUriToBase64(
                            uri = it,
                            resolver = context.contentResolver
                        )
                    )
                )
            }
            navController.popBackStack()
        } else {
            Toast.makeText(context as MainActivity, "No image selected", Toast.LENGTH_LONG).show()
        }

    }


    Scaffold(
        topBar = { WelcomeHeader(state) },
        containerColor = transparent
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .offset(y = 5.dp)
                            .shadow(5.dp, RoundedCornerShape(40.dp))
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
                                launcher.launch(
                                    PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                    ) {
                        Text(
                            text = "Upload Now.", fontSize = 24.sp,
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

                Text(
                    text = "Start uploading your coupon images here.",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(10.dp, 25.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )
            }

        }
    }

}