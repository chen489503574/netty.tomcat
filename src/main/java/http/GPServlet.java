package http;

/**
 * Created by Chenjf on 2018/11/10.
 */
public abstract class GPServlet {
    public abstract void doGet(GPRequest request,GPResponse response);
    public abstract void doPost(GPRequest request,GPResponse response);
}
