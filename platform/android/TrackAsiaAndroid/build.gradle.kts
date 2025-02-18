plugins {
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.dokka)
    id("com.android.library")
    id("com.jaredsburrows.license")
    kotlin("android")
    id("trackasia.download-vulkan-validation")
    id("trackasia.gradle-checkstyle")
    id("trackasia.gradle-dependencies-graph")
    id("trackasia.android-nitpick")
    id("trackasia.gradle-publish")
    id("trackasia.artifact-settings")
    id("com.trackasia.ccache-plugin")
}

dependencies {
    lintChecks(project(":TrackAsiaAndroidLint"))
    api(libs.trackasiaJavaGeoJSON)
    api(libs.trackasiaGestures)

    implementation(libs.trackasiaJavaTurf)
    implementation(libs.supportAnnotations)
    implementation(libs.supportFragmentV4)
    implementation(libs.okhttp3)
    implementation(libs.timber)
    implementation(libs.interpolator)

    testImplementation(libs.junit)
    testImplementation(libs.mockito)
    testImplementation(libs.mockk)
    testImplementation(libs.robolectric)
    testImplementation(libs.commonsIO)
    testImplementation(libs.assertjcore)

    androidTestImplementation(libs.testRunner)
    androidTestImplementation(libs.testRules)
}

dokka {
    moduleName.set("TrackAsia Native Android")

    dokkaSourceSets {
        main {
            includes.from("Module.md")

            sourceLink {
                remoteUrl("https://github.com/trackasia/trackasia-native/tree/main/platform/android/")
                localDirectory.set(rootDir)
            }

            // TODO add externalDocumentationLinks when these get dokka or javadocs:
            // - https://github.com/trackasia/trackasia-java
            // - https://github.com/trackasia/trackasia-gestures-android
        }
    }
}

android {
    ndkVersion = Versions.ndkVersion

    defaultConfig {
        compileSdk = 34
        minSdk = 21
        targetSdk = 33
        buildConfigField("String", "GIT_REVISION_SHORT", "\"${getGitRevision()}\"")
        buildConfigField("String", "GIT_REVISION", "\"${getGitRevision(false)}\"")
        buildConfigField(
            "String",
            "TRACKASIA_VERSION_STRING",
            "\"TrackAsia Native/${project.property("VERSION_NAME")}\""
        )
        consumerProguardFiles("proguard-rules.pro")

        externalNativeBuild {
            cmake {
                arguments("-DMLN_LEGACY_RENDERER=ON", "-DMLN_DRAWABLE_RENDERER=OFF")
            }
        }
    }

    flavorDimensions += "renderer"
    productFlavors {
        create("legacy") {
            dimension = "renderer"
            externalNativeBuild {
                cmake {
                    arguments("-DMLN_LEGACY_RENDERER=ON", "-DMLN_DRAWABLE_RENDERER=OFF")
                }
            }
        }
        create("drawable") {
            dimension = "renderer"
            externalNativeBuild {
                cmake {
                    arguments("-DMLN_LEGACY_RENDERER=OFF", "-DMLN_DRAWABLE_RENDERER=ON")
                }
            }
        }
        create("vulkan") {
            dimension = "renderer"
            externalNativeBuild {
                cmake {
                    arguments("-DMLN_LEGACY_RENDERER=OFF", "-DMLN_DRAWABLE_RENDERER=ON")
                    arguments("-DMLN_WITH_OPENGL=OFF", "-DMLN_WITH_VULKAN=ON")
                }
            }
        }
    }

    sourceSets {
        getByName("legacy") {
            java.srcDirs("src/opengl/java/")
        }
        getByName("drawable") {
            java.srcDirs("src/opengl/java/")
        }
    }

    // Build native libraries
    val nativeTargets = mutableListOf("trackasia")
    if (project.hasProperty("mapbox.with_test")) {
        nativeTargets.add("mbgl-test")
    }
    if (project.hasProperty("mapbox.with_benchmark")) {
        nativeTargets.add("mbgl-benchmark")
    }
    nativeBuild(nativeTargets)

    // Avoid naming conflicts, force usage of prefix
    resourcePrefix("trackasia_")

    sourceSets {
        getByName("main") {
            res.srcDirs("src/main/res-public")
        }
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true

            // Robolectric 4.0 required config
            // http://robolectric.org/migrating/#migrating-to-40
            isIncludeAndroidResources = true
        }
    }

    buildTypes {
        debug {
            isTestCoverageEnabled = false
            isJniDebuggable = true
        }
    }

    namespace = "com.trackasia.android"

    lint {
        checkAllWarnings = true
        disable += listOf(
            "MissingTranslation",
            "TypographyQuotes",
            "ObsoleteLintCustomCheck",
            "MissingPermission",
            "WrongThreadInterprocedural"
        )
        warningsAsErrors = false
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

licenseReport {
    generateHtmlReport = false
    generateJsonReport = true
    copyHtmlReportToAssets = false
    copyJsonReportToAssets = false
}

fun getGitRevision(shortRev: Boolean = true): String {
    val cmd = if (shortRev) "git rev-parse --short HEAD" else "git rev-parse HEAD"
    val proc = Runtime.getRuntime().exec(cmd)
    return proc.inputStream.bufferedReader().readText().trim()
}

configurations {
    getByName("implementation") {
        exclude(group = "commons-logging", module = "commons-logging")
        exclude(group = "commons-collections", module = "commons-collections")
    }
}

// apply<DownloadVulkanValidationPlugin>()

// intentionally disabled
// apply(plugin = "trackasia.jacoco-report")
