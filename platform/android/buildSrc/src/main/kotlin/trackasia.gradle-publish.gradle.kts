import org.gradle.kotlin.dsl.get
import java.util.Locale

plugins {
    `maven-publish`
    signing
    id("com.android.library")
    id("trackasia.artifact-settings")
    id("trackasia.publish-root")
}

tasks.register<Javadoc>("androidJavadocs") {
    source = fileTree(android.sourceSets.getByName("main").java.srcDirs)
    classpath = files(android.bootClasspath)
    isFailOnError = false
}

tasks.register<Jar>("androidJavadocsJar") {
    archiveClassifier.set("javadoc")
    from(tasks.named("androidJavadocs", Javadoc::class.java).get().destinationDir)
}

tasks.register<Jar>("androidSourcesJar") {
    archiveClassifier.set("sources")
    from(android.sourceSets.getByName("main").java.srcDirs)
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
    (options as StandardJavadocDocletOptions).apply {
        charSet = "UTF-8"
        docEncoding = "UTF-8"
    }
}


artifacts {
    add("archives", tasks.named("androidSourcesJar"))
    add("archives", tasks.named("androidJavadocsJar"))
}

project.logger.lifecycle(project.extra["versionName"].toString())

version = project.extra["versionName"] as String
group = project.extra["trackAsiaArtifactGroupId"] as String

fun configureMavenPublication(
    renderer: String,
    publicationName: String,
    artifactIdPostfix: String,
    descriptionPostfix: String,
) {
    publishing {
        publications {
            create<MavenPublication>(publicationName) {
                groupId = project.group.toString()
                artifactId = "${project.extra["trackAsiaArtifactId"]}$artifactIdPostfix"
                version = project.version.toString()

                from(components["${renderer}Release"])

                pom {
                    name.set("${project.extra["trackAsiaArtifactTitle"]}$descriptionPostfix")
                    description.set("${project.extra["trackAsiaArtifactTitle"]}$descriptionPostfix")
                    url.set(project.extra["trackAsiaArtifactUrl"].toString())
                    licenses {
                        license {
                            name.set(project.extra["trackAsiaArtifactLicenseName"].toString())
                            url.set(project.extra["trackAsiaArtifactLicenseUrl"].toString())
                        }
                    }
                    developers {
                        developer {
                            id.set(project.extra["trackAsiaDeveloperId"].toString())
                            name.set(project.extra["trackAsiaDeveloperName"].toString())
                            email.set("team@track-asia.com")
                        }
                    }
                    scm {
                        connection.set(project.extra["trackAsiaArtifactScmUrl"].toString())
                        developerConnection.set(project.extra["trackAsiaArtifactScmUrl"].toString())
                        url.set(project.extra["trackAsiaArtifactUrl"].toString())
                    }
                }
            }
        }
    }
}


// workaround for https://github.com/gradle/gradle/issues/26091#issuecomment-1836156762
// https://github.com/gradle-nexus/publish-plugin/issues/208
tasks {
    withType<PublishToMavenRepository> {
        dependsOn(withType<Sign>())
    }
}

afterEvaluate {
    configureMavenPublication("drawable", "opengl", "", "")
    configureMavenPublication("vulkan", "vulkan", "-vulkan", "(Vulkan)")
    // Right now this is the same as the first, but in the future we might release a major version
    // which defaults to Vulkan (or has support for multiple backends). We will keep using only
    // OpenGL ES with this artifact ID if that happens.
    configureMavenPublication("drawable", "opengl2", "-opengl", " (OpenGL ES)")
}


afterEvaluate {
    android.libraryVariants.forEach { variant ->
        tasks.named("androidJavadocs", Javadoc::class.java).configure {
            doFirst {
                classpath = classpath.plus(files(variant.javaCompileProvider.get().classpath))
            }
        }
        tasks.named("publishOpengl2PublicationToMavenLocal") {
            dependsOn(tasks.named("signOpengl2Publication"))  // Khai báo sự phụ thuộc giữa sign và publish
        }

        tasks.withType<PublishToMavenLocal> {
            dependsOn(tasks.withType<Sign>())
        }
    }
}

signing {
    sign(publishing.publications)
}
