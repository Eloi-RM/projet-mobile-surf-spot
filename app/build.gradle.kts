plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.surfspot"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.surfspot"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}



dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // ViewModel et LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata:2.6.1")

    // Glide pour le chargement d'images
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("com.google.code.gson:gson:2.8.9")
    // Dépendances de base Android
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.11.0")

    // RecyclerView pour la liste des spots
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // CardView pour les items de la liste
    implementation("androidx.cardview:cardview:1.0.0")

    // SwipeRefreshLayout pour le rafraîchissement par tirage
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // Architecture Components - ViewModel et LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata:2.7.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    // Retrofit pour les appels API REST
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp pour les requêtes HTTP
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")  // optionnel pour le logging

    // Gson pour le parsing JSON
    implementation("com.google.code.gson:gson:2.10.1")

    // Glide pour le chargement et la mise en cache des images
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // Fragment - pour la gestion avancée des fragments
    implementation("androidx.fragment:fragment:1.6.2")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")

    // Edge-to-Edge support
    implementation("androidx.activity:activity:1.8.2")

    // Tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("org.osmdroid:osmdroid-android:6.1.20")
    implementation("org.osmdroid:osmdroid-wms:6.1.20")
    implementation("org.osmdroid:osmdroid-mapsforge:6.1.20")
    implementation("org.osmdroid:osmdroid-geopackage:6.1.20") {
        exclude(group = "com.j256.ormlite", module = "ormlite-core")
    }
}