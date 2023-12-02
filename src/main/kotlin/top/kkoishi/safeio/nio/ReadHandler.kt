package top.kkoishi.safeio.nio

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.codec.http.HttpResponseStatus
import top.kkoishi.safeio.io.IOUtil
import java.lang.Exception

class ReadHandler : HttpRequestHandler() {
    override fun readHttpRequest(
        ctx: ChannelHandlerContext,
        msg: HttpRequest
    ): Pair<ByteBuf, HttpResponseStatus> = try {
        val json = IOUtil.readData()
        Unpooled.copiedBuffer(json, Charsets.UTF_8) to HttpResponseStatus.OK
    } catch (e: Exception) {
        e.printStackTrace()
        Unpooled.copiedBuffer("internal error", Charsets.UTF_8) to HttpResponseStatus.INTERNAL_SERVER_ERROR
    }

    override fun verifyRequest(uri: String, method: HttpMethod): Boolean {
        return method == HttpMethod.GET && uri == "/read_data"
    }
}