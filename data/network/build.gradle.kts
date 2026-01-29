import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.weather.android.library)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

android {
    namespace = "com.lndmg.network"

    val apiKey = localProperties.getProperty("MAP_API_KEY") ?: ""
    defaultConfig {
        buildConfigField("String", "MAP_API_KEY", "\"$apiKey\"")
    }
    buildFeatures {
        buildConfig = true
    }

}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.converter.kotlinx.serialization)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

}