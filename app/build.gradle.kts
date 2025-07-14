import com.google.devtools.ksp.KspExperimental

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.dagger.hilt)
}

android {
    namespace = "com.pawkeeperdev"
    compileSdk = 35

    buildFeatures {
        viewBinding = true
    }


    defaultConfig {
        applicationId = "com.pawkeeperdev"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        multiDexEnabled = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    ksp {
        @OptIn(KspExperimental::class)
        useKsp2 = false
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //for room data base
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    //for hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    ksp(libs.hilt.android.testing)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.multidex)

    //co-r
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.navigation.fragment.ktx)

    //circular imageview
    implementation(libs.circleimageview)
    //For Glide
    implementation(libs.glide)
    //For skull load
    implementation(libs.shimmer)

    implementation(libs.imagepicker)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.lottie)
}

