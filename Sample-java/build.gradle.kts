plugins {
    //noinspection AndroidGradlePluginVersion
    id("com.android.application") version "8.4.2"
}

val useMavenOperaAdsSDK = !project.hasProperty("useMavenOperaAdsSDK") ||
        project.property("useMavenOperaAdsSDK").toString().toBoolean()

android {
    namespace = "com.opera.ads.demo"

    defaultConfig {
        applicationId = "com.opera.ads.demo"

        compileSdk = 34
        // We use a minSdk version lower than the minSdk of the Opera Ads SDK intentionally.
        // Expected to get error on sdk initialization on affected devices, but should NOT crash.
        minSdk = 21
        //noinspection OldTargetApi
        targetSdk = 34

        versionCode = 1
        versionName = "1.0"

        buildFeatures {
            buildConfig = true
            viewBinding = true
        }

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
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
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.9.0")
    implementation("androidx.recyclerview:recyclerview:1.3.0")
}
