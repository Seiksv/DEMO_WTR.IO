plugins {

    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.demo3m"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.demo3m"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
// Dagger
    var daggerVersion = "2.48"
    implementation("com.google.dagger:hilt-android:$daggerVersion")
    kapt("com.google.dagger:hilt-android-compiler:$daggerVersion")

// Retrofit para realizar solicitudes HTTP
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

// ViewModel y LiveData
    implementation ("androidx.activity:activity-ktx:1.7.2")
    implementation ("androidx.fragment:fragment-ktx:1.6.1")
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.4.0")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.4.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")

// Navigation
    val navigationVersion = "2.3.5"
    implementation ("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation ("androidx.navigation:navigation-ui-ktx:$navigationVersion")

// Coroutines
    implementation ("androidx.compose.material3:material3:1.2.1")
    implementation ("androidx.compose.material3:material3-window-size-class:1.2.1")
    implementation ("androidx.compose.material3:material3-adaptive-navigation-suite:1.0.0-alpha05")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}