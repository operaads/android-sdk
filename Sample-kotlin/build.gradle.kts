import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    //noinspection AndroidGradlePluginVersion
    id("com.android.application") version "8.4.2"
    //noinspection NewerVersionAvailable
    id("org.jetbrains.kotlin.android") version "1.9.25"
}

val useMavenOperaAdsSDK = !project.hasProperty("useMavenOperaAdsSDK") ||
        project.property("useMavenOperaAdsSDK").toString().toBoolean()

android {
    namespace = "com.opera.ads.demo"
    //noinspection GradleDependency
    compileSdk = 34

    defaultConfig {
        applicationId = "com.opera.ads.demo"
        // We use a minSdk version lower than the minSdk of the Opera Ads SDK intentionally.
        // Expected to get error on sdk initialization on affected devices, but should NOT crash.
        minSdk = 21
        //noinspection OldTargetApi
        targetSdk = 34

        versionCode = 1
        versionName = "1.0"

        if (!useMavenOperaAdsSDK) {
            missingDimensionStrategy("okhttp", "okhttp4")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
    }
}

// Full support for Kotlin integration begins with version 2.2.2.
kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
        freeCompilerArgs = listOf("-Xannotation-default-target=param-property")
    }
}

dependencies {
    if (useMavenOperaAdsSDK) {
        println("\u001B[31mNotice: \"${project.name}\" depends on Opera Ads SDK from maven repo!\u001B[0m")
        //noinspection GradleDynamicVersion
        implementation("com.opera:opera-ads:+")
    } else {
        println("\u001B[31mNotice: \"${project.name}\" depends on ':sdk' module!\u001B[0m")
        implementation(project(":sdk"))
    }

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.9.0")
    implementation("androidx.recyclerview:recyclerview:1.3.0")
}
