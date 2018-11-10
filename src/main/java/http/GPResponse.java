package http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import static io.netty.handler.codec.http.HttpHeaderNames.*;


/**
 * Created by Chenjf on 2018/11/10.
 */
public class GPResponse {

    private ChannelHandlerContext ctx;
    private HttpRequest r;

    public GPResponse(ChannelHandlerContext ctx,HttpRequest r){
        this.ctx = ctx;
        this.r = r;
    }

    public void write(String out) throws Exception{
        try {
            if(out == null){return ;}
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer(out.getBytes("UTF-8")));
            //设置请求头
            response.headers().set(CONTENT_TYPE, "text/json");
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            response.headers().set(EXPIRES, 0);
            if (HttpHeaders.isKeepAlive(r)) {
                response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            }

            ctx.write(response);
        }finally {
            ctx.flush();
        }
    }
}
