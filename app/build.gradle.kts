plugins {
    alias(libs.plugins.weather.application.library)
    alias(libs.plugins.weather.compose.library)

}

android {
    namespace = "com.lndmg.sportyweather"

    defaultConfig {
        applicationId = "com.lndmg.sportyweather"
        versionCode = 1
        versionName = "1.0"

    }

}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":feature:search"))
    implementation(project(":feature:weather"))
    implementation(project(":data:database"))
    implementation(project(":data:location"))
    implementation(project(":data:network"))
    implementation(project(":data:repository"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)


}