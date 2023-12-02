package top.kkoishi.safeio.io

import top.kkoishi.safeio.Main
import java.nio.file.Path
import kotlin.io.path.*

object IOUtil {
    @JvmStatic
    fun mergeData(data: MutableMap<String, String>) {
        val path = Path.of("${Main.dir}/${Main.fileName}")
        // create file if not exists
        if (path.notExists()) {
            with(path.parent) {
                if (notExists())
                    createDirectories()
            }
            path.createFile()
            path.writeText("{}")
        }

        // read existed data from file, then merge them.
        val json = path.readText(Charsets.UTF_8)
        val existedData = Main.GSON.fromJson(json, Main.dataMutableMapType)
        data.forEach { (k, v) -> existedData[k] = v }
        path.writeText(Main.GSON.toJson(existedData))
        println("success to merge data.")
    }

    @JvmStatic
    fun readData(): String {
        val path = Path.of("${Main.dir}/${Main.fileName}")
        // create file if not exists
        if (path.notExists()) {
            with(path.parent) {
                if (notExists())
                    createDirectories()
            }
            path.createFile()
            path.writeText("{}")
            // return empty
            return "{}"
        }

        return path.readText(Charsets.UTF_8)
    }
}