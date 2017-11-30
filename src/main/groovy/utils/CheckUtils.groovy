package utils

import auto.Configs
import org.gradle.api.Project

class CheckUtils {
    /**
     * 检查项目中的属性配置是否正确
     * @param p
     * @return
     */
    boolean isProsAvaild(Project p) {
        def exts = p.exts
        if (exts(Configs.uploadEnable)) {
            if (isNull(exts(Configs.pom_version))) {
                throw new RuntimeException("project $project.name uploadEnable is true,you have to set pom_version for library!")
            }
        }
        if (!project.app && exts(Configs.asApp)) {
            if (isNull(exts(Configs.launchActivity))) {
                throw new RuntimeException("project $project.name asApp is true,you have to set launchActivity!")
            }
            if (isNull(exts(Configs.app_icon))) {
                throw new RuntimeException("project $project.name asApp is true,you have to set app_icon!")
            }
        }
    }

    boolean isNull(def obj) {

        return null == obj || "" == obj.toString()
    }

    boolean isEmpty(def col) {
        return null == col || !(col instanceof Collection) || col.isEmpty()
    }
}