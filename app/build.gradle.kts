plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
configurations {
    all {
        resolutionStrategy {
            // do not upgrade above 3.12.0 to support API < 21 while server uses
            // COMPATIBLE_TLS, or okhttp3 is used in project
            force("org.hamcrest:hamcrest:2.1")
        }
    }
}

android {
    namespace = "com.project.lembretio"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.project.lembretio"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

val core_version = "1.6.0"

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test:core-ktx:1.5.0")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.core:core-ktx:$core_version")
    androidTestImplementation("org.awaitility:awaitility-kotlin:3.1.6")
}