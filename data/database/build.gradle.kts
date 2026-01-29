plugins {
    alias(libs.plugins.weather.android.library)
}

android {
    namespace = "com.lndmg.database"

}

dependencies {
    implementation(libs.room.runtime)
    ksp(libs.ksp.room)
    implementation(libs.room.ktx)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

}