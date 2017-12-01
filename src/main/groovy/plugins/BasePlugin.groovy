package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import utils.Generator
import utils.ProUtils
import utils.CheckUtils
/**
 * Created by pqixing on 17-11-21.
 */

abstract class BasePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        ProUtils.initProperties(project)
        CheckUtils.checkProperties(project)

        project.apply from: Generator.writeAndroidGradle(project)
        applyForChildren(project)

        project.afterEvaluate {
            Generator.writeDependeny(project)
            Generator.writeConfigs(project)
        }
    }

    abstract void applyForChildren(Project project)

}