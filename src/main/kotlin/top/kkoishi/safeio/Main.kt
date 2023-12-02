package top.kkoishi.safeio

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlin.concurrent.thread

fun main(args: Array<String>) {
    GNUTypeOptions.handleArguments(*args)
    thread {
        HttpServer(Main.port).run()
    }
}

object Main {
    @JvmStatic
    val GSON: Gson = GsonBuilder().setPrettyPrinting().create()

    @JvmStatic
    var dir = "./"

    @JvmStatic
    var fileName = "ifp_data.json"

    @JvmStatic
    var port: Int = 1145

    @JvmStatic
    val dataMutableMapType = object : TypeToken<MutableMap<String, String>>() {}
}