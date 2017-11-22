package plugins

import org.gradle.api.Project
/**
 * Created by pqixing on 17-11-21.
 */

class Application extends BasePlugin {
    @Override
    void apply(Project project) {
        super.apply(project)
    }

    @Override
    void applyAndroidVerions(Project project, boolean library) {
        project.apply plugin: 'com.android.application'
    }
}
