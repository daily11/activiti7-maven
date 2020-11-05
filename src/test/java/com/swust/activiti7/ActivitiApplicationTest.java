package com.swust.activiti7;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Chen Yixing
 * @date 2020/11/5 9:08
 */
public class ActivitiApplicationTest {
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "chen");
        map.put("phone", "17309045310");

        map.forEach((key, value) -> {
            System.out.println("key = " + key + ", value = " + value);
        });

        List<String> list = new ArrayList<>();
        list.add("chen");list.add("zou");
        list.forEach(arg -> System.out.println(arg));
    }
}
