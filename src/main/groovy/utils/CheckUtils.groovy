package utils

import auto.Configs
import org.gradle.api.Project

class CheckUtils {
    /**
     * 检查项目中的属性配置是否正确
     * @param p
     * @return
     */
   static boolean isProsAvaild(Project p) {
        def exts = p.exts
        if (exts(Configs.uploadEnable)) {
            if (isNull(exts(Configs.pom_version))) {
                throw new RuntimeException("project $p.name uploadEnable is true,you have to set pom_version for library!")
            }
        }
        if (!p.app && exts(Configs.asApp)) {
            if (isNull(exts(Configs.launchActivity))) {
                throw new RuntimeException("project $p.name asApp is true,you have to set launchActivity!")
            }
            if (isNull(exts(Configs.app_icon))) {
                throw new RuntimeException("project $p.name asApp is true,you have to set app_icon!")
            }
        }
    }

    static boolean isNull(def obj) {

        return null == obj || "" == obj.toString()||"null"==obj.toString()
    }

    /**
     * 是否允许上传
     * @param project
     * @return
     */
    static boolean isUploadAble(Project project) {
        return project.exts(Configs.uploadEnable) && ("release" != project.exts(Configs.env) || "dc_release" == project.exts(Configs.s_mv_key))
    }
}