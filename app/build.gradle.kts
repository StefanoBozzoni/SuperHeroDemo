import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.example.superheroesdemo"
    compileSdk = 34

    defaultConfig {
        val properties = Properties().apply {
            load(rootProject.file("local.properties").inputStream())
        }

        applicationId = "com.example.superheroesdemo"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "MARVEL_API_KEY", "\"${properties.getProperty("MARVEL_API_KEY")}\"")
        buildConfigField("String", "MARVEL_PRIVATE_KEY", "\"${properties.getProperty("MARVEL_PRIVATE_KEY")}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {

    //val libVersions = rootProject.ext["libVersions"]

    implementation("androidx.core:core-ktx:${libVersions.jetpack_core}")
    val composeBom = platform("androidx.compose:compose-bom:2023.06.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Choose one of the following:
    // Material Design 3
    implementation("androidx.compose.material3:material3")

    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("androidx.paging:paging-common-ktx:${libVersions.paging_compose}")
    implementation("androidx.paging:paging-runtime-ktx:${libVersions.paging_compose}")
    implementation("androidx.paging:paging-compose:${libVersions.paging_compose}")
    implementation("androidx.navigation:navigation-compose:${libVersions.navigation_compose}")

    // Optional - Included automatically by material, only add when you need
    // the icons but not the material library (e.g. when using Material3 or a
    // custom design system based on Foundation)
    implementation("androidx.compose.material:material-icons-core")
    // Optional - Add full set of material icons
    implementation("androidx.compose.material:material-icons-extended")
    // Optional - Add window size utils
    implementation("androidx.compose.material3:material3-window-size-class")

    // Optional - Integration with activities, viemodels,livedata
    implementation("androidx.activity:activity-compose:${libVersions.activity_compose}")  //necessary for composable in activities
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${libVersions.lifecycle_compose}") //necessary for viewmodelScope
    implementation("androidx.lifecycle:lifecycle-runtime-compose:${libVersions.lifecycle_compose}") //necessary for collectAsStateWithLifeCycle
    implementation("androidx.compose.runtime:runtime-livedata")

    implementation("io.coil-kt:coil-compose:${libVersions.coil_compose}") //coil compose

    implementation("io.insert-koin:koin-androidx-compose:${libVersions.koin_compose}")
    api("com.squareup.retrofit2:retrofit:${libVersions.retrofit}")
    api("com.squareup.retrofit2:converter-gson:${libVersions.retrofit}")
    api("com.squareup.okhttp3:okhttp:${libVersions.okHttp}")
    api("com.squareup.okhttp3:logging-interceptor:${libVersions.okHttp}")

    implementation("androidx.room:room-runtime:${libVersions.room}")
    annotationProcessor("androidx.room:room-compiler:${libVersions.room}")
    implementation("androidx.room:room-ktx:${libVersions.room}")
    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:${libVersions.room}")
    implementation("io.github.hukumister:lazycardstack:0.0.1")
}