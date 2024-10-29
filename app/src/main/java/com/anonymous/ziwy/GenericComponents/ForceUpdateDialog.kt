package com.anonymous.ziwy.GenericComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.anonymous.ziwy.Utilities.ZColors.orange
import com.anonymous.ziwy.Utilities.ZColors.white

@Composable
fun ForceUpdateDialog(
    isDialogVisible: MutableState<Boolean>,
) {

    val uriHandler = LocalUriHandler.current

    if (isDialogVisible.value) {
        Dialog(
            onDismissRequest = {
//                isDialogVisible.value = false
            },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Surface(
                color = white,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier
                        .padding(10.dp, 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Woah! We have got an update for you. Please update the app to continue using it.",
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                    //close button
                    Surface(
                        color = orange,
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth()
                            .shadow(2.dp, RoundedCornerShape(5.dp))
                            .clip(RoundedCornerShape(5.dp))
                            .clickable {
                                uriHandler.openUri("https://play.google.com/store/apps/details?id=com.anonymous.ziwy")
                            },
                    ) {
                        Text(
                            text = "Go to PlayStore", fontSize = 16.sp,
                            modifier = Modifier.padding(
                                start = 10.dp,
                                top = 12.dp,
                                bottom = 13.dp,
                                end = 10.dp
                            ),
                            textAlign = TextAlign.Center,
                            color = white,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}