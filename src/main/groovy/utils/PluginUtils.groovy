package utils

import org.gradle.api.Project

/**
 * Created by pqixing on 17-11-21.
 */

class PluginUtils {
    static void addProjectExt(Project proj){
//        if(proj.hasProperty("exts")) return

        def getExt = { key,value = "" ->
            return project.hasProperty(key)?project.ext.get(key):value
        }
        getExt.delegate = proj
        proj.ext.exts = getExt
    }

/**
 * 打印工程的依赖关系图
 * @param project
 * @param model 输出路径，不传则使用project的build目录
 */
    static void printDependencies(Project project, String path) {
        println("输出依赖关系" + project.name)
    }

    static String androidGradlePath(Project project) {
        def file = project.file(project.buildDir.absolutePath+File.separator
                +"out"+File.separator+"dachen"+File.separator+"android.gradle")
        if(file.exists()) file.delete()
        file.parentFile.mkdirs()

        BufferedOutputStream out = file.newOutputStream()
        def versions = VersionUtils.updateVersions(project, androidGradle())
        out.write(versions.getBytes())
        out.flush()
        out.close()
        def path = file.absolutePath
        return path
    }

/**
 * 返回默认的gradle文件
 * @return
 */
    static String androidGradle() {
        return '''apply plugin: "maven"
android {
    compileSdkVersion #compileSdkVersion
    defaultConfig {
        minSdkVersion #minSdkVersion
        targetSdkVersion #targetSdkVersion

        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro\'
        }
    }

}
// 上传到本地代码库
uploadArchives{
    repositories{
        mavenDeployer{
            repository(url:"#maven_url"){
                authentication(userName: "admin", password: "admin123")
            }
            pom.groupId = 'com.dachen.android' // 组名
            pom.artifactId = '#artifactId' // 插件名
            pom.version = '#pom_version' // 版本号
        }
    }
}
        '''
    }
}
