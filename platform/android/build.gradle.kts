plugins {
    alias(libs.plugins.nexusPublishPlugin)
    alias(libs.plugins.kotlinter) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    id("com.jaredsburrows.license") version "0.9.8" apply false
    id("trackasia.dependencies")
    id("trackasia.publish-root")
}


nexusPublishing {
    repositories {
        sonatype {
            stagingProfileId.set(extra["sonatypeStagingProfileId"] as String?)
            username.set(extra["ossrhUsername"] as String?)
            password.set(extra["ossrhPassword"] as String?)
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
        }
    }
}
