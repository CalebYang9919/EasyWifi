import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class FileUntil {
    public static JSONObject ReadTxtToJson(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath)));
        HashMap<String, Object> map = new HashMap<String, Object>();
        String s = null;
        while ((s = reader.readLine()) != null) {//使用readLine方法，一次读一行
            map.put(s, 0l);
        }
        JSONObject jsonObject = new JSONObject(map); //map转Json
        reader.close();
        return jsonObject;
    }

    public static JSONObject ReadMapToJson(Map<String, Integer> maps) throws IOException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (Map.Entry<String, Integer> entry : maps.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        JSONObject jsonObject = new JSONObject(map); //map转Json
        return jsonObject;
    }


    public static Map<String, Integer> ReadJsonToMap(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath)));
        String s = null;
        JSONObject jsonObject = null;
        while ((s = reader.readLine()) != null) {//使用readLine方法，一次读一行
            jsonObject = JSON.parseObject(s);
        }
        //Json转map
        Map<String, Integer> stringLongMap = new HashMap<String, Integer>();
        for (Map.Entry<String, Object> map : jsonObject.entrySet()) {
            stringLongMap.put(map.getKey(), (Integer) map.getValue());
        }
        reader.close();
        return stringLongMap;
    }


    public static <K extends Comparable, V extends Comparable> Map<K, V> sortMapByValues(Map<K, V> aMap) {
        HashMap<K, V> finalOut = new LinkedHashMap<K, V>();
        aMap.entrySet()
                .stream()
                .sorted((p1, p2) -> p2.getValue().compareTo(p1.getValue()))
                .collect(Collectors.toList()).forEach(ele -> finalOut.put(ele.getKey(), ele.getValue()));
        return finalOut;
    }

    public static String GetPhoneNum(Map<String, Integer> map) throws IOException {
        String num = "";
        for (String item : map.keySet()) {
            num = item;
            break;
        }
        return num;
    }
}


