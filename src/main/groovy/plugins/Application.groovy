package plugins

import org.gradle.api.Project
import utils.*

/**
 * Created by pqixing on 17-11-21.
 */

class Application extends BasePlugin {
    @Override
    void apply(Project project) {
        super.apply(project)
    }

    @Override
    boolean isLibraryPlugin() {
        return false
    }

    @Override
    void applyAndroid(Project project) {
        project.apply plugin: 'com.android.application'
        project.apply from : VersionUtils.generatorGradle(project)
    }

}
