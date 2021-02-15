plugins {
	`kotlin-dsl`
	`java-gradle-plugin`
	`maven-publish`
	id("com.gradle.plugin-publish") version "0.12.0"
	id("nebula.release") version "15.3.1"
}

group = "ru.capjack.gradle"

repositories {
	jcenter()
}

dependencies {
	implementation("com.moowork.gradle:gradle-node-plugin:1.3.1")
}

gradlePlugin {
	plugins.create("jst") {
		id = "ru.capjack.jst"
		implementationClass = "ru.capjack.gradle.jst.JstPlugin"
	}
}

pluginBundle {
	vcsUrl = "https://github.com/CaptainJack/gradle-jst"
	website = vcsUrl
	tags = listOf("capjack", "js", "tool", "jst")
}

tasks["postRelease"].dependsOn("publishPlugins")
