// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath("com.huawei.agconnect:agcp:1.9.1.300")
        classpath("com.android.tools.build:gradle:8.7.0")
    }
}

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.jetbrainsKotlinJvm) apply false
    alias(libs.plugins.androidHilt) apply false
    id("androidx.room") version "2.6.1" apply false
    alias(libs.plugins.androidLibrary) apply false
}