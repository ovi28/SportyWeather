plugins {
    alias(libs.plugins.weather.android.library)
    alias(libs.plugins.weather.compose.library)

}

android {
    namespace = "com.lndmg.ui"

}

dependencies {
    implementation(project(":domain"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}