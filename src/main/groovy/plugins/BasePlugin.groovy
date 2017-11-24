package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import utils.*

/**
 * Created by pqixing on 17-11-21.
 */

abstract class BasePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        addProjectExt(project)
        boolean islibrary = isLibraryPlugin()
        project.ext.isLibrary = islibrary
        VersionUtils.initProject(project)

        project.repositories {
            maven {
                url VersionUtils.getMavenUrl(project.exts)
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
    void addProjectExt(Project proj){
        def getExt = { key,value = null ->
            return project.hasProperty(key)?project.ext.get(key):value
        }
        getExt.delegate = proj
        proj.ext.exts = getExt

        def fromRepo = {key,value = null ->
            if(value==null) {
                value = getExt(D.repoVerions, [:])[key]
            }
            if(value==null){
                value = "+"
            }
            return "com.dachen.android:$project.name:$value"
        }
        fromRepo.delegate = proj
        proj.ext.fromRepo = fromRepo
    }
}
