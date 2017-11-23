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

}

    //mavenTemplet
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
}