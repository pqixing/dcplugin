package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import tasks.UploadTask

class Upload implements Plugin<Project>{

    @Override
    void apply(Project target) {
        target.task(upload,type:UploadTask)
    }
}