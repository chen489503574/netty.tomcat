package netty.server;

import http.GPRequest;
import http.GPResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import servlets.MyServlet;

/**
 * Created by Chenjf on 2018/11/9.
 */
public class GPTomcatHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if(msg instanceof HttpRequest){
            HttpRequest r = (HttpRequest)msg;

            GPRequest request = new GPRequest(ctx,r);
            GPResponse response = new GPResponse(ctx,r);

            new MyServlet().doGet(request,response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
