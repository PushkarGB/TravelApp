plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.travelactivity"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.travelactivity"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    implementation(libs.play.services.maps)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation ("com.google.android.gms:play-services-maps:18.0.0")
    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.0")

    implementation ("com.airbnb.android:lottie:6.4.1")
    implementation ("com.loopj.android:android-async-http:1.4.10")  // clinet server connectivity
    implementation ("com.google.android.gms:play-services-auth:21.2.0")
    implementation ("com.github.bumptech.glide:glide:4.15.1")
}