plugins {
    id("com.android.application")

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

    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor ("androidx.room:room-compiler:2.6.1")

    implementation("com.mapbox.maps:android:10.9.1")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.4.0")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.preference)

    implementation(libs.room.common.jvm)
    implementation(libs.room.runtime.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)

    androidTestImplementation(libs.espresso.core)




}
