plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.pushstartuphub.smartwrite"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.pushstartuphub.smartwrite"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.sdp)
    implementation(libs.ssp)
    implementation(libs.richeditor)

    implementation(libs.generativeai)

    // Required for one-shot operations (to use `ListenableFuture` from Guava Android)
    implementation(libs.guava)

    // Required for streaming operations (to use `Publisher` from Reactive Streams)
    implementation(libs.reactive.streams)

    implementation(libs.material)



    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}