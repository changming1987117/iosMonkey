package com.ios.monkey;

import com.alibaba.fastjson.JSONObject;
import macaca.client.MacacaClient;

/**
 * Created by cm on 2017.7.21 酷狗app特殊的需要
 */
public class MonkeySpecialEvent extends MonkeyEvent{
    private double startX, startY, endX, endY;
    private MacacaClient driver;


    public MonkeySpecialEvent(MacacaClient driver, double startX, double startY, double endX, double endY) {
        super(MonkeyEvent.EVENT_TYPE_SWIPE);
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.driver = driver;

    }


    public int injectEvent() throws Exception {
        // 循环4次左滑
        for(int i = 0; i<4; i++){
            System.out.println("sending Swipe Event : Swipe-> [start(" + startX + "," + startY + "), end(" + endX + "," + endY+")]");
            driver.drag(startX,startY,endX,endY,2,50);
            Thread.sleep(2000);
        }

        return MonkeyEvent.INJECT_SUCCESS;
    }
}
