package ru.capjack.gradle.jst

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.property

@Suppress("UnstableApiUsage", "UNCHECKED_CAST")
open class GeneratePackageJsonTask : DefaultTask() {
	
	@Input
	val version = project.objects.property<String>().convention(
		project.provider { project.version.toString() }
	)
	
	@InputFile
	val inputFile = project.objects.fileProperty().convention(
		project.layout.projectDirectory.file("package.json")
	)
	
	@OutputFile
	val outputFile = project.objects.fileProperty().convention(
		project.extensions.getByType<JstExtension>().publicationDir.file("package.json")
	)
	
	init {
		group = "jst"
	}
	
	@TaskAction
	fun execute() {
		val data = JsonSlurper().parse(inputFile.get().asFile) as MutableMap<String, Any>
		
		data.remove("devDependencies")
		data.remove("scripts")
		
		data["version"] = version.get()
		
		outputFile.get().asFile.writeText(JsonBuilder(data).toPrettyString())
	}
}