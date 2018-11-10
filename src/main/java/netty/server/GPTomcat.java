package netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * Created by Chenjf on 2018/11/9.
 */
public class GPTomcat {

    public void start(int port) throws Exception{
        //用NIO的写法
//        ServerSocketChannel s = ServerSocketChannel.open();
//        s.bind(new InetSocketAddress(8080));
        //用BIO的写法
//        ServerSocket s = new ServerSocket(port);

        //Netty的主从模型写法
        //Boss线程
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //Worker线程
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            //Netty服务
            ServerBootstrap server = new ServerBootstrap();//新建个   boot引擎

            //链路式编程
            server.group(bossGroup,workerGroup)
                    //绑定主线程处理类
                    .channel(NioServerSocketChannel.class)
                    //绑定子线程的处理类，Handler，每个Handler就是一个线程
                    .childHandler(new ChannelInitializer<SocketChannel>() {//这根本上就是个Handler
                        @Override
                        protected void initChannel(SocketChannel client) throws Exception {

                            //无锁化串行编程，用addLast添加到最前，请求进来的时候先处理GPTomcatHandler

                            //业务逻辑链路，编码器
                            client.pipeline().addLast(new HttpResponseEncoder());

                            //解码器
                            client.pipeline().addLast(new HttpRequestDecoder());

                            //业务逻辑处理
                            client.pipeline().addLast(new GPTomcatHandler());//先接收
                        }
                    })
                    //配置信息
                    .option(ChannelOption.SO_BACKLOG,128)//针对主线程的配置，线程最多的个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true);//针对子线程的配置，保持长连接
            //让其  阻塞，等待请求
            ChannelFuture f = server.bind(port).sync();

            System.out.println("GPTomcat已启动"+port);

            f.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        try{
            new GPTomcat().start(8080);
        }catch (Exception e ){
            e.printStackTrace();
        }
    }

}
