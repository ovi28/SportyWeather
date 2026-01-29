plugins {
    alias(libs.plugins.weather.android.library)
}

android {
    namespace = "com.lndmg.repository"


}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data:database"))
    implementation(project(":data:location"))
    implementation(project(":data:network"))
    implementation(project(":core:common"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.play.services.location)
    implementation(libs.kotlinx.play.services)

}