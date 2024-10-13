plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.engliword.wordin"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.engliword.wordin"
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

    packaging {
        resources {
            // Додаємо конфліктні файли до виключень
            excludes.add("META-INF/DEPENDENCIES")
            excludes.add("META-INF/LICENSE")
            excludes.add("META-INF/LICENSE.txt")
            excludes.add("META-INF/NOTICE")
            excludes.add("META-INF/NOTICE.txt")
        }
    }
}

kapt {
    correctErrorTypes = true
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui)

    // Charts
    implementation(libs.williamchart)

    // Animation Lottie
    implementation(libs.lottie)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    //Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.ui.auth)
    implementation(libs.firebase.auth)

    // Google Play Services для аутентифікації
    implementation(libs.play.services.auth.v2070)

    // Google API для Drive
    implementation(libs.google.api.services.drive.vv3rev20220815200)

    // Google API Client для Android
    implementation(libs.google.api.client.android.v1330)

    // Google OAuth Client
    implementation(libs.google.oauth.client.jetty.v1331)

    // Google HTTP Client та Gson для обробки HTTP-запитів та JSON
    implementation(libs.google.http.client.gson.v1410)
    implementation(libs.google.http.client.android)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
