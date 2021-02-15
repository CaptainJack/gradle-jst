package ru.capjack.gradle.jst

import com.moowork.gradle.node.npm.NpmExecRunner
import com.moowork.gradle.node.npm.NpmSetupTask
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.property

@Suppress("LeakingThis", "UnstableApiUsage")
open class PublishNpmPackageTask() : DefaultTask() {
	
	val packageDir = project.objects.directoryProperty().convention(
		project.extensions.getByType<JstExtension>().publicationDir
	)
	
	val npmRegistry = project.objects.property<String>().convention(
		project.extensions.getByType<JstExtension>().npmRegistry
	)
	
	val npmToken = project.objects.property<String>().convention(
		project.extensions.getByType<JstExtension>().npmToken
	)
	
	init {
		group = "jst"
		dependsOn(NpmSetupTask.NAME)
	}
	
	@TaskAction
	fun execute() {
		val runner = NpmExecRunner(project)
		runner.workingDir = packageDir.get().asFile
		
		val registry = npmRegistry.get().trimEnd('/') + '/'
		
		runner.arguments = listOf(
			"publish",
			"--access", "public",
			"--registry", registry,
			"--//" + registry.substringAfter("://") + ":_authToken=" + npmToken.get()
		)
		
		runner.execute()
	}
}