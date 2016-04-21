package com.example.wq.help;

import com.example.wq.newclass.RecoResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wq on 2016/3/31.
 */
public class javaJson {


    /*
    序列化出对象ArrayList
    */
    public static RecoResult getReconObject(String jsonString){
        Gson gson=new Gson();
        RecoResult result=gson.fromJson(jsonString,RecoResult.class);
        return result;
    };

}

