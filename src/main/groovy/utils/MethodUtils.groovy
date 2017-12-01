package utils

import auto.Configs
import org.gradle.api.Project

class MethodUtils {
    /**
     * 添加获取属性的方法
     * @param p
     */
    static void addExts(Project p) {
        def exts = { key, value = null ->
            if (project.hasProperty(key)) value = project.ext.get(key)
            else if (project.pros[key] != null) value = project.pros[key]
            println("exts $key : $value")
           value
        }

        exts.delegate = p
        p.ext.exts = exts
    }

    /**
     * 添加获取依赖的方法
     * @param p
     */
    static void addFromRepo(Project p) {
        def fromRepo = { key, value = null ->
            key = key.replace(":", "")

            if (value == null) {
                def repoVersions = p.exts(Configs.repoVersions)
                if(repoVersions !=null) value = repoVersions[key]
            }
            if (value == null) {
                value = "+"
            }
            return "${project.pros[Configs.groupName]}.android:$key:$value"
        }
        fromRepo.delegate = p
        p.ext.fromRepo = fromRepo
    }

    static String getUrl(String[] strs) {
        def result = new StringBuilder()
        strs.each {
            result.append("$it$File.separator")
        }
        return result.substring(0, result.size() - 1)
    }
}