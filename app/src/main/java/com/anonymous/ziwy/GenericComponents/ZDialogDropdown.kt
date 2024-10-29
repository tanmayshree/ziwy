package com.anonymous.ziwy.GenericComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.anonymous.ziwy.Utilities.ZColors.orange
import com.anonymous.ziwy.Utilities.ZColors.white

@Composable
fun ZDialogDropdown(
    isDialogDropdownVisible: MutableState<Boolean>,
    itemList: List<String>,
    onItemSelected: (item: String, index: Int) -> Unit
) {
    if (isDialogDropdownVisible.value) {
        Dialog(
            onDismissRequest = {
                isDialogDropdownVisible.value = false
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
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
                    itemList.forEachIndexed { index, item ->
                        Text(
                            text = item,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clickable {
                                    onItemSelected(item, index)
                                    isDialogDropdownVisible.value = false
                                }
                        )
                        if (index != itemList.size - 1)
                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp)
                            )
                    }

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
                                isDialogDropdownVisible.value = false
                            },
                    ) {
                        Text(
                            text = "Close", fontSize = 16.sp,
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