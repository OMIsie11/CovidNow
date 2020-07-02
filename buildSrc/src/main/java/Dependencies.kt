object App {
    const val compileSdk = 29
    const val minSdk = 21
    const val targetSdk = 28
    const val versionCode = 4
    const val versionName = "1.1.2"
}

object Versions {
    const val GRADLE = "3.6.3"
    const val KOTLIN = "1.3.72"
    const val KTLINT_GRADLE = "9.1.1"
    const val NAV_SAFE_ARGS = "2.2.1"

    // Libs
    const val APPCOMPAT = "1.1.0"
    const val CORE_KTX = "1.2.0"
    const val COROUTINES_ANDROID = "1.3.5"
    const val CONSTRAINT_LAYOUT = "1.1.3"
    const val MATERIAL = "1.2.0-alpha05"
    const val SWIPE_REFRESH_LAYOUT = "1.0.0"
    const val PREFERENCE = "1.1.0"
    const val LEGACY_SUPPORT_V4 = "1.0.0"
    const val NAVIGATION = "2.2.1"
    const val LIFECYCLE = "2.2.0"
    const val RETROFIT = "2.7.1"
    const val MOSHI = "1.9.2"
    const val ROOM = "2.2.4"
    const val KOIN = "2.1.4"
    const val TIMBER = "4.7.1"
    const val THREE_TEN_ABP = "1.2.2"
    const val MPANDROID_CHART = "v3.1.0"
    const val LEAK_CANARY = "2.2"

    // TESTING
    // Common dependencies
    const val COROUTINES_TEST = "1.3.5"
    const val ARCH_CORE_TESTING = "2.1.0"

    // Local tests
    const val JUNIT = "4.12"
    const val MOCKITO_KOTLIN = "2.2.0"

    // Instrumented tests
    const val ANDROIDX_TEST = "1.2.0"
    const val ANDROIDX_TEST_EXT_JUNIT = "1.1.1"
    const val HAMCREST = "1.3"
    const val ESPRESSO = "3.2.0"
}

object Libs {
    const val KOTLIN_STD_LIB = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.KOTLIN}"
    const val APPCOMPAT = "androidx.appcompat:appcompat:${Versions.APPCOMPAT}"
    const val CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
    const val CONSTRAINT_LAYOUT =
        "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
    const val MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"
    const val COROUTINES_ANDROID =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES_ANDROID}"
    const val SWIPE_REFRESH_LAYOUT =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.SWIPE_REFRESH_LAYOUT}"
    const val PREFERENCE = "androidx.preference:preference-ktx:${Versions.PREFERENCE}"
    const val LEGACY_SUPPORT_V4 = "androidx.legacy:legacy-support-v4:${Versions.LEGACY_SUPPORT_V4}"
    const val NAV_FRAGMENT =
        "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"
    const val NAV_UI = "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"
    const val LIFECYCLE_VIEWMODEL =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE}"
    const val LIFECYCLE_LIVEDATA = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIFECYCLE}"
    const val LIFECYCLE_COMPILER = "androidx.lifecycle:lifecycle-compiler:${Versions.LIFECYCLE}"
    const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val RETROFIT_MOSHI = "com.squareup.retrofit2:converter-moshi:${Versions.RETROFIT}"
    const val MOSHI = "com.squareup.moshi:moshi:${Versions.MOSHI}"
    const val MOSHI_KOTLIN = "com.squareup.moshi:moshi-kotlin:${Versions.MOSHI}"
    const val MOSHI_CODEGEN = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.MOSHI}"
    const val ROOM_RUNTIME = "androidx.room:room-runtime:${Versions.ROOM}"
    const val ROOM_KTX = "androidx.room:room-ktx:${Versions.ROOM}"
    const val ROOM_COMPILER = "androidx.room:room-compiler:${Versions.ROOM}"
    const val KOIN = "org.koin:koin-android:${Versions.KOIN}"
    const val KOIN_VIEWMODEL = "org.koin:koin-androidx-viewmodel:${Versions.KOIN}"
    const val TIMBER = "com.jakewharton.timber:timber:${Versions.TIMBER}"
    const val THREE_TEN_ABP = "com.jakewharton.threetenabp:threetenabp:${Versions.THREE_TEN_ABP}"
    const val MPANDROID_CHART = "com.github.PhilJay:MPAndroidChart:${Versions.MPANDROID_CHART}"
    const val LEAK_CANARY = "com.squareup.leakcanary:leakcanary-android:${Versions.LEAK_CANARY}"

    // TESTING
    // Common
    const val COROUTINES_TEST =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINES_TEST}"
    const val ARCH_CORE_TESTING = "androidx.arch.core:core-testing:${Versions.ARCH_CORE_TESTING}"

    // Local unit tests
    const val JUNIT = "junit:junit:${Versions.JUNIT}"
    const val MOCKITO_KOTLIN =
        "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.MOCKITO_KOTLIN}"

    // Instrumented tests
    const val ANDROIDX_TEST_CORE = "androidx.test:core:${Versions.ANDROIDX_TEST}"
    const val ANDROIDX_TEST_RUNNER = "androidx.test:runner:${Versions.ANDROIDX_TEST}"
    const val ANDROIDX_TEST_RULES = "androidx.test:rules:${Versions.ANDROIDX_TEST}"
    const val ANDROIDX_TEST_EXT_JUNIT =
        "androidx.test.ext:junit:${Versions.ANDROIDX_TEST_EXT_JUNIT}"
    const val HAMCREST = "org.hamcrest:hamcrest-library:${Versions.HAMCREST}"
    const val ROOM_TESTING = "androidx.room:room-testing:${Versions.ROOM}"

    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO}"
    const val ESPRESSO_CONTRIB = "androidx.test.espresso:espresso-contrib:${Versions.ESPRESSO}"
    const val ESPRESSO_INTENTS = "androidx.test.espresso:espresso-intents:${Versions.ESPRESSO}"
}