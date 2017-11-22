package utils

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
        def versions = ["compileSdkVersion":"26"
        ,"minSdkVersion":"16"
        ,"targetSdkVersion":"21"
        ,"versionCode":"1"
        ,"versionName":"1.0"
        ,"maven_url":getMavenUrl(project.dachen.testEnv)
//        ,"maven_url":'uri("/home/pqixing/.repo")'
        ,"artifactId":project.name
        ,"pom_version":project.dachen.pom_version
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
        def ext = project.rootProject.extensions
        initVersions(project).findAll { map->
            source=source.replace("#${map.key}",ext.hasProperty(map.key)?ext.getByName(map.key):map.value)
        }
        return source
    }
   static String getVersion(Gradle gradle,String module){
        return "com.dachen.android:commom:+"
    }
}