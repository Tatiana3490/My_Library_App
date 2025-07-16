plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.svalero.mylibraryapp"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.svalero.mylibraryapp"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

  //  buildFeatures {
  //      buildConfig = true
 //   }


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
}

dependencies {
    implementation("com.mapbox.maps:android:10.9.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.4.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Core libraries
  /*  implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    implementation(libs.recyclerviewSelection)
    implementation(libs.preference)

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Mapbox SDK and annotation plugin via version catalog
   // implementation(libs.mapboxSdk)
    //implementation(libs.annotationPlugin)
    implementation("com.mapbox.maps:android:10.15.1")
    implementation("com.mapbox.maps:plugin-annotation:10.15.1")

    // Room database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")

    // Image loading con Glide
    implementation("com.github.bumptech.glide:glide:4.15.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.0")

    // Testing
    testImplementation(libs.junit)
    testImplementation("org.mockito:mockito-core:5.0.0")
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)*/
}
