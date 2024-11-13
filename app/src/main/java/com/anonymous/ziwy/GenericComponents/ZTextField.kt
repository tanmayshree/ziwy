package com.anonymous.ziwy.GenericComponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anonymous.ziwy.Utilities.ZColors.blue
import com.anonymous.ziwy.Utilities.ZColors.orange
import com.anonymous.ziwy.Utilities.ZColors.white

@Composable
fun ZTextField(hintText: String, value: String, onValueChange: (String) -> Unit) {
    Surface(
        color = white,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = orange
                ),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                decorationBox = {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        if (value.isEmpty())
                            Text(
                                text = hintText,
                                color = blue,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        it()
                    }
                }
            )
        }
    }
}
