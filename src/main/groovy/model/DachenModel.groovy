package model

import org.gradle.api.Project
import utils.PluginUtils

/**
 * 大辰相关配置
 */
class DachenModel {
    Project p
    public DachenModel(Project project){
        p = project
        PluginUtils.addProjectExt(project)
    }
    /**
     * 仓库的环境配置
     */
    boolean testMavenEnv = p.exts("testMavenEnv",true)

    String compileSdkVersion = p.exts("compileSdkVersion","26")
    String minSdkVersion = p.exts("minSdkVersion","16")
    String targetSdkVersion = p.exts("targetSdkVersion","21")
    String versionCode = p.exts("versionCode","1")
    String versionName = p.exts("versionName","1.0")
    
    //上传到maven仓库的版本
    String pom_version =p.exts("pom_version","0.0.1")

    boolean runAlone = p.exts("runAlone",true)
    Component component



    static void init(Project project, Closure call) {
        def m = new DachenModel(project)
        call.delegate = m
        call.setResolveStrategy(Closure.DELEGATE_ONLY)
        call.call()
        project.ext.dachen = m
    }

    @Override
    public String toString() {
        return "DachenModel{" +
                "testMavenEnv=" + testMavenEnv +
                ", compileSdkVersion='" + compileSdkVersion + '\'' +
                ", minSdkVersion='" + minSdkVersion + '\'' +
                ", targetSdkVersion='" + targetSdkVersion + '\'' +
                ", versionCode='" + versionCode + '\'' +
                ", versionName='" + versionName + '\'' +
                ", pom_version='" + pom_version + '\'' +
                ", component=" + component +
                '}';
    }
}