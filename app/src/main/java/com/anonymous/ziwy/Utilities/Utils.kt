package com.anonymous.ziwy.Utilities

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Parcelable
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.anonymous.ziwy.GenericModels.AppUpdateInfoResponseModel
import com.anonymous.ziwy.GenericModels.CountriesListModel
import com.anonymous.ziwy.GenericModels.PreferencesUserData
import com.anonymous.ziwy.MainActivity
import com.anonymous.ziwy.Utilities.ZColors.grey
import com.anonymous.ziwy.Utilities.ZColors.lightGrey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.util.Locale

object Utils {

    @Composable
    fun PickImage() {
        val result = remember { mutableStateOf<Uri?>(null) }
        val launcher =
            rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
                result.value = it
            }
        launcher.launch(
            PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    /*suspend fun handleImageProcessing(uri: String): String? {
        return try {
            val file = File(uri)
            val fileInputStream =
                withContext(Dispatchers.IO) {
                    FileInputStream(file)
                }
            val bytes = fileInputStream.readBytes()
            withContext(Dispatchers.IO) {
                fileInputStream.close()
            }

            val base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)
            "data:image/jpeg;base64,$base64Image"
        } catch (error: Exception) {
            error.printStackTrace()
            null
        }
    }*/

    /*fun openUrlInBrowser(websiteUrl: String, context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
        if (intent.resolveActivity((context as MainActivity).packageManager) != null) {
            (context as MainActivity).startActivity(intent)
        } else {
            Toast.makeText(
                (context as MainActivity),
                "No application can handle this request",
                Toast.LENGTH_LONG
            )
                .show()
        }
    }*/

    fun handleSharingIntents(intent: Intent): Uri {
        when (intent.action) {
            Intent.ACTION_SEND -> {
                if (intent.type?.startsWith("image/") == true) {
                    (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
//                        val singleImage = findViewById<ImageView>(R.id.singleImage)
//                        singleImage.setImageURI(it)
//                        viewModel.setImageUris(listOf(it))
                        println("620555 Single image uri: $it")
                        return it
                    }
                }
            }

            Intent.ACTION_SEND_MULTIPLE -> {
                if (intent.type?.startsWith("image/") == true) {
                    intent.getParcelableArrayListExtra<Parcelable>(Intent.EXTRA_STREAM)?.let {
                        val imageUris = it.map { uri -> uri as Uri }

//                        val gridView = findViewById<GridView>(R.id.gridView)
//                        val imageAdapter = ImageAdapter(this, imageUris)
//                        gridView.adapter = imageAdapter
                    }
                }
            }
        }

        return Uri.EMPTY
    }

    fun handleGoogleSignInIntent(intent: Intent): Boolean {
        println("620555 - Utils - handleGoogleSignInIntent - ${intent.data}")
        val ans = intent.data?.let {
            it.path?.startsWith("/oauth2callback") == true
        } ?: false

        println("620555 - Utils - handleGoogleSignInIntent Answer - $ans")
        return ans
    }

    suspend fun getUserDetailsFromPreferences(context: Context): PreferencesUserData {

        val userName = withContext(Dispatchers.IO) {
            (context as MainActivity).readFromPreferences("username")
        }

        val countryCode = withContext(Dispatchers.IO) {
            (context as MainActivity).readFromPreferences("countryCode")
        }

        val phoneNumber = withContext(Dispatchers.IO) {
            (context as MainActivity).readFromPreferences("phoneNumber")
        }

        val joiningDate = withContext(Dispatchers.IO) {
            (context as MainActivity).readFromPreferences("joiningDate")
        }

        println("620555 - Preferences - userName - $userName")
        println("620555 - Preferences - countryCode - $countryCode")
        println("620555 - Preferences - phoneNumber - $phoneNumber")
        println("620555 - Preferences - joiningDate - $joiningDate")

        return PreferencesUserData(
            username = userName,
            countryCode = countryCode,
            phoneNumber = phoneNumber,
            joiningDate = joiningDate
        )

    }

    /*fun parseApiResponseForUserDetails(jsonString: String): ApiResponseForUserData {
        val jsonElement = Json.parseToJsonElement(jsonString).jsonObject

        // Check if the error key "message" exists in the JSON
        return if (jsonElement.containsKey("message")) {
            Json.decodeFromString<ErrorResponseForUserData>(jsonString)
        } else {
            Json.decodeFromString<SuccessResponseForUserData>(jsonString)
        }
    }*/

    /*fun parseOpenAiResponseForCouponData(jsonString: String): OpenAiImageDataModel {
//        val jsonElement = Json.parseToJsonElement(jsonString).jsonObject
        return Json.decodeFromString<OpenAiImageDataModel>(jsonString)
    }*/

    fun parseJsonForCountryList(jsonString: String): CountriesListModel {
//        val jsonElement = Json.parseToJsonElement(jsonString).jsonObject
        return Json.decodeFromString<CountriesListModel>(jsonString)
    }

    /**
     * File uri to base64.
     *
     * @param uri
     * the uri
     * @param resolver
     * the resolver
     * @return the string
     */
    fun fileUriToBase64(uri: Uri, resolver: ContentResolver): String {
        var encodedBase64 = ""
        try {
            val bytes = compressImage(uri, resolver)
            encodedBase64 = Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        println("620555 Base64 Image: $encodedBase64")
        return encodedBase64
    }

    /**
     * Read bytes.
     *
     * @param uri
     * the uri
     * @param resolver
     * the resolver
     * @return the byte[]
     * @throws IOException
     * Signals that an I/O exception has occurred.
     */
    @Throws(IOException::class)
    private fun readBytes(uri: Uri, resolver: ContentResolver): ByteArray {
        // this dynamically extends to take the bytes you read
        val inputStream = resolver.openInputStream(uri)
        val byteBuffer = ByteArrayOutputStream()

        // this is storage overwritten on each iteration with bytes
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)

        // we need to know how may bytes were read to write them to the
        // byteBuffer
        var len = 0
        while ((inputStream!!.read(buffer).also { len = it }) != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        inputStream.close()
        // and then we can return your byte array.
        return byteBuffer.toByteArray()
    }

    @Throws(IOException::class)
    private fun compressImage(
        uri: Uri,
        resolver: ContentResolver,
        maxWidth: Int = 800,
        maxHeight: Int = 800,
        quality: Int = 50
    ): ByteArray {
        val inputStream =
            resolver.openInputStream(uri) ?: throw IOException("Failed to open input stream")
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()

        val scaledBitmap = Bitmap.createScaledBitmap(
            originalBitmap,
            maxWidth.coerceAtMost(originalBitmap.width),
            maxHeight.coerceAtMost(originalBitmap.height),
            true
        )

        val byteArrayOutputStream = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    fun checkDateDifference(targetDateString: String): Long? {
        // Define the expected date format
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        // Check if the input string is in the correct format
        val targetDate = try {
            LocalDate.parse(targetDateString, formatter)
        } catch (e: DateTimeParseException) {
            println("Invalid date format. Please use yyyy-MM-dd.")
            return null
        }

        // Get the current date
        val currentDate = LocalDate.now()

        // Calculate the difference in days
        return ChronoUnit.DAYS.between(currentDate, targetDate)
    }

    fun formatDateTime(input: String): String? {
        return try {
            val zonedDateTime = ZonedDateTime.parse(input)
            val formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy", Locale.ENGLISH)
            zonedDateTime.format(formatter)
        } catch (e: DateTimeParseException) {
            println("Invalid date format")
            null
        }
    }

    fun isForceUpdateDialogVisible(
        appUpdateInfoResponseModel: AppUpdateInfoResponseModel,
        context: Context
    ): Boolean {
        val myAppVersion = getAppVersion(context)?.first
        val playStoreVersion = appUpdateInfoResponseModel.currentAppVersion

        return if (myAppVersion != null && playStoreVersion != null) {
            val myAppVersionCode = calculateVersionCode(myAppVersion)
            val playStoreVersionCode = calculateVersionCode(playStoreVersion)
            println("620555 - myAppVersionCode: $myAppVersionCode")
            println("620555 - playStoreVersionCode: $playStoreVersionCode")

            if (myAppVersionCode != null && playStoreVersionCode != null) {
                if (myAppVersionCode < playStoreVersionCode)
                    appUpdateInfoResponseModel.isUpdateRequired == true
                else false
            } else {
                false
            }
        } else {
            false
        }
    }

    fun calculateVersionCode(versionName: String): Int? {
        // Split the version name by '.' and parse each part
        val versionParts = versionName.split(".")

        // Ensure there are exactly 3 parts (major, minor, patch)
        if (versionParts.size != 3) return null

        return try {
            // Parse major, minor, and patch as integers
            val major = versionParts[0].toIntOrNull() ?: return null
            val minor = versionParts[1].toIntOrNull() ?: return null
            val patch = versionParts[2].toIntOrNull() ?: return null

            // Calculate version code
            (major * 10000) + (minor * 100) + patch
        } catch (e: NumberFormatException) {
            null // Return null if parsing fails
        }
    }


    private fun getAppVersion(context: Context): Pair<String, Int>? {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            val versionName = packageInfo.versionName
            val versionCode =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) packageInfo.longVersionCode.toInt() else
                    packageInfo.versionCode // Use `.versionCode` for API levels below 28
            Pair(versionName, versionCode)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    @Composable
    fun transitionColor(targetValue: Color = grey): Color {
        val infiniteTransition = rememberInfiniteTransition(label = "")
        val color by infiniteTransition.animateColor(
            initialValue = lightGrey,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )
        return color
    }
}
