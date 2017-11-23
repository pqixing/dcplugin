package model
import org.gradle.api.Project
/**
 * 大辰相关配置
 */
class DachenModel {
    /**
     * 仓库的环境配置
     */
    boolean testMavenEnv = true

    String compileSdkVersion = "26"
    String minSdkVersion = "16"
    String targetSdkVersion = "21"
    String versionCode = "1"
    String versionName = "1.0"
    
    //上传到maven仓库的版本
    String pom_version ="0.0.1"

    boolean runAlone = true
    Component component



    static void init(def container, Closure call) {
        Project project =container instanceof Project?container: container.project
        println("prname = $project.name")
        def m = new DachenModel()
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