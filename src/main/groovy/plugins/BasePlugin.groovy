package plugins

import auto.Configs
import org.gradle.api.Plugin
import org.gradle.api.Project
import utils.*

/**
 * Created by pqixing on 17-11-21.
 */

abstract class BasePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        VersionUtils.initProject(project)
        addProjectExt(project)

        def mUrl = VersionUtils.getMavenUrl(project.exts)
        project.defConfigs[auto.Configs.maven_url] = mUrl
        boolean islibrary = isLibraryPlugin()

        project.ext.isLibrary = islibrary

        project.repositories {
            maven {
                url mUrl
            }
        }
        applyAndroid(project)
    }

    abstract boolean isLibraryPlugin()

    abstract void applyAndroid(Project project)
/**
 * 添加获取ext属性的方法
 * @param proj
 */
    void addProjectExt(Project proj) {
        def getExt = { key, value = null ->
            if (project.hasProperty(key)) return project.ext.get(key)
            else if (project.defConfigs[key]!=null) return project.defConfigs[key]
            else return value
        }

        getExt.delegate = proj
        proj.ext.exts = getExt

        def fromRepo = { key, value = null ->
            if (value == null) {
                value = getExt(D.repoVerions)[key]
            }
            if (value == null) {
                value = "+"
            }
            return "${getExt(Configs.packagePrefix)}.android:$project.name:$value"
        }
        fromRepo.delegate = proj
        proj.ext.fromRepo = fromRepo
    }
}
