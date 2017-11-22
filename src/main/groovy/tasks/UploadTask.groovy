package tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class UploadTask extends DefaultTask{
   UploadTask(){
       super()
       group = "upload"
       description = "dachen maven upload task"
       mustRunAfter project.build
   }

    @TaskAction
    void uploadToNexus(){
        println("upload to nexus"+project.name)
    }
}