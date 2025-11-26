plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.kotlintest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.kotlintest"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        // Important: change the keystore for a production deployment
        create("release") {
            // get from env variables
            val userKeystore = File(System.getProperty("user.home"), ".android/debug.keystore")
            val localKeystore = File("/Users/mohamad/AndroidStudioProjects/kotlintest/key.file")
            val hasKeyInfo = userKeystore.exists()

            storeFile = if (hasKeyInfo) userKeystore else localKeystore
            storePassword = if (hasKeyInfo) "android" else System.getenv("compose_store_password")
            keyAlias = if (hasKeyInfo) "androiddebugkey" else System.getenv("compose_key_alias")
            keyPassword = if (hasKeyInfo) "android" else System.getenv("compose_key_password")

        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = "\"https://api.mockapi.com/\""
            )
            buildConfigField(
                type = "String",
                name = "API_KEY",
                value = "\"b0742dc665ee4c659c06add509604c3e\""
            )

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = "\"https://api.mockapi.com/\""
            )
            buildConfigField(
                type = "String",
                name = "API_KEY",
                value = "\"b0742dc665ee4c659c06add509604c3e\""
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("libs")
        }
    }
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    implementation(files("libs/contec_spo2_ble_sdk_v2.0.jar"))
    implementation (files("libs/contec_htd_sdk_v1.8.aar"))
    implementation (files("libs/contec_bp_ble_sdk_v1.4.jar"))
    implementation (files("libs/ecgsdk_library-release-1.8.7_202406191047.aar"))
    implementation(files("libs/minttisdk_v2.1.2-beta.jar"))
    implementation(files("libs/wbclib.jar"))

    // Architecture Components
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)

    // Hilt
    implementation(libs.hilt.android.core)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // Jetpack Compose
    val composeBom = platform(libs.androidx.compose.bom)

    implementation(libs.androidx.activity.compose)
    implementation(composeBom)
    implementation(libs.androidx.compose.foundation.core)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.accompanist.appcompat.theme)
    implementation(libs.accompanist.swiperefresh)
    implementation(libs.androidx.constraintlayout.compose)

    debugImplementation(composeBom)
    debugImplementation(libs.androidx.compose.ui.tooling.core)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    //Ktor
    implementation(libs.io.ktor.core)
    implementation(libs.io.ktor.android)
    implementation(libs.io.ktor.client.content.negotiation)
    implementation(libs.io.ktor.serialization.kotlinx.json)
    implementation(libs.io.ktor.logging)
    implementation(libs.kotlinx.serialization.json)
//testing
    // Dependencies for local unit tests
    testImplementation(composeBom)
    testImplementation(libs.junit4)
    testImplementation(libs.androidx.archcore.testing)
    testImplementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.navigation.testing)
    testImplementation(libs.androidx.test.espresso.core)
    testImplementation(libs.androidx.test.espresso.contrib)
    testImplementation(libs.androidx.test.espresso.intents)
    testImplementation(libs.google.truth)
    testImplementation(libs.androidx.compose.ui.test.junit)
    // AndroidX Test - Hilt testing
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)
    implementation(group = "com.antonkarpenko", name = "ffmpeg-kit-min-gpl", version = "2.1.0")
}