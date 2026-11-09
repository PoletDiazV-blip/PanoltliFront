plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    id("org.jlleitschuh.gradle.ktlint")
}

android {
    namespace = "com.polet.panoltli"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.polet.panoltli"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Lectura con respaldo directo para evitar cadenas vacías por caché de Gradle
        val googleClientId = (project.findProperty("GOOGLE_WEB_CLIENT_ID") as? String)
            ?.takeIf { it.isNotBlank() }
            ?: "970409351390-6ikqgb0vumck51ktmu1fukvird1hcva8.apps.googleusercontent.com"

        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"$googleClientId\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
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
    implementation(libs.androidx.material.icons.extended)

    // Red (Retrofit + Gson)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")


    // Hilt (Inyección de dependencias)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Credential Manager para Google Sign-In
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.google.id)

}

ktlint {
    android.set(true)
    ignoreFailures.set(true)
    disabledRules.set(
        setOf(
            "package-name",
            "filename",
            "no-wildcard-imports",
            "function-naming",
            "experimental:package-name"
        )
    )
}
