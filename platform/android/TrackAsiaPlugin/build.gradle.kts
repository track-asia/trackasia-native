repositories {
  google()
  mavenCentral()
}

plugins {
  `java-gradle-plugin`
  `kotlin-dsl`
}

dependencies {
  implementation("com.android.tools.build:gradle:8.6.1")
}

group = "com.trackasia"
version = "0.0.1"

gradlePlugin {
  plugins {
    create("cmakePlugin") {
      id = "com.trackasia.ccache-plugin"
      implementationClass = "com.trackasia.CcachePlugin"
    }
  }
}
