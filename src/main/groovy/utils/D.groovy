package utils

class D{

    //需要替换到模板的字段，不能轻易修改字段名
    static final String compileSdkVersion ="compileSdkVersion"
    static final String minSdkVersion ="minSdkVersion"
    static final String targetSdkVersion ="targetSdkVersion"

    static final String versionCode ="versionCode"
    static final String versionName ="versionName"

    static final String maven_url ="maven_url"
    static final String pom_version ="pom_version"
    static final String artifactId ="artifactId"




    //自定义字段
    static final String uploadEnable ="uploadEnable"
    static final String flavorsEnable ="flavorsEnable"

    static final String testEnv ="testEnv"
    static final String printLog ="printLog"

    //作为app单独运行，必须是library才成效
    static final String asApp ="asApp"

    static final String applicationLike ="applicationLike"
    static final String luanchActivity ="luanchActivity"
    static final String app_icon ="app_icon"
    static final String app_name ="app_name"
    static final String app_theme ="app_theme"


    //输出目录
    static final String outDir ="outDir"

    static final maven_url_test = "http://192.168.3.7:9527/nexus/content/repositories/android-test/"
    static final maven_url_release = "http://192.168.3.7:9527/nexus/content/repositories/android/"


}