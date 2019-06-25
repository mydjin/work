package com.xtaller.party.utils.convert;

import com.xtaller.party.utils.bean.Where;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taller on 2017/9/8.
 */
public class W {
    public static String[] and(Object... objects){
        int len = objects.length;
        String [] arr = new String[len];
        for(int i=0;i<len;i++){
            arr[i] = objects[i].toString();
        }
        return arr;
    }
    public static String[] order(Object... objects){
        int len = objects.length;
        String [] arr = new String[len];
        for(int i=0;i<len;i++){
            arr[i] = objects[i].toString();
        }
        return arr;
    }
    public static String[] field(Object... objects){
        int len = objects.length;
        String [] arr = new String[len];
        for(int i=0;i<len;i++){
            arr[i] = objects[i].toString();
        }
        return arr;
    }
    public static List<Where> f(String[]... wheres){
        List<Where> wList = new ArrayList<Where>();
        for(String[] w:wheres){
            switch (w.length){
                case 1:
                    wList.add(new Where(w[0]));
                    break;
                case 2:
                    wList.add(new Where(w[0],w[1]));
                    break;
                case 3:
                    wList.add(new Where(w[0],w[1],w[2]));
                    break;
            }
        }
        return wList;
    }
}
