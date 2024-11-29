// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    kotlin("plugin.serialization") version "1.9.0" apply false
//    alias(libs.plugins.compose.compiler) apply false

    //firebase sdk
    id("com.google.gms.google-services") version "4.4.2" apply false
}