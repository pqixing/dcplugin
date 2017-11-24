package plugins

import org.gradle.api.Project
import utils.*

/**
 * Created by pqixing on 17-11-21.
 */

class Library extends BasePlugin {
    @Override
    void apply(Project project) {
        super.apply(project)
    }

    @Override
    boolean isLibraryPlugin() {
        return true
    }

    @Override
    void applyAndroid(Project project) {
        def asApp = project.exts(D.asApp,false)
        project.apply plugin: (asApp?'com.android.application':'com.android.library')
        project.apply from : VersionUtils.generatorGradle(project)
        if(asApp){
            VersionUtils.generatorApplication(project)
            VersionUtils.generatorActivity(project)
            VersionUtils.generatorManifeast(project)
        }
    }
}
