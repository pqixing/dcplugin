package utils

import auto.Configs
import auto.TP
import org.gradle.api.Project

class Generator {
    /**
     * 输出文件
     * @param file
     * @param data
     */
    static String write(File file, String data) {
        if (file.exists()) file.delete()
        file.parentFile.mkdirs()
        BufferedOutputStream out = file.newOutputStream()
        out.write(data.getBytes())
        out.flush()
        out.close()
        return file.path
    }

    /**
     * 输出AndroidGradle文件
     * @param project
     * @return
     */
    static String writeAndroidGradle(Project project) {
        def exts = project.exts
        def gradleStr = TP.androidGradle
        gradleStr = ProUtils.replaceKey(project, gradleStr, Configs.flavorsEnable, exts(Configs.flavorsEnable)==true? TP.flavors : "")
        gradleStr = ProUtils.replaceKey(project, gradleStr, "sourceSetEnable", !project.app && exts(Configs.asApp)==true ? TP.sourceSet : "")
        gradleStr = ProUtils.replaceKey(project, gradleStr, Configs.uploadEnable, CheckUtils.isUploadAble(project) ? TP.maven : "")

        return write(new File(exts(Configs.outDir), "androidConfigs.gradle"), ProUtils.replaceAllKey(project, gradleStr))
    }
    /**
     * 创建清单文件
     * @param project
     * @return
     */
    static String writeManifest(Project project) {
        def inputManifest = new File(MethodUtils.getUrl(project.projectDir.path, "src", "main"), "AndroidManifest.xml").text
        def result = inputManifest.replaceFirst("<manifest(?s).*?>", ProUtils.replaceAllKey(project, TP.manifestMeta))
                .replaceFirst("<application(?s).*?>", ProUtils.replaceAllKey(project, TP.manifestApp))
        return write(new File(project.exts(Configs.outDir), "AndroidManifest.xml"), result)
    }
    /**
     * 创建Applicaiton类
     * @param project
     * @return
     */
    static String writeApplication(Project project) {
        def exts = project.exts
        def dirPath = MethodUtils.getUrl(exts(Configs.outDir), "java", exts(Configs.packageName).replace('.', File.separator))
        return write(new File(dirPath, "DefaultAppCation.java"), ProUtils.replaceAllKey(project, TP.application))
    }
    /**
     * 创建Activity类
     * @param project
     * @return
     */
    static String writeActivity(Project project) {
        def exts = project.exts
        def dirPath = MethodUtils.getUrl(exts(Configs.outDir), "java", exts(Configs.packageName).replace('.', File.separator))
        return write(new File(dirPath, "DefaultActivity.java"), ProUtils.replaceAllKey(project, TP.activity))
    }
    /**
     * 输出依赖关系
     * @param project
     * @return
     */
    static void writeDependeny(Project project) {
        project.task("dependency", type: org.gradle.api.tasks.diagnostics.DependencyReportTask) {
            group = "android"
            outputFile = project.file(project.exts(Configs.outDir) + File.separator + "dependency.txt")
            doLast {
                def strList = new LinkedList<String>()
                outputFile.eachLine {
                    if (it.startsWith("No dependencies")) {
                        strList.removeLast()
                        strList.removeLast()
                    } else {
                        strList.add(it + "\n")
                    }
                }
                write(outputFile, strList.toString())
            }
        }
        project.dependency.execute()
    }

    /**
     * 输出配置文件信息，返回输出路径
     * @param project
     * @return
     */
    static String writeConfigs(Project project) {
        def exts = project.exts
        def configsFile = project.file(exts(Configs.outDir) + File.separator + "config.txt")

        def configsStr = new StringBuilder()
        Configs.properties.keySet().sort { it }.each { key ->
            configsStr.append("$key : ${key.startsWith('s_') ? 'Not Public Properties!!!' : exts(key)} \n")
        }
        configsStr.append("--------------------------------android configs--------------------------------\n")

        def variantClosures = { variant ->
            configsStr.append("--------------------------------variant name = $variant.name --------------------------------\n")
            configsStr.append("applicationId : $variant.applicationId\n")
        }
        if ("application" == exts(Configs.plugin_type))
            project.android.applicationVariants.all(variantClosures)
        else project.android.libraryVariants.all(variantClosures)

//        println("configsStr = $configsStr")
        return write(configsFile, configsStr.toString())
    }
}