package utils

import model.DachenModel
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle
/**
 * 获取版本的工具
 */
class VersionUtils{
/**
 * 获取maven仓库的地址
 * @param test 测试环境使用
 * @return
 */
    static String getMavenUrl(boolean testEnv) {
        return testEnv ? "http://192.168.3.7:9527/nexus/content/repositories/android-test/"
                : "http://192.168.3.7:9527/nexus/content/repositories/android/"
    }

    static def initVersions(Project project){
       DachenModel model = project.exts("dachen",new DachenModel())
        def versions = ["compileSdkVersion":model.compileSdkVersion
        ,"minSdkVersion":model.minSdkVersion
        ,"targetSdkVersion":model.targetSdkVersion
        ,"versionCode":model.versionCode
        ,"versionName":model.versionName
        ,"maven_url":getMavenUrl(model.testMavenEnv)
        ,"artifactId":project.name
        ,"pom_version":model.pom_version
        ]
        return versions
    }

/**
 * 更新文件中的版本号
 * @param project
 * @param source
 * @return
 */
     static String updateVersions(Project project,String source){
        initVersions(project).findAll { map->
            source=source.replace("#${it.key}",it.value)
        }
        return source
    }
   static String getVersion(Gradle gradle,String module){
        return "com.dachen.android:commom:+"
    }
}