package top.kkoishi.safeio.nio

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.*

abstract class HttpRequestHandler : SimpleChannelInboundHandler<HttpObject>() {
    public override fun channelRead0(ctx: ChannelHandlerContext, msg: HttpObject) {
        println("read msg: ${msg.javaClass}")
        if (msg is HttpRequest) {
            if (verifyRequest(msg.uri(), msg.method())) {
                println("verified.")
                val (buf, status) = readHttpRequest(ctx, msg)
                println(buf.toString(Charsets.UTF_8))
                println(status)
                val response = DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, buf)
                response.headers()
                    .set(HttpHeaderNames.CONTENT_TYPE, "text/html;charsets=utf-8")
                    .set(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes())
                ctx.writeAndFlush(response)
            } else
                println("Not verified: ${msg.uri()}, ${msg.method()}")
        }
    }

    open fun verifyRequest(uri: String, method: HttpMethod): Boolean = method == HttpMethod.GET

    abstract fun readHttpRequest(ctx: ChannelHandlerContext, msg: HttpRequest): Pair<ByteBuf, HttpResponseStatus>
}