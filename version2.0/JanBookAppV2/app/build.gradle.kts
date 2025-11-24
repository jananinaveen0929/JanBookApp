plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.janbookappv2"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.janbookappv2"
        minSdk = 29
        targetSdk = 36
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
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // RecyclerView (For creating efficient lists)
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // CardView (For creating cards)
    implementation("androidx.cardview:cardview:1.0.0")
    // Material Design - Required for FloatingActionButton and modern UI
    implementation("com.google.android.material:material:1.11.0")
// Gson - Required for saving/loading books to SharedPreferences ⭐ NEW!
    implementation("com.google.code.gson:gson:2.10.1")
// CoordinatorLayout - Required for FAB layout
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
}