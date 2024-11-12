package com.anonymous.ziwy.Screens.RootComponent

enum class Screen {
    LOGIN_PAGE,
    ENTER_MOBILE_NUMBER,
    APPROVAL_PAGE,
    MAIN_PAGE,
    HOME_PAGE,
    UPLOAD_PAGE,
    PROFILE_PAGE,
    SPLASH_PAGE,
    ADD_USER_INFORMATION_PAGE,
    TOUR_PAGE,
    TOUR_SCREEN_1,
    TOUR_SCREEN_2,
    TOUR_SCREEN_3,
    COUPON_DETAIL_PAGE
}

sealed class NavigationItem(val route: String) {
    data object LoginPage : NavigationItem(Screen.LOGIN_PAGE.name)
    data object EnterMobileNumber : NavigationItem(Screen.ENTER_MOBILE_NUMBER.name)
    data object ApprovalPage : NavigationItem(Screen.APPROVAL_PAGE.name)
    data object MainPage : NavigationItem(Screen.MAIN_PAGE.name)
    data object HomePage : NavigationItem(Screen.HOME_PAGE.name)
    data object UploadPage : NavigationItem(Screen.UPLOAD_PAGE.name)
    data object ProfilePage : NavigationItem(Screen.PROFILE_PAGE.name)
    data object SplashPage : NavigationItem(Screen.SPLASH_PAGE.name)
    data object AddUserInformationPage : NavigationItem(Screen.ADD_USER_INFORMATION_PAGE.name)
    data object TourPage : NavigationItem(Screen.TOUR_PAGE.name)
    data object TourScreen1 : NavigationItem(Screen.TOUR_SCREEN_1.name)
    data object TourScreen2 : NavigationItem(Screen.TOUR_SCREEN_2.name)
    data object TourScreen3 : NavigationItem(Screen.TOUR_SCREEN_3.name)
    data object CouponDetailPage : NavigationItem(Screen.COUPON_DETAIL_PAGE.name)
}