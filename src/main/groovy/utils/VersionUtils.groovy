package utils
import org.gradle.api.Project

/**
 * 获取版本的工具
 */
class VersionUtils{

    static String getUrl(String[] strs){
        def result = new StringBuilder()
        strs.each {
            result.append("$it$File.separator")
        }
        return result.substring(0,result.size()-1)
    }

    static void initProject(Project project){
        project.ext.maven_userName = "admin"
        project.ext.maven_password = "admin123"
        project.ext.defaultDir = getUrl(project.buildDir.path,"outputs","dachen")
        project.ext.packageName = "com.dachen.$project.name"
    }
    //创建Applicaiton类
    static String generatorApplication(Project project){
        def appTempStr = Templet.getApplicationTemplet().replace("#packageName", project.packageName)
        def applicationLike =project.exts(D.applicationLike,null)
//        println("applicationLike = $applicationLike")
        if(applicationLike!=null)appTempStr = appTempStr.replace('//ApplicationLike',"new $applicationLike()")

       def applicatinPath =  project.file(getUrl(project.exts(D.outDir,project.defaultDir),"java",project.packageName.replace('.',File.separator),"DefaultAppCation.java"))
        generatorFile(applicatinPath,appTempStr)
    }


    //创建manifest
    static String generatorManifeast(Project project){
       def manifestText =  project.file(getUrl(project.projectDir.path, "src", "main", "AndroidManifest.xml")).text
        def app = Templet.manifestApplicaion.replace("#packageName",project.packageName)
                .replace("#app_icon",project.exts(D.app_icon,"@mipmap/ic_launcher"))
                .replace("#app_name",project.exts(D.app_name,project.name))
                .replace('#luanchActivity',project.exts(D.luanchActivity,""))
        def meta = Templet.manifestMeta.replace('#packageName',project.packageName)
                    .replace("#${D.versionName}",project.exts(D.versionName,"1.0.0"))
                    .replace("#${D.versionCode}",project.exts(D.versionCode,"1"))
//        println("app = $app")
        def newTest = manifestText.replaceFirst("<!--menifest start-->(?s).*<!--menifest end-->",meta)
                .replaceFirst("<!--app start-->(?s).*<!--app end-->",app)
        manifestText = newTest
        def outFile = project.file(getUrl(project.exts(D.outDir,project.defaultDir),"AndroidManifest.xml"))
        generatorFile(outFile,manifestText)

    }

    static void generatorFile(File file,String data){
        if(file.exists()) file.delete()
        file.parentFile.mkdirs()
        BufferedOutputStream out = file.newOutputStream()
        out.write(data.getBytes())
        out.flush()
        out.close()
    }


    static String generatorGradle(Project project) {
        def exts = project.exts
        def file = project.file(getUrl(exts(D.outDir,project.defaultDir),"android.gradle"))

        def tepmletStr = Templet.androidTemplet
        if(exts(D.flavorsEnable,false)) tepmletStr = tepmletStr.replace('//productFlavorsPosition',Templet.productFlavors)
        if(exts(D.uploadEnable,false)) tepmletStr = tepmletStr.replace('//mavenTemplet',Templet.mavenTemplet)
        if(project.isLibrary&&exts(D.asApp,false)) tepmletStr = tepmletStr.replace('  //sourceSet',Templet.sourceSet).replace("#outDir",exts(D.outDir,project.defaultDir))

        tepmletStr = VersionUtils.updateGradleProperties(project, tepmletStr)

        generatorFile(file,tepmletStr)
        def path = file.absolutePath
        return path
    }





    static def getProperties(Project project){
        def exts = project.exts
        def versions = [
                compileSdkVersion :exts(D.compileSdkVersion,"26")
                ,minSdkVersion:exts(D.minSdkVersion,"16")
                ,targetSdkVersion:exts(D.targetSdkVersion,"21")
                ,maven_url:getMavenUrl(exts)
                ,artifactId:project.name
                ,pom_version:exts(D.pom_version,"0.0.1")
        ]
        if(exts(D.printLog,false)) println("all versions = $versions")
        return versions
    }

    static String getMavenUrl(def exts){
        String maven = exts(D.maven_url,null)
        if(maven==null) maven = exts(D.testEnv,true)?D.maven_url_test:D.maven_url_release
        return maven
    }
/**
 * 更新文件中的版本号
 * @param project
 * @param source
 * @return
 */
    static String updateGradleProperties(Project project, String source){
        getProperties(project).findAll { map->
            source=source.replace("#${map.key}",map.value.toString())
        }
        return source
    }



}