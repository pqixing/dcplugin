package plugins

import auto.Configs
import org.gradle.api.Project
import utils.Generator

/**
 * Created by pqixing on 17-11-21.
 */

class Library extends BasePlugin {
    @Override
    void apply(Project project) {
        project.ext.app = false
        super.apply(project)
    }

    @Override
    void applyForChildren(Project project) {
        def asApp = project.exts(Configs.asApp)
        if (asApp) {
            Generator.writeApplication(project)
            Generator.writeActivity(project)
            Generator.writeManifest(project)
        }
    }
}
