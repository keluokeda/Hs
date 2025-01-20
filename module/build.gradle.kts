plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    id("androidx.room")
}

android {
    namespace = "com.ke.hs.module"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    room {
        schemaDirectory("$projectDir/schemas")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    implementation(libs.shizuku.api)
    implementation(libs.shizuku.provider)
    implementation(libs.logger)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    // To use Kotlin annotation processing tool (kapt)
    kapt(libs.androidx.room.compiler)
    // optional - Paging 3 Integration
    implementation(libs.androidx.room.paging)

    implementation(libs.androidx.navigation.compose)

    implementation(libs.retrofit)
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi)
    kapt(libs.moshi.kotlin.codegen)

    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("androidx.lifecycle:lifecycle-service:2.7.0")
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("co.yml:ycharts:2.1.0")
//    implementation("androidx.compose.material:material:1.6.6")
    implementation ("com.tencent.bugly:crashreport:latest.release")
    implementation("androidx.datastore:datastore-preferences:1.1.0")
//    implementation("com.tencent.shiply:upgrade:2.0.0-RC01")
    implementation("com.tencent.shiply:upgrade:2.1.5-RC01")
    implementation("com.tencent.shiply:upgrade-ui:2.1.5-RC01") // 弹框ui相关，业务方如果自己自定义弹框，可以不依赖

    implementation ("com.huawei.agconnect:agconnect-core:1.9.1.300")
    implementation ("com.huawei.agconnect:agconnect-remoteconfig-ktx:1.9.1.300")
//    implementation("com.huawei.agconnect:agconnect-remoteconfig:1.9.1.302")
}