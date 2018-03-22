package com.geeker.marketing.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2018/3/21 0021.
 */
public class demoUtils {

    public static void main(String[] args){
        List<Integer> list = new ArrayList<>();
        list.add(4);
        list.add(3);
        list.add(5);
        list.add(1);
        list.add(2);
        Collections.sort(list);
        for(Integer a : list){
            System.out.println(a);
        }
    }
}
