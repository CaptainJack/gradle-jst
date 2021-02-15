package ru.capjack.gradle.jst

import com.moowork.gradle.node.NodePlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import java.io.File

class JstPlugin : Plugin<Project> {
	override fun apply(project: Project) {
		project.pluginManager.apply(BasePlugin::class)
		project.pluginManager.apply(NodePlugin::class)
		
		project.extensions.create<JstExtension>("jst", project)
		
		val tasks = project.tasks
		val assembleTask = tasks.named("assemble")
		
		project.file("tsconfig.json").takeIf(File::exists)?.also { file ->
			val taskEnsureTypeScriptCompiler = tasks.register<EnsureTypeScriptCompilerTask>("ensureTypeScriptCompiler")
			val taskCompileTypeScript = tasks.register<CompileTypeScriptTask>("compileTypeScript", file)
			taskCompileTypeScript.configure { dependsOn(taskEnsureTypeScriptCompiler) }
			assembleTask.configure { dependsOn(taskCompileTypeScript) }
		}
		
		val taskGeneratePackageJson = tasks.register<GeneratePackageJsonTask>("generatePackageJson")
		assembleTask.configure { dependsOn(taskGeneratePackageJson) }
		
		val taskPublishNpmPackage = tasks.register<PublishNpmPackageTask>("publishNpmPackage") {
			this.dependsOn(tasks.named("build"))
		}
		
		project.pluginManager.withPlugin("nebula.release") {
			tasks.named("release").configure { dependsOn(taskPublishNpmPackage) }
		}
	}
}
