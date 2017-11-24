package utils
/**
 * Created by pqixing on 17-11-21.
 */

class Templet {

/**
 * 返回默认的gradle文件
 * @return
 */
    static String getAndroidTemplet() {
        return '''
android {
    compileSdkVersion #compileSdkVersion
    defaultConfig {
        minSdkVersion #minSdkVersion
        targetSdkVersion #targetSdkVersion

        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
        manifestPlaceholders = [CHANNEL_NAME: "DACHEN_DOCTOR"]
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro\'
        }
    }
    
    //productFlavorsPosition
    
    //sourceSet

}
    //mavenTemplet
        '''
    }

    static String getSourceSet(){
        return '''
 sourceSets {
        //在main目录中
        main {
            java.srcDirs += "#outDir"
            manifest.srcFile '#outDir/AndroidManifest.xml'
        }
    }
'''
    }

    static String getMavenTemplet(){
        return '''
apply plugin: "maven"
// 上传到本地代码库
uploadArchives{
    repositories{
        mavenDeployer{
            repository(url:"#maven_url"){
                authentication(userName: "${project.exts('maven_userName')}", password: "${project.exts('maven_userName')}")
            }
            pom.groupId = 'com.dachen.android' // 组名
            pom.artifactId = '#artifactId' // 插件名
            pom.version = '#pom_version' // 版本号
        }
    }
}
'''
    }
    static String getProductFlavors (){
        return '''//多渠道打包
    flavorDimensions "dachen"
    productFlavors {
        dachen {
            manifestPlaceholders = [CHANNEL_NAME: "DACHEN_DOCTOR"]
             dimension "dachen"
        }
        //channelStart----
        yyb {
            manifestPlaceholders = [CHANNEL_NAME: "YINGYONGBAO_DOCTOR"]
            dimension "dachen"
        }
        baidu {
            manifestPlaceholders = [CHANNEL_NAME: "BAIDU_DOCTOR"]
            dimension "dachen"
        }
        wdj {
            manifestPlaceholders = [CHANNEL_NAME: "WANDOUJIA_DOCTOR"]
            dimension "dachen"
        }
        shichang_360 {
            manifestPlaceholders = [CHANNEL_NAME: "360_DOCTOR"]
            dimension "dachen"
        }
        hiapk {
            manifestPlaceholders = [CHANNEL_NAME: "ANZHUO_DOCTOR"]
            dimension "dachen"
        }
        anzhi {
            manifestPlaceholders = [CHANNEL_NAME: "ANZHI_DOCTOR"]
           dimension "dachen"
        }
        mi {
            manifestPlaceholders = [CHANNEL_NAME: "XIAOMI_DOCTOR"]
           dimension "dachen"
        }
        HW {
            manifestPlaceholders = [CHANNEL_NAME: "HUAWEI_DOCTOR"]
          dimension "dachen"
        }
        mz {
            manifestPlaceholders = [CHANNEL_NAME: "MEIZU_DOCTOR"]
            dimension "dachen"
        }
        //channelEnd----
    }
'''
    }


    static String getApplicationTemplet(){
        return '''
package #packageName;

import android.app.Application;

/**
 * Created by pqixing on 17-11-24.
 */

public class DefaultAppCation extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //ApplicationLike.onCreate();
    }
}
'''
    }

    static String getManifestMeta(){
        return '''
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    package="#packageName"
    android:versionCode="#versionCode"
    android:versionName="#versionName"
    >
'''
    }

    static String getManifestApplicaion(){
        return '''
   <application
        android:allowBackup="true"
        android:name="#packageName.DefaultAppCation"
        android:icon="#app_icon"
        android:label="#app_name"
        >
        <activity android:name="#luanchActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
'''
    }
}
