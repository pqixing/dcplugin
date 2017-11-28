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
        boolean islibrary = isLibraryPlugin()
        project.ext.isLibrary = islibrary

        VersionUtils.initProject(project)
        VersionUtils.checkProperties(project)

        applyAndroid(project)
    }

    abstract boolean isLibraryPlugin()

    abstract void applyAndroid(Project project)

}