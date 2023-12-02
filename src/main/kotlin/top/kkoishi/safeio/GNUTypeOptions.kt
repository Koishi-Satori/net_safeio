package top.kkoishi.safeio

import java.lang.Exception
import java.lang.NumberFormatException
import java.math.BigInteger
import java.nio.file.Path
import kotlin.io.path.*

object GNUTypeOptions {
    class Option(
        private val names: Array<String>,
        val requireExtended: Boolean = false,
        private val action: (String, String?) -> Unit
    ) {
        fun matches(arg: String) = names.contains(arg)

        operator fun invoke(option: String, arg: String?) = action(option, arg)
    }

    @JvmStatic
    private val recognizedOptions: Array<Option> = arrayOf(
        Option(arrayOf("-dir", "-d"), true) { _, arg ->
            if (arg != null) {
                try {
                    val nDir = Path.of(arg)
                    if ((nDir.exists() && !nDir.isDirectory()) || nDir.notExists())
                        nDir.createDirectories()
                    Main.dir = nDir.absolutePathString()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Main.dir = "./"
                }
            }
        },
        Option(arrayOf("-port", "-p"), true) { _, port ->
            if (port != null) {
                try {
                    val iPort = port.toBigInteger()
                    if (iPort < BigInteger("65536") && iPort.signum() == 1) {
                        Main.port = iPort.toInt()
                        println("new port: ${Main.port}")
                    } else
                        println("invalid port number: $iPort")
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }
            }
        }
    )

    @JvmStatic
    fun handleArguments(vararg args: String) {
        val rest = args.iterator()
        while (rest.hasNext()) {
            val arg = rest.next()
            handleArgument(arg, rest)
        }
    }

    @JvmStatic
    fun handleArgument(arg: String, rest: Iterator<String>) {
        val filteredOptions = recognizedOptions.filter { it.matches(arg) }
        if (filteredOptions.isEmpty())
            println("Unknown arg: $arg")
        else with(filteredOptions.first()) {
            if (!requireExtended)
                invoke(arg, null)
            else {
                if (rest.hasNext())
                    invoke(arg, rest.next())
                else
                    println("Lack of extended argument.")
            }
        }
    }
}