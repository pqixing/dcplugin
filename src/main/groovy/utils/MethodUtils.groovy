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
                def repos = [:]

                def temp = p.exts(Configs.repoVersions)
                if(temp instanceof Map) repos.putAll(temp)
                if(temp instanceof  String)  repos.putAll(new GroovyShell().evaluate(temp))

                value = repos.get(key)
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