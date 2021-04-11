import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;


import java.io.*;


public class HttpClinetUntil {
    private static final String CONFIG_PATH = System.getProperty("user.dir") + "\\config.xml";
    private static final String UA_PATH = System.getProperty("user.dir") + "\\ua.txt";

//    private static final String CONFIG_PATH = ".\\src\\main\\resources\\config.xml";
//    private static final String UA_PATH = ".\\src\\main\\resources\\ua.txt";

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
            //System.out.println(postMethod.getResponseBodyAsString());
        } catch (IOException e) {
            return -1;
        } finally {
            postMethod.releaseConnection();
        }
        return HttpStatus.SC_OK;
    }

    //判断当前是否有网络
    public static boolean isHasNet() {
        //判断是否存在xml文件
        File fileConfig = new File(CONFIG_PATH);
        //判断是否存在ua.txt文件
        File fileUa = new File(UA_PATH);
        if (!fileConfig.exists()) {
            //不存在则提示
            System.out.println("错误信息：缺少config.xml文件");
            System.exit(0);//退出程序
        }
        if (!fileUa.exists()) {
            //不存在则提示
            System.out.println("错误信息：缺少ua.txt文件");
            System.exit(0);//退出程序
        }
        boolean hasnet = false;  //默认没有网络
        // 创建httpClient实例对象
        HttpClient httpClient = new HttpClient();
        // 设置httpClient连接主机服务器超时时间：15000毫秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        // 创建get请求方法实例对象
        GetMethod getMethod = new GetMethod("http://www.baidu.com");
        //自动获取ua
        String state = FileUntil.ReadXmlElement(CONFIG_PATH, "Headers", "User-Agent-isAuto");
        String hv = "";//ua字段
        if (state.equals("True")) {
            //自动获取Ua
            try {
                RandomAccessFile raf = new RandomAccessFile(UA_PATH, "r");
                int index = (int) ((Math.random() * 12)) * 110;
                raf.seek(index);
                hv = raf.readLine();
                if (!hv.equals("")) {
                    getMethod.addRequestHeader("User-Agent", hv);
                }
                raf.close();
            } catch (IOException e) {
                System.out.println("错误信息：" + e.getMessage());
            }
        } else {
            //手动更改ua
            hv = FileUntil.ReadXmlElement(CONFIG_PATH, "Headers", "User-Agent");
            if (!hv.equals("")) {
                getMethod.addRequestHeader("User-Agent", hv);
            }
        }
        try {
            httpClient.executeMethod(getMethod);
            InputStream str = getMethod.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(str));
            StringBuffer sb = new StringBuffer();
            String result = "";
            while ((result = br.readLine()) != null) {
                if (result.contains("STATUS OK")) {     //返回状态为OK
                    hasnet = true;  //有网
                    break;
                }
            }
//            if (getMethod.getResponseBodyAsString().contains("STATUS OK")) { //返回状态为OK
//                hasnet = true;  //有网
//            }
        } catch (IOException e) {
            System.out.println("错误信息：" + e.getMessage());
        } finally {
            getMethod.releaseConnection();
        }
        return hasnet;
    }
}
