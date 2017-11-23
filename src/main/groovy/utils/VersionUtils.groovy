package utils
import org.gradle.api.Project

/**
 * 获取版本的工具
 */
class VersionUtils{


    static def initVersion(Project project){
        def exts = project.exts
        def versions = [
                runAlone :exts(D.compileSdkVersion,false)
                ,testMavenEnv :exts(D.testMavenEnv,true)
                ,compileSdkVersion :exts(D.compileSdkVersion,"26")
                ,minSdkVersion:exts(D.minSdkVersion,"16")
                ,targetSdkVersion:exts(D.targetSdkVersion,"21")
                ,versionCode:exts(D.versionCode,"1")
                ,versionName:exts(D.versionName,"1.0")
                ,maven_url:getMavenUrl(exts)
                ,artifactId:project.name
                ,pom_version:exts(D.pom_version,"0.0.1")
        ]
        if(exts(D.printLog,false)) println("all versions = $versions")
        return versions
    }

    static String getMavenUrl(def exts){
        String maven = exts(D.maven_url,null)
        if(maven==null) maven = exts(D.testMavenEnv,true)?D.maven_test:D.maven
        return maven
    }
/**
 * 更新文件中的版本号
 * @param project
 * @param source
 * @return
 */
    static String updateVersions(Project project,String source){
        initVersion(project).findAll { map->
            source=source.replace("#${map.key}",map.value.toString())
        }
        return source
    }

    static String getAndriodTemp(Project project,boolean flavors  = false) {
        def file = project.file(project.buildDir.absolutePath+File.separator
                +"outputs"+File.separator+"dachen"+File.separator+"android.gradle")
        if(file.exists()) file.delete()
        file.parentFile.mkdirs()

        def tepmletStr = Templet.androidTemplet
        if(flavors) tepmletStr = tepmletStr.replace('//productFlavorsPosition',Templet.productFlavors)
        if(project.exts(D.mavenEnable,false)) tepmletStr = tepmletStr.replace('//mavenTemplet',Templet.mavenTemplet)

        tepmletStr = VersionUtils.updateVersions(project, tepmletStr)


        BufferedOutputStream out = file.newOutputStream()
        out.write(tepmletStr.getBytes())
        out.flush()
        out.close()
        def path = file.absolutePath
        return path
    }



}