package plugins

import auto.Configs
import org.gradle.api.Plugin
import org.gradle.api.Project
import tasks.GeneratorReport
import utils.ProUtils
import utils.VersionUtils
/**
 * Created by pqixing on 17-11-21.
 */

abstract class BasePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        boolean islibrary = isLibraryPlugin()
        project.ext.isLibrary = islibrary
        ProUtils.initProperties(project)

        VersionUtils.initProject(project)
        VersionUtils.checkProperties(project)

//        project.apply from : VersionUtils.generatorBuildScript(project)

        applyAndroid(project)
        project.afterEvaluate {
            project.task("GeneratorReport", type: GeneratorReport) {
                group = "android"
                outputFile = project.file(project.exts(Configs.outDir) + File.separator + "dpReport.txt")
                doLast {
                    removeNoDependencies(outputFile)
                }
            }
            project.GeneratorReport.execute()
            outConfigs(project)


        }
    }

    void removeNoDependencies(File outputFile) {
        def strList = new LinkedList<String>()
        outputFile.eachLine {
            if (it.startsWith("No dependencies")) {
                strList.removeLast()
                strList.removeLast()
            } else {
                strList.add(it + "\n")
            }
        }
        VersionUtils.generatorFile(outputFile, strList.toListString())
    }

    void outConfigs(Project project) {
        def configsFile = project.file(project.exts(Configs.outDir) + File.separator + "configs.txt")
        def configsStr = new StringBuilder()
        Configs.properties.sort { it.key }.each { map ->
            if (map.key == Configs.maven_password || map.key == Configs.maven_userName) return
            configsStr.append("$map.key : ${project.exts(map.key)} \n")
        }
        configsStr.append("--------------------------------android configs--------------------------------\n")

        def variantClosures = { variant ->
            configsStr.append("--------------------------------variant name = $variant.name --------------------------------\n")
            configsStr.append("applicationId : $variant.applicationId\n")
        }
        if (!project.isLibrary || project.exts(Configs.asApp))
            project.android.applicationVariants.all(variantClosures)
        else project.android.libraryVariants.all(variantClosures)
//        println("configsStr = $configsStr")
        VersionUtils.generatorFile(configsFile, configsStr.toString())
    }

    abstract boolean isLibraryPlugin()

    abstract void applyAndroid(Project project)

}