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

    @Override
    void apply(Project project) {
        addProjectExt(project)
        println("onIn---------------------")
        DachenModel model = project.exts("dachen",new DachenModel())
        println("model = $model")
        project.repositories {
            maven {
                url VersionUtils.getMavenUrl(model.testMavenEnv)
            }
        }

        applyAndroidVerions(project, true)
        project.apply from: PluginUtils.androidGradlePath(project)
    }
/**
 * 添加获取ext属性的方法
 * @param proj
 */
    void addProjectExt(Project proj){
        def getExt = { key,value = "" ->
            return project.hasProperty(key)?project.ext.get(key):value
        }
        getExt.delegate = proj
        proj.ext.exts = getExt
    }

    abstract void applyAndroidVerions(Project project, boolean library)
}
