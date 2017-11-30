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
            if (project.hasProperty(key)) return project.ext.get(key)
            else if (project.pros[key] != null) return project.pros[key]
            else return value
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
                if (project.hasProperty(key)) value = project.ext.get(key)
                else value = project.pros[key]
            }
            if (value == null) {
                value = "+"
            }
            return "${project.pros[Configs.groupName]}.android:$key:$value"
        }
        fromRepo.delegate = p
        p.ext.fromRepo = fromRepo
    }
}