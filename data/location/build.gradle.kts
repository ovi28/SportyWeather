plugins {
    alias(libs.plugins.weather.android.library)
}

android {
    namespace = "com.lndmg.location"


}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.play.services.location)

}