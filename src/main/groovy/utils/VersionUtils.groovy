package utils

import auto.Configs
import org.gradle.api.Project

/**
 * 获取版本的工具
 */
class VersionUtils {

    static void checkProperties(Project project) {
        def exts = project.exts
        if (exts(Configs.uploadEnable)) {
            if (exts(Configs.pom_version) == null) {
                throw new RuntimeException("project $project.name uploadEnable is true,you have to set pom_version for library!")
            }
        }
        if (project.isLibrary && exts(Configs.asApp)) {
            if (exts(Configs.launchActivity) == null) {
                throw new RuntimeException("project $project.name asApp is true,you have to set launchActivity!")
            }
            if (exts(Configs.app_icon) == null) {
                throw new RuntimeException("project $project.name asApp is true,you have to set app_icon!")
            }
        }
    }

    static String getUrl(String[] strs) {
        def result = new StringBuilder()
        strs.each {
            result.append("$it$File.separator")
        }
        return result.substring(0, result.size() - 1)
    }

    static void initProject(Project project) {
        def defConfigs = [:]
        Configs.properties.each { map ->
            if (map.value instanceof String) {
                defConfigs[map.key] = map.value.toString()
                        .replace("#projectName", project.name)
                        .replace("#projectDir", project.projectDir.path)
            } else {
                defConfigs[map.key] = map.value
            }
        }
        defConfigs[Configs.packageName] = defConfigs[Configs.packageName].toString().replace("#groupName", defConfigs[Configs.groupName])
        project.ext.defConfigs = defConfigs
        addProjectExt(project)
        defConfigs[Configs.packageName] = defConfigs[Configs.packageName].toString().replace("#groupName", project.exts(Configs.groupName))

        def mUrl = utils.VersionUtils.getMavenUrl(project.exts)
        project.defConfigs[auto.Configs.maven_url] = mUrl

        project.repositories {
            maven {
                url mUrl
            }
        }
    }
    /**
     * 添加获取ext属性的方法
     * @param proj
     */
    static void addProjectExt(Project proj) {
        def getExt = { key, value = null ->
            if (project.hasProperty(key)) return project.ext.get(key)
            else if (project.defConfigs[key] != null) return project.defConfigs[key]
            else return value
        }

        getExt.delegate = proj
        proj.ext.exts = getExt

        def fromRepo = { key, value = null ->
            key = key.replace(":","")

            if (value == null) {
                value = getExt(Configs.repoVersions)[key]
            }
            if (value == null) {
                value = "+"
            }
            return "${getExt(Configs.groupName)}.android:$key:$value"
        }
        fromRepo.delegate = proj
        proj.ext.fromRepo = fromRepo
    }

//创建Applicaiton类
    static String generatorApplication(Project project) {
        def exts = project.exts
        def appTempStr = Templet.applicationTemplet.replace("#packageName", exts(Configs.packageName))
        def applicationLike = exts(Configs.applicationLike)
//        println("applicationLike = $applicationLike")
        if (applicationLike != null&&applicationLike!="") appTempStr = appTempStr.replace('//ApplicationLike', "new $applicationLike()")

        def applicatinPath = project.file(getUrl(exts(Configs.outDir), "java", exts(Configs.packageName).replace('.', File.separator), "DefaultAppCation.java"))
        generatorFile(applicatinPath, appTempStr)
    }

//创建Activity类
    static void generatorActivity(Project project) {
        def exts = project.exts
        def activityTempStr = Templet.activityTemplet.replace("#packageName", exts(Configs.packageName)).replace("#launchActivity", exts(Configs.launchActivity))
        def activityPath = project.file(getUrl(exts(Configs.outDir), "java", exts(Configs.packageName).replace('.', File.separator), "DefaultActivity.java"))
        generatorFile(activityPath, activityTempStr)
    }

//创建manifest
    static String generatorManifeast(Project project) {
        def exts = project.exts

        def app = Templet.manifestApplicaion.replace("#packageName", exts(Configs.packageName))
                .replace("#app_icon", exts(Configs.app_icon))
                .replace("#app_name", exts(Configs.app_name))
        def theme = exts(Configs.app_theme)
        app = app.replace("#app_theme", theme == null ? "" : "android:theme=\" $theme\"")

        def meta = Templet.manifestMeta.replace('#packageName', exts(Configs.packageName))
                .replace("#${Configs.versionName}", exts(Configs.versionName))
                .replace("#${Configs.versionCode}", exts(Configs.versionCode))

        def manifestText = project.file(getUrl(project.projectDir.path, "src", "main", "AndroidManifest.xml"))
                .text.replaceFirst("<manifest(?s).*?>", meta).replaceFirst("<application(?s).*?>", app)

        def outFile = project.file(getUrl(exts(Configs.outDir), "AndroidManifest.xml"))
        generatorFile(outFile, manifestText)

    }

    static void generatorFile(File file, String data) {
        if (file.exists()) file.delete()
        file.parentFile.mkdirs()
        file.parentFile.mkdirs()
        BufferedOutputStream out = file.newOutputStream()
        out.write(data.getBytes())
        out.flush()
        out.close()
    }
    static String generatorBuildScript(Project project) {
        def exts = project.exts
        def file = project.file(getUrl(exts(Configs.outDir), "androiBuildScript.gradle"))
        def script = Templet.buildScripTemple.replace("#gradle_version",project.exts(Configs.gradle_version))
        generatorFile(file, script)
        return file.absolutePath
    }

    static String generatorGradle(Project project) {
        def exts = project.exts
        def file = project.file(getUrl(exts(Configs.outDir), "androiConfigs.gradle"))

        def tepmletStr = Templet.androidTemplet
        if (exts(Configs.flavorsEnable)) tepmletStr = tepmletStr.replace('//productFlavorsPosition', Templet.productFlavors)

        //是否允许上传到仓库
        def uploadEnable = exts(Configs.uploadEnable)&&(exts(Configs.env)!="release"||(Configs.dc_module.contains(project.name)&&exts(Configs.maven_dc_key)=="dc_release"))

        if (uploadEnable) tepmletStr = tepmletStr.replace('//mavenTemplet', Templet.mavenTemplet)
        if (project.isLibrary && exts(Configs.asApp)) tepmletStr = tepmletStr.replace('  //sourceSet', Templet.sourceSet).replace("#outDir", exts(Configs.outDir))
        tepmletStr = VersionUtils.updateGradleProperties(project, tepmletStr)
        generatorFile(file, tepmletStr)
        def path = file.absolutePath
        return path
    }


    static def getProperties(Project project) {
        def versions = project.defConfigs.findAll { map ->
            map.key == Configs.compileSdkVersion || map.key == Configs.minSdkVersion ||
                    map.key == Configs.targetSdkVersion || map.key == Configs.maven_url ||
                    map.key == Configs.artifactId || map.key == Configs.pom_version || map.key == Configs.groupName
        }
        return versions
    }

    static String getMavenUrl(def exts) {
        String maven = exts(Configs.maven_url)
        if (maven == null) maven = Configs.properties["release" == exts(Configs.env) ? Configs.maven_url_release : Configs.maven_url_test]
        return maven
    }
/**
 * 更新文件中的版本号
 * @param project
 * @param source
 * @return
 */
    static String updateGradleProperties(Project project, String source) {
        def exts = project.exts
        def maps = getProperties(project)
        maps.findAll { map ->
            def value = exts(map.key)
            if (value != null) source = source.replace("#${map.key}", value)
        }
        return source
    }

}