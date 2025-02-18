extra["trackAsiaArtifactGroupId"] = "io.github.track-asia"
extra["trackAsiaArtifactId"] = "android-sdk"
extra["trackAsiaArtifactTitle"] = "TrackAsia Android"
extra["trackAsiaArtifactDescription"] = "TrackAsia Android"
extra["trackAsiaDeveloperName"] = "TrackAsia"
extra["trackAsiaDeveloperId"] = "trackasia"
extra["trackAsiaArtifactUrl"] = "https://github.com/trackasia/trackasia-native"
extra["trackAsiaArtifactScmUrl"] = "scm:git@github.com:trackasia/trackasia-native.git"
extra["trackAsiaArtifactLicenseName"] = "BSD"
extra["trackAsiaArtifactLicenseUrl"] = "https://opensource.org/licenses/BSD-2-Clause"

// Handling conditional assignment for versionName
extra["versionName"] = if (project.hasProperty("VERSION_NAME")) {
    project.property("VERSION_NAME")
} else {
    System.getenv("VERSION_NAME")
}
