plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    kotlin("plugin.serialization") version "2.0.21"

    id("kotlin-parcelize")

    id("dev.shreyaspatil.compose-compiler-report-generator") version "1.4.2"
//
//    id("com.google.devtools.ksp")
//
//    id("com.google.dagger.hilt.android")

    alias(libs.plugins.google.ksp)
    alias(libs.plugins.android.hilt)
}

android {
    namespace = "com.frogbubbletea.ustcoursemobile"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.frogbubbletea.ustcoursemobile"
        minSdk = 24
        targetSdk = 35
        versionCode = 4
        versionName = "0.4.0-alpha"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")

            isDebuggable = false
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
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    val nav_version = "2.8.8"

    implementation("androidx.navigation:navigation-compose:$nav_version")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    implementation("androidx.activity:activity:1.10.1")

    implementation("com.google.android.material:material:1.12.0")

    implementation("androidx.transition:transition-ktx:1.5.1")

    implementation(platform(libs.androidx.compose.bom))

    implementation("com.fleeksoft.ksoup:ksoup-kotlinx:0.2.2")

    implementation("com.fleeksoft.ksoup:ksoup-network:0.2.2")

    implementation("androidx.compose.runtime:runtime-tracing")

    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.8")

//    val room_version = "2.7.1"
//
//    implementation("androidx.room:room-runtime:$room_version")
//
//    ksp("androidx.room:room-compiler:$room_version")
//
//    implementation("com.google.dagger:hilt-android:2.56.2")
//
//    ksp("com.google.dagger:hilt-android-compiler:2.56.2")

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.bundles.hilt)
    implementation(libs.bundles.room)
    ksp(libs.bundles.hilt.ksp)
    ksp(libs.androidx.room.compiler)
}