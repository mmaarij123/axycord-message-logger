import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    id("mmaarij.axycord.plugin")
}

val releaseSigningFile = file("keystore.properties")
val releaseSigning = Properties().apply {
    if (releaseSigningFile.exists()) releaseSigningFile.inputStream().use(::load)
}

axycordPlugin {
    id.set("axycord.messagelogger")
    entrypoint.set("mmaarij.axycord.messagelogger.MessageLoggerPlugin")
    description.set("Keeps deleted messages in chat and shows edit history")
    // Plugin API levels, independent of the app's versionName and SDK artifact version.
    axycordVersionMinimum.set(3)
    axycordVersionMaximumExclusive.set(4)
    usesResources.set(false)
    async()
}

android {
    namespace = "mmaarij.axycord.messagelogger"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "mmaarij.axycord.messagelogger"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"
    }

    signingConfigs {
        if (releaseSigningFile.exists()) create("release") {
            storeFile = rootProject.file(releaseSigning.getProperty("storeFile"))
            storePassword = releaseSigning.getProperty("storePassword")
            keyAlias = releaseSigning.getProperty("keyAlias")
            keyPassword = releaseSigning.getProperty("keyPassword")
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.findByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures.viewBinding = false
    lint {
        checkOnly += setOf("NewApi")
        fatal += "NewApi"
    }
}

dependencies {
    val checkout = providers.gradleProperty("axycordCheckout").orNull
    if (checkout != null) {
        val sdk = rootProject.file(checkout).resolve("build/plugin-sdk/axycord-plugin-api-1.0.6.jar")
        require(sdk.isFile) { "Generate the local SDK first: scripts/build.sh pluginSdkJar" }
        compileOnly(files(sdk))
    } else {
        compileOnly("mmaarij.axycord:plugin-api:1.0.6")
    }
    compileOnly(kotlin("stdlib"))
    compileOnly(libs.rxjava)
    compileOnly("androidx.core:core:1.16.0") { isTransitive = false }
    testImplementation("junit:junit:4.13.2")
    testImplementation(kotlin("stdlib"))
}
