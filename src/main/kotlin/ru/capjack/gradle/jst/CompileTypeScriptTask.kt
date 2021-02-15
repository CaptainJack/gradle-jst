package ru.capjack.gradle.jst

import com.moowork.gradle.node.NodeExtension
import com.moowork.gradle.node.exec.NodeExecRunner
import com.moowork.gradle.node.npm.NpmInstallTask
import groovy.json.JsonSlurper
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType
import java.io.File
import javax.inject.Inject

@Suppress("UnstableApiUsage", "LeakingThis", "UNCHECKED_CAST")
open class CompileTypeScriptTask @Inject constructor(
	@InputFile val configFile: File
) : DefaultTask() {
	
	@OutputDirectory
	val outputSourceDir = project.objects.directoryProperty().convention(
		project.extensions.getByType<JstExtension>().publicationDir.dir("src")
	)
	
	@OutputDirectory
	val outputTypesDir = project.objects.directoryProperty().convention(
		project.extensions.getByType<JstExtension>().publicationDir.dir("types")
	)
	
	init {
		group = "jst"
		dependsOn(NpmInstallTask.NAME)
		
		val config = JsonSlurper().parse(configFile) as Map<String, Any>
		val include = config["include"] as List<String>
		
		for (path in include) {
			inputs.dir(configFile.resolveSibling(path))
		}
	}
	
	@TaskAction
	fun execute() {
		val outputSourceDir = outputSourceDir.get().asFile
		val outputTypesDir = outputTypesDir.get().asFile
		
		project.delete(outputSourceDir, outputTypesDir)
		
		val runner = NodeExecRunner(project)
		
		runner.arguments = listOf(
			project.projectDir.resolve("node_modules/.bin/tsc").absolutePath,
			"--project", configFile.absolutePath,
			"--outDir", outputSourceDir.absolutePath,
			"--declarationDir", outputTypesDir.absolutePath,
			"--declaration", "true",
			"--removeComments", "true"
		)
		
		runner.execute()
	}
	
}