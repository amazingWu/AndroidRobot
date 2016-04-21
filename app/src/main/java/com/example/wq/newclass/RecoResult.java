package com.example.wq.newclass;

import java.util.List;

/**
 * 科大讯飞语音识别序列化对象
 * Created by wq on 2016/3/31.
 */
public class RecoResult {
    public int sn;
    public boolean ls;
    public int bg;
    public int ed;
    public List<WS> ws;

    @Override
    public String toString() {
        String result="";
        for (int i=0;i<this.ws.size();i++)
        {
            result+=ws.get(i).cw.get(0).w;
        }
        return result;
    }
}

class WS{
    public int bg;
    public List<CW> cw;
}

class CW{
    public String w;//需要得到的词语
    public int sc;
}

