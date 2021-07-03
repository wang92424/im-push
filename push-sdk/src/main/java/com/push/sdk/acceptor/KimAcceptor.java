package com.push.sdk.acceptor;

import com.push.sdk.coder.ServerDecoder;
import com.push.sdk.coder.ServerEncoder;
import com.push.sdk.coder.WebsocketDecoder;
import com.push.sdk.coder.WebsocketEncoder;
import com.push.sdk.handler.IRequestHandler;
import com.push.sdk.handler.KimHttpHandler;
import com.push.sdk.handler.KimWsHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author wgj
 * @description 接收器
 * @date 2021/6/17 17:08
 * @modify
 */
public class KimAcceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(KimAcceptor.class);
    /**
     * 读空闲时间(秒)
     */
    public final Duration writeIdle = Duration.ofSeconds(45);
    /**
     * 写接空闲时间(秒)
     */
    public final Duration readIdle = Duration.ofSeconds(60);
    /**
     * 服务端端口
     */
    private final Integer serverPort;
    private final Integer webPort;
    /**
     * webSocket端口
     */
    private final ThreadFactory bossThreadFactory;
    private final ThreadFactory workerThreadFactory;
    private final IRequestHandler iRequestHandler;
    private EventLoopGroup serverBossGroup;
    private EventLoopGroup serverWorkerGroup;
    private EventLoopGroup webBossGroup;
    private EventLoopGroup webWorkerGroup;

    public KimAcceptor(Integer serverPort, int webPort, IRequestHandler iRequestHandler) {
        this.serverPort = serverPort;
        this.webPort = webPort;
        this.iRequestHandler = iRequestHandler;
        bossThreadFactory = r -> {
            Thread thread = new Thread(r);
            thread.setName("nio-boss-");
            return thread;
        };
        workerThreadFactory = r -> {
            Thread thread = new Thread(r);
            thread.setName("nio-worker-");
            return thread;
        };

    }

    private void createAppEventGroup() {
        if (isLinuxSystem()) {
            serverBossGroup = new EpollEventLoopGroup(bossThreadFactory);
            serverWorkerGroup = new EpollEventLoopGroup(workerThreadFactory);
        } else {
            serverBossGroup = new NioEventLoopGroup(bossThreadFactory);
            serverWorkerGroup = new NioEventLoopGroup(workerThreadFactory);
        }
    }

    private void createWebEventGroup() {
        if (isLinuxSystem()) {
            webBossGroup = new EpollEventLoopGroup(bossThreadFactory);
            webWorkerGroup = new EpollEventLoopGroup(workerThreadFactory);
        } else {
            webBossGroup = new NioEventLoopGroup(bossThreadFactory);
            webWorkerGroup = new NioEventLoopGroup(workerThreadFactory);
        }
    }

    public void bind() {

        if (serverPort != null) {
            bindServerPort();
        }

        if (webPort != null) {
            bindWebsocketPort();
        }
    }

    public void destroy(EventLoopGroup bossGroup, EventLoopGroup workerGroup) {
        if (bossGroup != null && !bossGroup.isShuttingDown() && !bossGroup.isShutdown()) {
            try {
                bossGroup.shutdownGracefully();
            } catch (Exception ignore) {
            }
        }

        if (workerGroup != null && !workerGroup.isShuttingDown() && !workerGroup.isShutdown()) {
            try {
                workerGroup.shutdownGracefully();
            } catch (Exception ignore) {
            }
        }
    }

    public void destroy() {
        this.destroy(serverBossGroup, serverWorkerGroup);
    }

    /**
     * 綁定服務端端口
     */
    private void bindServerPort() {
        createAppEventGroup();
        ServerBootstrap bootstrap = createServerBootstrap(serverBossGroup, serverWorkerGroup);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
                ch.pipeline().addLast("http-codec", new HttpServerCodec());
                // 拼接完整的HTTP请求
                ch.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
                ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                ch.pipeline().addLast(new ServerDecoder());
                ch.pipeline().addLast(new ServerEncoder());
                ch.pipeline().addLast("http-handler", KimHttpHandler.instance());
                ch.pipeline().addLast("websocket-handler", KimWsHandler.instance(iRequestHandler));
            }
        });
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        ChannelFuture channelFuture = bootstrap.bind(serverPort).syncUninterruptibly();
        channelFuture.channel().newSucceededFuture().addListener(future -> {
            String logBanner = "\nServer Socket Server started on port {}.\n";
            LOGGER.info(logBanner, serverPort);
        });
        channelFuture.channel().closeFuture().addListener(future -> this.destroy(serverBossGroup, serverWorkerGroup));
    }

    private void bindWebsocketPort() {
        createWebEventGroup();
        ServerBootstrap bootstrap = createServerBootstrap(webBossGroup, webWorkerGroup);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            public void initChannel(SocketChannel ch) {
                ch.pipeline().addLast(new HttpServerCodec());
                ch.pipeline().addLast(new ChunkedWriteHandler());
                ch.pipeline().addLast(new HttpObjectAggregator(65536));
                ch.pipeline().addLast(new WebSocketServerProtocolHandler("/", false));
                ch.pipeline().addLast(new WebsocketDecoder());
                ch.pipeline().addLast(new WebsocketEncoder());
                ch.pipeline().addLast(new IdleStateHandler(readIdle.getSeconds(), writeIdle.getSeconds(), 0, TimeUnit.SECONDS));
                ch.pipeline().addLast(KimWsHandler.instance(iRequestHandler));
            }

        });

        ChannelFuture channelFuture = bootstrap.bind(webPort).syncUninterruptibly();
        channelFuture.channel().newSucceededFuture().addListener(future -> {
            String logBanner = "\nWebSocket Server started on port {}.\n";
            LOGGER.info(logBanner, webPort);
        });
        channelFuture.channel().closeFuture().addListener(future -> this.destroy(webBossGroup, webWorkerGroup));
    }

    /**
     * 创建启动对象
     *
     * @param bossGroup
     * @param workerGroup
     * @return
     */
    private ServerBootstrap createServerBootstrap(EventLoopGroup bossGroup, EventLoopGroup workerGroup) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.channel(isLinuxSystem() ? EpollServerSocketChannel.class : NioServerSocketChannel.class);
        return bootstrap;
    }

    private boolean isLinuxSystem() {
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("linux");
    }
}
