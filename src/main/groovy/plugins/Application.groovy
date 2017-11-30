package plugins

import org.gradle.api.Project
/**
 * Created by pqixing on 17-11-21.
 */

class Application extends BasePlugin {
    @Override
    void apply(Project project) {
        project.ext.app = true
        super.apply(project)
    }


    @Override
    void applyForChildren(Project project) {
    }

}
