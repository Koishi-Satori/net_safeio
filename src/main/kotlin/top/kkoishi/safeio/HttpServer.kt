package top.kkoishi.safeio

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.http.HttpObject
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.codec.http.HttpServerCodec
import top.kkoishi.safeio.nio.OverrideHandler
import top.kkoishi.safeio.nio.ReadHandler
import top.kkoishi.safeio.nio.WriteHandler
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

class HttpServer(private val port: Int) : Runnable {
    override fun run() {
        val bossGroup = NioEventLoopGroup(1, InnerThreadFactory("boss.group"))
        val workerGroup = NioEventLoopGroup(
            Runtime.getRuntime().availableProcessors() * 2,
            InnerThreadFactory("worker.group")
        )
        try {
            val serverBootstrap = ServerBootstrap()
            serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel::class.java)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(ch: SocketChannel) {
                        val handlers = arrayOf(WriteHandler(), ReadHandler(), OverrideHandler())
                        ch.pipeline().addLast(HttpServerCodec())
                            .addLast("aggregator", HttpObjectAggregator(1048576))
                            .addLast(object : SimpleChannelInboundHandler<HttpObject>() {
                                override fun channelRead0(ctx: ChannelHandlerContext, msg: HttpObject) {
                                    if (msg is HttpRequest) {
                                        handlers.filter { it.verifyRequest(msg.uri(), msg.method()) }
                                            .takeIf { it.isNotEmpty() }?.first()
                                            ?.channelRead0(ctx, msg)
                                    }
                                }
                            })

                    }
                })
            val future = serverBootstrap.bind(port)
            future.addListener {
                if (it.isSuccess)
                    println("bind port success: $port")
                else
                    println("bind port failed.")
            }
            println("success bootstrap server.")
            future.channel().closeFuture().sync()
        } finally {
            bossGroup.shutdownGracefully()
            workerGroup.shutdownGracefully()
        }
    }

    private class InnerThreadFactory(val prefix: String) : ThreadFactory {
        private val counter: AtomicInteger = AtomicInteger(0)

        override fun newThread(r: Runnable): Thread =
            Thread(Thread.currentThread().threadGroup, r, "$prefix-${counter.getAndIncrement()}")
    }
}