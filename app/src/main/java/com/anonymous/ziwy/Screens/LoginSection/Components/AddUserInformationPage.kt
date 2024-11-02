package com.anonymous.ziwy.Screens.LoginSection.Components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.anonymous.ziwy.GenericComponents.ZDialogDropdown
import com.anonymous.ziwy.GenericComponents.ZTextField
import com.anonymous.ziwy.GenericComponents.ZTextFieldWithDropDown
import com.anonymous.ziwy.MainActivity
import com.anonymous.ziwy.Screens.LoginSection.Models.AddUserInfoRequestModel
import com.anonymous.ziwy.Screens.LoginSection.ViewModel.LoginStore
import com.anonymous.ziwy.Screens.LoginSection.ViewModel.LoginViewModel
import com.anonymous.ziwy.Utilities.ZColors.lightBlueTransparent
import com.anonymous.ziwy.Utilities.ZColors.orange
import com.anonymous.ziwy.Utilities.ZColors.white

@Composable
fun AddUserInformationPage(
    navController: NavHostController,
    viewModel: LoginViewModel,
    state: LoginStore
) {

    val name = remember { mutableStateOf("") }
    val gender = remember { mutableStateOf("") }
    val ageGroup = remember { mutableStateOf("") }

    val genderList = listOf("Male", "Female", "Other")
    val ageGroupList = listOf("Under 18", "18-24", "25-34", "35-44", "45-54", "55-64", "65+")

    val isGenderDropdownVisible = remember { mutableStateOf(false) }
    val isAgeGroupDropdownVisible = remember { mutableStateOf(false) }

    val context = LocalContext.current

    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        color = lightBlueTransparent,
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures {
                keyboardController?.hide()
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            ZDialogDropdown(isGenderDropdownVisible, genderList) { item, index ->
                gender.value = item
            }
            ZDialogDropdown(isAgeGroupDropdownVisible, ageGroupList) { item, index ->
                ageGroup.value = item
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 28.dp, start = 16.dp, end = 16.dp, bottom = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                Text(
                    text = "User Information",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = orange,
                    modifier = Modifier.padding(bottom = 36.dp, top = 36.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(36.dp)
                ) {

                    ZTextField("Name", name.value) {
                        name.value = it
                    }

                    ZTextFieldWithDropDown("Gender", gender.value) {
                        isGenderDropdownVisible.value = true
                    }

                    ZTextFieldWithDropDown("Age Group", ageGroup.value) {
                        isAgeGroupDropdownVisible.value = true
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
                                    if (name.value.isEmpty() or gender.value.isEmpty() or ageGroup.value.isEmpty()) {
                                        Toast
                                            .makeText(
                                                context as MainActivity,
                                                "Please fill all the details!",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                        return@clickable
                                    }
                                    viewModel.addUserInformation(
                                        AddUserInfoRequestModel(
                                            countryCode = state.countryCode.value,
                                            mobileNumber = state.phoneNumber.value,
                                            email = null,
                                            userName = name.value,
                                            gender = gender.value,
                                            ageGroup = ageGroup.value,
                                            notificationId = null
                                        )
                                    )
                                },
                        ) {
                            Text(
                                text = "Submit", fontSize = 24.sp,
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
                }
            }
        }
    }
}