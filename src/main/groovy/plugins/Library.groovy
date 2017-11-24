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
       def runAlone = false
        project.apply plugin: (runAlone?'com.android.application':'com.android.library')
        project.apply from : VersionUtils.generatorGradle(project)
//        if(project.exts(D.asApp,false)){
            VersionUtils.generatorApplication(project)
            VersionUtils.generatorManifeast(project)
//        }
    }
}
