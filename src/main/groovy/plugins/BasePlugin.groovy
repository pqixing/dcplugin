package plugins

import model.DachenModel
import org.gradle.api.Plugin
import org.gradle.api.Project
import utils.PluginUtils
import utils.VersionUtils

/**
 * Created by pqixing on 17-11-21.
 */

abstract class BasePlugin implements Plugin<Project> {
    DachenModel model;
    @Override
    void apply(Project project) {
        if(model==null) {
            model = project.extensions.create("dachen", DachenModel)
            project.afterEvaluate {
                println("name :$project.name properties: $project.dachen")
                apply(project)
            }
            return
        }

        project.repositories {
            maven {
                url VersionUtils.getMavenUrl(model.testEnv)
            }
        }
        applyAndroidVerions(project,model.library)

        project.task("atest"){
            doFirst {
                println(model)
            }
        }
        project.apply from : PluginUtils.androidGradlePath(project)
    }

    abstract void applyAndroidVerions(Project project,boolean library)
}
