plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("kotlin-android")
}

android {
    namespace = "com.empcloud.empmonitor"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.empcloud.empmonitor"
        minSdk = 24
        targetSdk = 36
        versionCode = 10
        versionName = "1.9"

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

    externalNativeBuild {
        cmake {
            path("CMakeLists.txt")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    ndkVersion = "29.0.14206865"

}

dependencies {

    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation ("com.google.android.material:material:1.12.0")
    implementation ("com.google.android.material:material:1.12.0")
    implementation ("com.google.android.material:material:1.12.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation ("com.google.android.material:material:1.12.0")
    implementation ("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity:1.10.1")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.navigation:navigation-runtime-ktx:2.8.9")
    implementation("androidx.camera:camera-core:1.4.2")
    implementation("androidx.camera:camera-lifecycle:1.4.2")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.9")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.9")




    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")


    //Material Design Deps//
    implementation("com.google.android.material:material:1.12.0")


    //kotlin kapt and dagger-hilt dependancy//
    implementation("com.google.dagger:hilt-android:2.52")
    kapt("com.google.dagger:hilt-android-compiler:2.52")

    //Retrofit ,Gson, OkHttp Library//
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Fix Duplicate class
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.9.24"))
    //Coroutines and lifecycle//
    implementation("androidx.fragment:fragment-ktx:1.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-process:2.8.7")
    implementation("androidx.lifecycle:lifecycle-service:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.8.7")
    annotationProcessor("androidx.lifecycle:lifecycle-compiler:2.8.7")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:2.1.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.1.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.0.20")


    //Picasso Image loader from url//
    implementation ("com.squareup.picasso:picasso:2.71828")


    //password visibility/invisibility
    implementation ("com.google.android.material:material:1.12.0")
    implementation ("com.airbnb.android:lottie:6.1.0")



//    implementation("com.jaedongchicken:ytplayer:1.4.4")


//    implementation("'com.squareup.retrofit2:retrofit:2.9.0")
//    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
//    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-retrofit:1.5.1")


    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")

    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))

    implementation ("com.google.firebase:firebase-auth-ktx:23.2.0")

    implementation("net.rimoto:intlphoneinput:1.0.1")

    implementation ("com.google.android.gms:play-services-auth:21.3.0")

    implementation ("com.google.android.gms:play-services-maps:19.2.0")
    implementation ("com.google.android.gms:play-services-location:21.3.0")
    implementation ("com.google.android.libraries.places:places:4.2.0")

    implementation ("androidx.recyclerview:recyclerview:1.4.0")

    implementation ("com.ncorti:slidetoact:0.11.0")

    implementation ("com.google.android.material:material:1.12.0")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    implementation ("com.google.maps.android:android-maps-utils:2.2.0")

    implementation ("it.xabaras.android:recyclerview-swipedecorator:1.4")

    implementation ("com.google.android.gms:play-services-location:21.3.0")

    implementation ("com.hbb20:ccp:2.6.0")

    implementation ("androidx.camera:camera-camera2:1.4.2")
    implementation ("androidx.camera:camera-lifecycle:1.4.2")
    implementation ("androidx.camera:camera-view:1.4.2")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")

    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation ("com.google.code.gson:gson:2.10.1") // Replace with the latest version of Gson
    implementation ("com.squareup.retrofit2:converter-gson:latest_version") // Retrofit Gson converter

    implementation ("androidx.exifinterface:exifinterface:1.4.1")

    implementation ("com.google.android.gms:play-services-auth:21.3.0")

    implementation ("androidx.core:core-ktx:1.16.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation ("com.google.android.gms:play-services-location:21.3.0")
    implementation ("androidx.work:work-runtime-ktx:2.10.1")
    implementation ("androidx.room:room-runtime:2.7.1")
    kapt ("androidx.room:room-compiler:2.7.1")
    implementation ("androidx.room:room-ktx:2.7.1")

    implementation ("com.google.android.material:material:1.12.0") // or the latest version

    implementation ("androidx.camera:camera-camera2:1.4.2")
    implementation ("androidx.camera:camera-lifecycle:1.4.2")
    implementation ("androidx.camera:camera-view:1.4.2")

    implementation ("com.google.firebase:firebase-auth-ktx")
    implementation ("com.google.android.gms:play-services-auth-api-phone:18.2.0")
    implementation ("com.google.firebase:firebase-auth:23.2.0")


}
kapt {
    correctErrorTypes = true
}