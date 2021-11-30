plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = BuildConfig.compileSdkVersion

    defaultConfig {
        minSdk = BuildConfig.minSdkVersion
        targetSdk = BuildConfig.targetSdkVersion


        testInstrumentationRunner = BuildConfig.testRunner
    }

    buildTypes {
        getByName("debug"){
            buildConfigField("String", "AUTH_TOKEN", "\"df7c313b47b7ef87c64c0f5f5cebd6086bbb0fa\"")
            buildConfigField("String", "BASE_URL", "\"https://s3.eu-central-1.amazonaws.com\"")
        }
        getByName("release") {
            buildConfigField("String", "AUTH_TOKEN", "\"df7c313b47b7ef87c64c0f5f5cebd6086bbb0fa\"")
            buildConfigField("String", "BASE_URL", "\"https://s3.eu-central-1.amazonaws.com\"")
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
}

dependencies {

    implementation(KotlinDependencies.kotlinStd)

    //hilt
    implementation(Libraries.hilt)
    kapt(Libraries.hiltAnnotationProcessor)

    //For instrumentation tests
    implementation(Libraries.hiltInstrumentation)
    kapt(Libraries.hiltInsAnnotationProcessor)

    //For local unit tests
    implementation(Libraries.hiltUnitTest)
    kapt(Libraries.hiltUnitTestAnnotationProcessor)

    implementation(Libraries.retrofit)
    implementation(Libraries.retrofitMoshiConverter)
    implementation(Libraries.retrofitRxAdapter)

    implementation(Libraries.moshi)
    kapt(Libraries.moshiKotlinCodeGen)

    implementation(Libraries.loggingInterceptor) {
        this.exclude("org.json", "json")
    }

    //testing
    testImplementation(TestingDependencies.junit)
    testImplementation(TestingDependencies.mockWebServer)
    testImplementation(TestingDependencies.assertj)

    testImplementation (TestingDependencies.mockitoKotlin)
    testImplementation (TestingDependencies.mockitoInline)
}