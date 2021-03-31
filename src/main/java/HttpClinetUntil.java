import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;

import java.io.IOException;


public class HttpClinetUntil {
    public static int sendPost(String urlParam, String phone) {
        // 创建httpClient实例对象
        HttpClient httpClient = new HttpClient();
        // 设置httpClient连接主机服务器超时时间：2000毫秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(2000);
        // 创建post请求方法实例对象
        PostMethod postMethod = new PostMethod(urlParam);
        // 设置post请求超时时间
        postMethod.getParams().setParameter(HttpClientParams.SO_TIMEOUT, 2000);
        postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        postMethod.addRequestHeader("Content-Length", "153");
        //fromData数据
        postMethod.addParameter("DDDDD", phone);
        postMethod.addParameter("upass", "88888");
        postMethod.addParameter("R1", "0");
        postMethod.addParameter("R2", "");
        postMethod.addParameter("R3", "0");
        postMethod.addParameter("R6", "0");
        postMethod.addParameter("para", "00");
        postMethod.addParameter("0MKKey", "123456");
        postMethod.addParameter("buttonClicked", "");
        postMethod.addParameter("redirect_url", "");
        postMethod.addParameter("err_flag", "");
        postMethod.addParameter("username", "");
        postMethod.addParameter("password", "");
        postMethod.addParameter("user", "");
        postMethod.addParameter("cmd", "");
        postMethod.addParameter("Login", "");
        postMethod.addParameter("v6ip", "");
        try {
            httpClient.executeMethod(postMethod);
            System.out.println(postMethod.getResponseBodyAsString());
        } catch (IOException e) {
            return -1;
        } finally {
            postMethod.releaseConnection();
        }
        return HttpStatus.SC_OK;
    }

    //判断当前是否有网络
    public static boolean isHasNet() {
        boolean hasnet = false;  //默认没有网络
        // 创建httpClient实例对象
        HttpClient httpClient = new HttpClient();
        // 设置httpClient连接主机服务器超时时间：15000毫秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        // 创建get请求方法实例对象
        GetMethod getMethod = new GetMethod("http://www.baidu.com");
        try {
            httpClient.executeMethod(getMethod);
            if (getMethod.getResponseBodyAsString().contains("STATUS OK")) { //返回状态为OK
                hasnet = true;  //有网
            }
        } catch (IOException e) {
            System.out.println("错误信息：" + e.getMessage());
        } finally {
            getMethod.releaseConnection();
        }
        return hasnet;
    }
}
