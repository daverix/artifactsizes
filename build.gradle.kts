plugins {
    `java-gradle-plugin`
    `maven-publish`
    kotlin("jvm") version("1.3.31")
}

group = "net.daverix.artifactsizes"
version = "0.1"

dependencies {
    implementation(gradleKotlinDsl())
}

gradlePlugin {
    plugins {
        register("artifactsizes") {
            id = "net.daverix.artifactsizes"
            implementationClass = "net.daverix.artifactsizes.ArtifactSizesPlugin"
        }
    }
}

publishing {
    publications {
        register<MavenPublication>("plugin") {
            from(components["java"])
            artifactId = "plugin"
        }
    }
}

repositories {
    google()
    jcenter()
}
