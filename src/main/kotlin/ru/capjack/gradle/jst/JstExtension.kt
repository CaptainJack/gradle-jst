package ru.capjack.gradle.jst

import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property

@Suppress("UnstableApiUsage")
open class JstExtension(project: Project) {
	val npmToken: Property<String> = project.objects.property<String>().convention(
		project.providers.gradleProperty("capjack.jst.npmToken")
	)
	
	val npmRegistry: Property<String> = project.objects.property<String>().convention(
		project.providers.gradleProperty("capjack.jst.npmRegistry").orElse("https://registry.npmjs.org")
	)
	
	val publicationDir: DirectoryProperty = project.objects.directoryProperty().convention(
		project.layout.buildDirectory.dir("publications/npm")
	)
}

