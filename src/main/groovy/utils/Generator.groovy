package utils

import auto.Configs
import org.gradle.api.Project

class Generator {
    /**
     * 输出文件
     * @param file
     * @param data
     */
    static String write(File file, String data) {
        if (CheckUtils.isNull(file)) return ""
        if (file.exists()) file.delete()
        file.parentFile.mkdirs()
        BufferedOutputStream out = file.newOutputStream()
        out.write(data.getBytes())
        out.flush()
        out.close()
        return file.path
    }

/**
 * 输出配置文件信息，返回输出路径
 * @param project
 * @return
 */
    static String writeConfigs(Project project) {
        def exts = project.exts
        def configsFile = project.file(exts(Configs.outDir) + File.separator + "configs.txt")

        def configsStr = new StringBuilder()
        Configs.properties.keySet().sort { it.key }.each { key ->
            configsStr.append("$key : ${key.startsWith('s_') ? 'Not Public Properties!!!' : exts(key)} \n")
        }
        configsStr.append("--------------------------------android configs--------------------------------\n")

        def variantClosures = { variant ->
            configsStr.append("--------------------------------variant name = $variant.name --------------------------------\n")
            configsStr.append("applicationId : $variant.applicationId\n")
        }
        if ("application" == exts(Configs.plugin_type))
            project.android.applicationVariants.all(variantClosures)
        else project.android.libraryVariants.all(variantClosures)

        println("configsStr = $configsStr")
        return write(configsFile, configsStr.toString())
    }
}