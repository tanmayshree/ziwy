package com.anonymous.ziwy.Screens.LoginSection.Components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.anonymous.ziwy.R
import com.anonymous.ziwy.Screens.LoginSection.Models.LoginRequestModel
import com.anonymous.ziwy.Screens.LoginSection.ViewModel.LoginStore
import com.anonymous.ziwy.Screens.LoginSection.ViewModel.LoginViewModel
import com.anonymous.ziwy.Screens.RootComponent.NavigationItem
import com.anonymous.ziwy.Utilities.CountriesListJson
import com.anonymous.ziwy.Utilities.Utils
import com.anonymous.ziwy.Utilities.ZColors.black
import com.anonymous.ziwy.Utilities.ZColors.darkGrey
import com.anonymous.ziwy.Utilities.ZColors.grey
import com.anonymous.ziwy.Utilities.ZColors.lightBlueTransparent
import com.anonymous.ziwy.Utilities.ZColors.lightGrey
import com.anonymous.ziwy.Utilities.ZColors.orange
import com.anonymous.ziwy.Utilities.ZColors.white

@Composable
fun EnterMobileNumber(
    loginNavController: NavHostController,
    viewModel: LoginViewModel,
    state: LoginStore,
) {

    val countryList = Utils.parseJsonForCountryList(CountriesListJson).countriesList
    val isDropDownExpanded = remember { mutableStateOf(false) }

    val focusRequester = FocusRequester()
    val context = LocalContext.current

    val scrollState = rememberScrollState()

    val keyboardController = LocalSoftwareKeyboardController.current

    val uriHandler = LocalUriHandler.current

    LaunchedEffect(key1 = state.isLoading) {
        if (state.isLoading == true) {
            println("620555 - EnterMobileNumber.kt - EnterMobileNumber - isLoading - true - navigate to ApprovalPage")
            loginNavController.navigate(NavigationItem.ApprovalPage.route)
        }
    }
    Surface(
        color = lightBlueTransparent,
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    keyboardController?.hide()
                }
            }
            .fillMaxSize()
            .imePadding()
            .verticalScroll(scrollState)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 28.dp, start = 16.dp, end = 16.dp, bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painterResource(id = R.drawable.ziwy_logo),
                contentDescription = "Home",
                modifier = Modifier
                    .size(160.dp),
            )

            Text(
                text = "Never Miss a Deal!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = orange
            )

            Image(
                painterResource(id = R.drawable.people_on_login),
                contentDescription = "Home",
                modifier = Modifier
                    .size(240.dp),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Country Code",
                    color = orange,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                )

                Surface(
                    color = lightGrey,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .height(48.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box {
                            Surface(
                                color = grey,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .clickable {
                                        isDropDownExpanded.value = true
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(start = 12.dp, end = 8.dp)
                                        .height(48.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = state.countryFlag.value + "  " + state.countryCode.value,
                                        color = black,
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier
                                    )
                                    Icon(
                                        Icons.Filled.ArrowDropDown,
                                        contentDescription = "Profile",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            DropdownMenu(
                                expanded = isDropDownExpanded.value,
                                onDismissRequest = {
                                    isDropDownExpanded.value = false
                                }) {
                                countryList?.forEachIndexed { index, country ->
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                modifier = Modifier,
                                                horizontalArrangement = Arrangement.Center,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = country.unicodeFlag + " " + country.name + " (" + country.dialcode + ")",
                                                    color = black,
                                                    fontSize = 16.sp,
                                                    textAlign = TextAlign.Center,
                                                    fontWeight = FontWeight.Medium,
                                                    modifier = Modifier
                                                )
                                            }
                                        },
                                        onClick = {
                                            isDropDownExpanded.value = false
                                            countryList[index].dialcode?.let {
                                                state.countryCode.value = it
                                            }
                                            countryList[index].unicodeFlag?.let {
                                                state.countryFlag.value = it
                                            }
                                        }
                                    )
                                }
                            }
                        }
                        BasicTextField(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 12.dp),
                            value = state.phoneNumber.value,
                            onValueChange = {
                                state.phoneNumber.value = it
                            },
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            decorationBox = {
                                Box(
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    if (state.phoneNumber.value.isEmpty())
                                        Text(
                                            text = "Enter Mobile Number",
                                            color = darkGrey,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier
                                        )
                                    it()
                                }
                            }
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .offset(y = 5.dp)
                            .shadow(10.dp, RoundedCornerShape(40.dp))
                            .clip(RoundedCornerShape(40.dp))
                    )
                    Surface(
                        color = orange,
                        shape = RoundedCornerShape(40.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(5.dp, RoundedCornerShape(40.dp))
                            .clip(RoundedCornerShape(40.dp))
                            .clickable {
                                if (state.phoneNumber.value.isEmpty() || state.countryCode.value.isEmpty()) {
                                    Toast
                                        .makeText(
                                            context,
                                            "Please enter valid phone number",
                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                    return@clickable
                                }
                                viewModel.sendLoginRequest(
                                    loginRequestModel = LoginRequestModel(
                                        state.countryCode.value,
                                        state.phoneNumber.value
                                    ),
                                    context = context
                                )
                            },
                    ) {
                        Text(
                            text = "Sign In", fontSize = 24.sp,
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


                val annotatedText = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = black,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append("By creating an account, you agree to our ")
                    }
                    pushStringAnnotation(tag = "TERMS", annotation = "terms")
                    withStyle(
                        style = SpanStyle(
                            color = orange,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append("Terms of use")
                    }
                    pop()
                    withStyle(
                        style = SpanStyle(
                            color = black,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append(" and ")
                    }
                    pushStringAnnotation(tag = "POLICY", annotation = "policy")
                    withStyle(
                        style = SpanStyle(
                            color = orange,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append("Privacy policy")
                    }
                    pop()
                }

                ClickableText(
                    text = annotatedText,
                    modifier = Modifier
                        .padding(10.dp, 20.dp)
                        .fillMaxWidth(),
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    ),
                    onClick = { offset ->
                        annotatedText.getStringAnnotations(
                            tag = "TERMS",
                            start = offset,
                            end = offset
                        ).firstOrNull()?.let {
                            uriHandler.openUri("https://www.ziwy.in/#/terms")
                        }
                        annotatedText.getStringAnnotations(
                            tag = "POLICY",
                            start = offset,
                            end = offset
                        ).firstOrNull()?.let {
                            uriHandler.openUri("https://www.ziwy.in/#/privacy")
                        }
                    }
                )

            }

        }
    }
}