package ru.capjack.gradle.jst

import com.moowork.gradle.node.NodeExtension
import com.moowork.gradle.node.npm.NpmExecRunner
import com.moowork.gradle.node.npm.NpmSetupTask
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType

@Suppress("UnstableApiUsage", "LeakingThis")
open class EnsureTypeScriptCompilerTask : DefaultTask() {
	
	init {
		group = "jst"
		dependsOn(NpmSetupTask.NAME)
		outputs.upToDateWhen { task ->
			task.project.projectDir.let {
				it.resolve("node_modules/.bin/tsc").exists() && it.resolve("package.json").exists()
			}
		}
	}
	
	@TaskAction
	fun execute() {
		project.projectDir.resolve("package.json").takeIf { !it.exists() }?.also { it.writeText("{}") }
		
		val runner = NpmExecRunner(project)
		runner.arguments = listOf("install", "--save-dev", "typescript")
		runner.execute()
	}
}