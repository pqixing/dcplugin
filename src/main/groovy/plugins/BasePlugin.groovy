package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import utils.VersionUtils

/**
 * Created by pqixing on 17-11-21.
 */

abstract class BasePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        addProjectExt(project)
        project.repositories {
            maven {
                url VersionUtils.getMavenUrl(project.exts)
            }
        }
        applyAndroid(project)
    }

    abstract void applyAndroid(Project project)
/**
 * 添加获取ext属性的方法
 * @param proj
 */
    void addProjectExt(Project proj){
        def getExt = { key,value = null ->
            return project.hasProperty(key)?project.ext.get(key):value
        }
        getExt.delegate = proj
        proj.ext.exts = getExt
    }
}
