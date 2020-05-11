plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("org.jlleitschuh.gradle.ktlint")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(App.compileSdk)
    defaultConfig {
        applicationId = "io.github.omisie11.coronatracker"
        minSdkVersion(App.minSdk)
        targetSdkVersion(App.targetSdk)
        versionCode = App.versionCode
        versionName = App.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
        animationsDisabled = true
    }

    viewBinding {
        isEnabled = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Libs.KOTLIN_STD_LIB)
    implementation(Libs.APPCOMPAT)
    implementation(Libs.CORE_KTX)
    implementation(Libs.CONSTRAINT_LAYOUT)
    implementation(Libs.MATERIAL)
    implementation(Libs.COROUTINES_ANDROID)
    implementation(Libs.SWIPE_REFRESH_LAYOUT)
    implementation(Libs.PREFERENCE)
    implementation(Libs.LEGACY_SUPPORT_V4)
    // Navigation
    implementation(Libs.NAV_FRAGMENT)
    implementation(Libs.NAV_UI)
    // ViewModel and LiveData
    implementation(Libs.LIFECYCLE_VIEWMODEL)
    implementation(Libs.LIFECYCLE_LIVEDATA)
    kapt(Libs.LIFECYCLE_COMPILER)
    // Retrofit
    implementation(Libs.RETROFIT)
    implementation(Libs.RETROFIT_MOSHI)
    // Moshi
    implementation(Libs.MOSHI)
    implementation(Libs.MOSHI_KOTLIN)
    kapt(Libs.MOSHI_CODEGEN)
    // Room
    implementation(Libs.ROOM_RUNTIME)
    implementation(Libs.ROOM_KTX)
    kapt(Libs.ROOM_COMPILER)
    // Koin Android
    implementation(Libs.KOIN)
    implementation(Libs.KOIN_VIEWMODEL)
    // Timber
    implementation(Libs.TIMBER)
    // ThreeTenABP
    implementation(Libs.THREE_TEN_ABP)
    // MPAndroidChart
    implementation(Libs.MPANDROID_CHART)
    // LeakCanary
    debugImplementation(Libs.LEAK_CANARY)

    // Local Unit tests

    testImplementation(Libs.JUNIT)
    testImplementation(Libs.MOCKITO_KOTLIN)
    testImplementation(Libs.ARCH_CORE_TESTING)
    testImplementation("org.threeten:threetenbp:1.3.2") {
        exclude("com.jakewharton.threetenabp", "threetenabp")
    }
    testImplementation(Libs.COROUTINES_TEST)

    // Instrumented tests

    androidTestImplementation(Libs.ANDROIDX_TEST_CORE)
    androidTestImplementation(Libs.ANDROIDX_TEST_RUNNER)
    androidTestImplementation(Libs.ANDROIDX_TEST_RULES)
    androidTestImplementation(Libs.ANDROIDX_TEST_EXT_JUNIT)
    androidTestImplementation(Libs.HAMCREST)
    androidTestImplementation(Libs.ROOM_TESTING)
    androidTestImplementation(Libs.ARCH_CORE_TESTING)
    androidTestImplementation(Libs.COROUTINES_TEST)
    androidTestImplementation(Libs.ESPRESSO_CORE)
    androidTestImplementation(Libs.ESPRESSO_CONTRIB)
    androidTestImplementation(Libs.ESPRESSO_INTENTS)
}
