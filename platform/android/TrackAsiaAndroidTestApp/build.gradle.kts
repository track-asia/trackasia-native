plugins {
    alias(libs.plugins.kotlinter)
    id("com.android.application")
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinPluginSerialization)
    id("trackasia.gradle-make")
    id("trackasia.gradle-config")
    id("trackasia.gradle-checkstyle")
    id("trackasia.gradle-lint")
}


fun obtainTestBuildType(): String {
    return if (project.hasProperty("testBuildType")) {
        project.properties["testBuildType"] as String
    } else {
        "debug"
    }
}

android {
    ndkVersion = Versions.ndkVersion

    compileSdk = 34

    defaultConfig {
        applicationId = "com.trackasia.android.testapp"
        minSdk = 21
        targetSdk = 33
        versionCode = 14
        versionName = "6.0.1"
        testInstrumentationRunner = "com.trackasia.android.InstrumentationRunner"
        multiDexEnabled = true
    }

    nativeBuild(listOf("example-custom-layer"))

    packaging {
        resources.excludes += listOf("META-INF/LICENSE.txt", "META-INF/NOTICE.txt", "LICENSE.txt")
    }

    buildTypes {
        getByName("debug") {
            isJniDebuggable = true
            isDebuggable = true
            isTestCoverageEnabled = true
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            testProguardFiles("test-proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    testBuildType = obtainTestBuildType()

    flavorDimensions += "renderer"

    productFlavors {
        create("legacy") {
            dimension = "renderer"
        }
        create("drawable") {
            dimension = "renderer"
        }
        create("vulkan") {
            dimension = "renderer"
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    namespace = "com.trackasia.android.testapp"

    lint {
        abortOnError = false
        baseline = file("lint-baseline-local.xml")
        checkAllWarnings = true
        disable += listOf("MissingTranslation", "GoogleAppIndexingWarning", "UnpackedNativeCode", "IconDipSize", "TypographyQuotes")
        warningsAsErrors = true
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":TrackAsiaAndroid"))
    implementation(libs.trackasiaJavaTurf)

    implementation(libs.supportRecyclerView)
    implementation(libs.supportDesign)
    implementation(libs.supportConstraintLayout)
    implementation(libs.kotlinxSerializationJson)

    implementation(libs.multidex)
    implementation(libs.timber)
    implementation(libs.okhttp3)
    implementation(libs.kotlinxCoroutinesCore)
    implementation(libs.kotlinxCoroutinesAndroid)

    debugImplementation(libs.leakCanary)

    androidTestImplementation(libs.supportAnnotations)
    androidTestImplementation(libs.testRunner)
    androidTestImplementation(libs.testRules)
    androidTestImplementation(libs.testEspressoCore)
    androidTestImplementation(libs.testEspressoIntents)
    androidTestImplementation(libs.testEspressoContrib)
    androidTestImplementation(libs.testUiAutomator)
    androidTestImplementation(libs.appCenter)
    androidTestImplementation(libs.androidxTestExtJUnit)
    androidTestImplementation(libs.androidxTestCoreKtx)
    androidTestImplementation(libs.kotlinxCoroutinesTest)
}
