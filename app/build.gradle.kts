plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // Remove compose plugin if not using Jetpack Compose
}

android {
    namespace = "com.example.lostandfoundnew"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.lostandfoundnew"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        // Remove compose if using XML layouts
        viewBinding = true  // Add for view binding
    }
}

dependencies {
    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)

    // Material Design
    implementation(libs.material)

    // RecyclerView
    implementation(libs.androidx.recyclerview)

    // SQLite
    implementation(libs.androidx.sqlite)

    // Lifecycle components
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.appcompat)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.gridlayout)
}