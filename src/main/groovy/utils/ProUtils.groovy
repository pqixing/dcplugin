package utils

import auto.Configs
import org.gradle.api.Project

/**
 * Created by pqixing on 17-11-30.
 */

class ProUtils {

    /**
     * 初始化项目中配置的属性
     * @param project
     */
    static void initProperties(Project project) {
        def pros =[:]
        Configs.properties.each { map ->
            pros[map.key] =project.hasProperty(map.key)?project.ext.get(map.key):map.value
        }

        if (CheckUtils.isNull(pros[Configs.s_mv_url])) {
            pros[Configs.s_mv_url] = "release" == pros[Configs.env] ? pros[Configs.s_mv_release] : pros[Configs.s_mv_test]
        }
        pros[Configs.plugin_type] = project.app||pros[Configs.asApp]==true?"application":"library"

        pros.each { map ->
            if (!CheckUtils.isNull(map.value)|| map.value.toString().contains("#")) {
                map.value = map.value.toString().replace("#projectDir", project.projectDir.path)
                        .replace("#groupName", pros[Configs.groupName])
                        .replace("#projectName", project.name)
            }
        }
//        println("pros = $pros")
        project.ext.pros = pros//设置默认参数

        MethodUtils.addExts(project)
        MethodUtils.addFromRepo(project)
    }

    /**
     * 替换文本中所有的属性值
     * @param p
     * @param source
     * @return
     */
    static String replaceAllKey(Project p, String source) {
        Configs.properties.keySet().each { key ->
            source = replaceKey(p, source, key)
        }
        return source
    }
    /**
     * 替换文本中指定属性值的方法
     * #key   普通替换，只替换该字段
     * #1key  单行替换，如果value不为空，普通替换，否则，该行都隐藏
     * #2key    单行替换，如果用户配置的value不为空，普通替换用户配置的值，否则，该行都隐藏
     * @param p
     * @param source 源文本
     * @param key 属性的key
     * @param value 待替换的value，不传，则从默认配置项中查找
     * @return
     */
    static String replaceKey(Project p, String source, String key, String value = "def") {
        if ("def" == value) value = p.exts(key)
        def builder = new StringBuilder()
        source.eachLine { s ->
            if(CheckUtils.isNull(s)) return
            if (CheckUtils.isNull(String.valueOf(value)) && s.contains("#1$key")) return
            if(s.contains("#2$key")&&!p.hasProperty(key)) return

            s = s.replace("#$key", String.valueOf(value))
                    .replace("#1$key",String.valueOf(value))
                    .replace("#2$key",String.valueOf(value))

            builder.append(s).append("\n")//替换#（任意）key
        }
        return builder.toString()
    }
}
