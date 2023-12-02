import org.junit.jupiter.api.Test
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL

class Test {
    @Test
    fun testPost() {
        val conn = URL("http://localhost:1146/write_data").openConnection() as HttpURLConnection
        conn.connectTimeout = 10000
        conn.readTimeout = 10000
        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "application/json;charset=utf-8")
        conn.setRequestProperty("Charset", "UTF-8")
        conn.setRequestProperty("Content-Length", "{\"test\": \"[test, test]\"}".toByteArray().size.toString())
        conn.doOutput = true
        conn.doInput = true
        conn.useCaches = false
        //conn.connect()

        val out = DataOutputStream(conn.outputStream)
        out.write("{\"test\": \"[test, test]\"}".toByteArray())
        out.flush()
        out.close()
        println(conn.responseMessage)
    }
}