plugins {
    `kotlin-dsl`
}

group = "com.lndmg.sportyweather.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    compileOnly(libs.android.tools.build.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    implementation(libs.ksp.gradle.plugin)
    implementation(libs.kotlin.serialization)
    implementation(libs.kotlin.compose.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("weatherApplication") {
            id = "com.lndmg.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("weatherCompose") {
            id = "com.lndmg.compose"
            implementationClass = "ComposeConventionPlugin"
        }
        register("weatherAndroidLibrary") {
            id = "com.lndmg.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("weatherJvmLibrary") {
            id = "com.lndmg.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}
