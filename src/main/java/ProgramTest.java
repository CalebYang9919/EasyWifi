import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpStatus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class ProgramTest {
//    private static final String AFTER_PHONE_NUMS_PATH = ".\\src\\main\\resources\\AfterPhoneNums.json";
//    private static final String PHONE_NUMS_PATH = ".\\src\\main\\resources\\PhoneNums.txt";

    private static final String AFTER_PHONE_NUMS_PATH = System.getProperty("user.dir") + "\\AfterPhoneNums.json";
    private static final String PHONE_NUMS_PATH = System.getProperty("user.dir") + "\\PhoneNums.txt";

    //map缓存
    private static Map<String, Integer> cacheMap = new LinkedHashMap<>();
    //临时数据集
    private static Map<String, Integer> map = new HashMap<>();

    public static void main(String[] args) throws Exception {
        //HttpClient系统日志配置
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "error");// "stdout"为标准输出格式，"error"为调试模式
        long beginTime; //记录连接网络后当前账户使用时间
        long endTime; //结束时间
        long phoneUseTime; //使用时间
        //先判断是否存在json文件
        File file = new File(AFTER_PHONE_NUMS_PATH);
        if (!file.exists()) {
            //不存在将txt转为json
            JSONObject json = FileUntil.ReadTxtToJson(PHONE_NUMS_PATH);
            //写入Json文件
            BufferedWriter bw1 = new BufferedWriter(new FileWriter(new File(AFTER_PHONE_NUMS_PATH), true));
            bw1.write(json.toJSONString());
            bw1.close();
            // FileUntil.WriteJson(json);
        }
        //读取Json文件存入缓存
        cacheMap = FileUntil.sortMapByValues(FileUntil.ReadJsonToMap(AFTER_PHONE_NUMS_PATH));
        //读取Json文件存入临时数据集
        map = FileUntil.ReadJsonToMap(AFTER_PHONE_NUMS_PATH);
        while (true) {
            if (cacheMap.size() == 0) { //当缓存中没有可用号码时
                // 读取Json文件存入缓存
                cacheMap = FileUntil.sortMapByValues(FileUntil.ReadJsonToMap(AFTER_PHONE_NUMS_PATH));
            }
            //初始化
            beginTime = 0;
            endTime = 0;
            phoneUseTime = 0;
            String phoneNum = FileUntil.GetPhoneNum(cacheMap);  //读缓存数据
            int code = HttpClinetUntil.sendPost("http://172.16.18.6/a70.htm", phoneNum);
            if (code == HttpStatus.SC_OK) {  //如果请求成功，就去判断当前是否有网络
                while (HttpClinetUntil.isHasNet()) { //有网
                    System.out.println("当前已连接上网络,账户为：" + phoneNum);
                    //记录当前账户使用时间
                    beginTime = System.currentTimeMillis();
                    Thread.sleep(2000); //每隔2秒判断当前网络状态
                    endTime = System.currentTimeMillis();
                    phoneUseTime += (endTime - beginTime) / 1000;  //使用时间（秒）
                }
                if (phoneUseTime != 0) {
                    map.put(phoneNum, Math.toIntExact(phoneUseTime));    //写入临时数据集
                }
                //没网，从缓存中删除当前号码
                cacheMap.remove(phoneNum);
                //删除Json文件
                file.delete();
                //临时数据集重新写入Json
                BufferedWriter bw2 = new BufferedWriter(new FileWriter(new File(AFTER_PHONE_NUMS_PATH), true));
                JSONObject jsonObject = FileUntil.ReadMapToJson(map);
                bw2.write(jsonObject.toJSONString());
                bw2.close();
                System.out.println("当前账号：" + phoneNum + "没网");
            } else {//请求校园网不成功
                System.out.println("请连接校园网！");
            }
            Thread.sleep(1000);
        }
    }
}

