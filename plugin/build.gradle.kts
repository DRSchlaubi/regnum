import com.jfrog.bintray.gradle.BintrayExtension
import org.jetbrains.dokka.DokkaConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.LinkMapping
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.dokka").version("0.9.17")
    id("com.jfrog.bintray").version("1.8.4")
    kotlin("jvm") version "1.3.21"
    java
    `maven-publish`
}

group = "cc.hawkbot.regnum.server"
val archivesBasename = "plugin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {

    compile(project(":shared"))

    compile("de.foryasee:plugins:1.1.0")

    // Server
    implementation("io.javalin:javalin:2.6.0")
    @Suppress("SpellCheckingInspection")
    implementation("net.dv8tion:JDA:4.ALPHA.0_50")

    implementation(kotlin("stdlib-jdk8"))
    testCompile("junit", "junit", "4.12")
}

val dokkaJar by tasks.creating(Jar::class)

val sourcesJar by tasks.creating(Jar::class)

artifacts {
    add("archives", dokkaJar)
    add("archives", sourcesJar)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")
    setPublications("mavenJava")
    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        repo = "maven"
        name = "regnum-plugin"
        userOrg = "hawk"
        setLicenses("GPL-3.0")
        vcsUrl = "https://github.com/DRSchlaubi/regnum.git"
        version(delegateClosureOf<BintrayExtension.VersionConfig> {
            name = project.version as String
        })
    })
}

tasks {
    "dokka"(DokkaTask::class) {
        outputFormat = "html"
        outputDirectory = "public"
        jdkVersion = 8
        reportUndocumented = true
        impliedPlatforms = mutableListOf("JVM")
        sourceDirs = files("src/main/kotlin", "src/main/java")
        sourceDirs.forEach {
            val relativePath = rootDir.toPath().relativize(it.toPath()).toString()
            linkMapping(delegateClosureOf<LinkMapping> {
                dir = it.absolutePath
                url = "https://github.com/DRSchlaubi/regnum/tree/master/$relativePath"
                suffix = "#L"
            })
        }
        externalDocumentationLink(delegateClosureOf<DokkaConfiguration.ExternalDocumentationLink.Builder> {
            url = uri("https://www.slf4j.org/api/").toURL()
        })
        externalDocumentationLink(delegateClosureOf<DokkaConfiguration.ExternalDocumentationLink.Builder> {
            url = uri("http://fasterxml.github.io/jackson-databind/javadoc/2.9/").toURL()
        })
        externalDocumentationLink(delegateClosureOf<DokkaConfiguration.ExternalDocumentationLink.Builder> {
            url = uri("https://pages.hawkbot.cc/shared/javadoc/shared/").toURL()
        })
        externalDocumentationLink(delegateClosureOf<DokkaConfiguration.ExternalDocumentationLink.Builder> {
            url = uri("https://ci.dv8tion.net/job/JDA4-Alpha/javadoc/").toURL()
            packageListUrl = uri("https://gist.githubusercontent.com/DRSchlaubi/3d1d0aaa5c01963dcd4d0149c841c896/raw/22141759fbab1e38fd2381c3e4f97616ecb43fc8/package-list").toURL()
        })
    }
    val buildDir = File("build")
    "sourcesJar"(Jar::class) {
        classifier = "sources"
        destinationDir = buildDir
        from(sourceSets["main"].allSource)
    }
    "dokkaJar"(Jar::class) {
        group = JavaBasePlugin.DOCUMENTATION_GROUP
        classifier = "javadoc"
        destinationDir = buildDir
        from(tasks["dokka"])
    }
    "jar"(Jar::class) {
        destinationDir = buildDir
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_HIGHER
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}