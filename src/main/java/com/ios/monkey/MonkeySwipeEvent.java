package com.ios.monkey;

import com.alibaba.fastjson.JSONObject;
import macaca.client.MacacaClient;

/**
 * Created by hujiachun on 16/12/23.
 */
public class MonkeySwipeEvent extends MonkeyEvent {
    private double startX, startY, endX, endY;
    private MacacaClient driver;


    public MonkeySwipeEvent(MacacaClient driver, double startX, double startY, double endX, double endY) {
        super(MonkeyEvent.EVENT_TYPE_SWIPE);
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.driver = driver;

    }

    public int injectEvent() throws Exception {
        System.out.println("sending Swipe Event : Swipe-> [start(" + startX + "," + startY + "), end(" + endX + "," + endY+")]");
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("fromX", startX);
        jSONObject.put("fromY", startY);
        jSONObject.put("toX", endX);
        jSONObject.put("toY", endY);
        jSONObject.put("duration", 1);
        driver.touch("tap", jSONObject);
        //driver.touchAsync("tap", jSONObject);
        return MonkeyEvent.INJECT_SUCCESS;
    }
}
