package com.ios.monkey;

import com.alibaba.fastjson.JSONObject;
import com.ios.monkey.screenshot.Screenshot;
import com.ios.monkey.util.Shell;

import macaca.client.MacacaClient;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Random;

/**
 * Created by hujiachun on 16/12/21.
 */
public class Monkey {

    //idevicecrashreport -u ecab65eca01ae1d42874c26e645a33aee78296b6 -e -k /Users/hujiachun/Downloads/carshlog

    private MacacaClient driver;
    private int width, height, submitX_mim, submitX_max, submitY_mim, submitY_max, contentX_min, contentX_max, contentY_mim, contentY_max, special_point_x, special_point_y;
    private static boolean needhelp = false;
    private static String UDID, BUNDLEID, APPPATH, RESULT_DIR;

    // 默认允许180分钟
    private static String TIMING = "180";
    private static String PORT = "3456";
    private static String HOST = "127.0.0.1";
    private static String PROXYPORT = "8900";
    private static String REUSE = "3";
    private int backX = 25, backY = 40;
    private int eventcount = 0;


    public static void main(String[] args) throws IOException, InterruptedException {
        executeParameter(args);

    }


    private static void executeParameter(String[] args) {
        int optSetting = 0;

        for (; optSetting < args.length; optSetting++) {
            if ("-u".equals(args[optSetting])) {
                UDID = args[++optSetting];
            } else if ("-a".equals(args[optSetting])) {
                APPPATH = args[++optSetting];
            } else if ("-b".equals(args[optSetting])) {
                BUNDLEID = args[++optSetting];
            } else if ("-t".equals(args[optSetting])) {
                TIMING = args[++optSetting];
            } else if ("-d".equals(args[optSetting])) {
                RESULT_DIR = args[++optSetting];
            } else if ("-host".equals(args[optSetting])) {
                HOST = args[++optSetting];
            } else if ("-reuse".equals(args[optSetting])) {
                REUSE = args[++optSetting];
            } else if ("-port".equals(args[optSetting])) {
                PORT = args[++optSetting];
            } else if ("-proxyport".equals(args[optSetting])) {
                PROXYPORT = args[++optSetting];
            } else if ("-h".equals(args[optSetting])) {
                needhelp = true;
                System.out.println(
                        "-u:设备的UDID\n" +
                                "-a:测试App的绝对路径\n"+
                                "-b:测试App的Bundle\n"+
                                "-host:macaca服务器host\n"+
                                "-reuse:0: 启动并安装 app。1 (默认): 卸载并重装 app。 2: 仅重装 app。3: 在测试结束后保持 app 状态。\n"+
                                "-t:运行时长（分），可选，默认180分钟\n"+
                                "-port:macaca服务的端口，默认3456\n" +
                                "-proxyport:usb代理端口，默认8900");
                break;
            }

        }
        if (!needhelp) {
            try {
                System.out.println("测试设备:" + UDID + "\n" + "测试App:" + BUNDLEID);
                org.testng.Assert.assertTrue((!UDID.equals(null)) && (!BUNDLEID.equals(null)));
            } catch (Exception e) {
                System.out.println("请确认参数配置,需要帮助请输入 java -jar iosMonkey.jar -h\n"
                        + "ERROR信息"+ e.toString());
            }

            try {
                new Monkey().run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void run() throws Exception {
        System.out.println("本次设定的运行时长【" + TIMING + "】分钟");
        init();
        width = (Integer) driver.getWindowSize().get("width");
        height = (Integer) driver.getWindowSize().get("height");
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);

        submitX_max = width - 1;
        submitX_mim = width / 10;
        submitY_max = height - 1;
        submitY_mim = height / 10 * 9;

        contentX_max = width - width / 10;
        contentX_min = width / 10;
        contentY_max = height / 2 + height / 10;
        contentY_mim = height / 2 - height / 10;
        special_point_x = width / 2;
        special_point_y = (int) (height * 0.94);

        // 卸载安装需要划过闪屏页
        if(REUSE!="3"){

            Thread.sleep(3000);
            double startX = Math.ceil(0.8 * (width - 1));
            double startY = Math.ceil(0.5 * (height - 1));
            double endX = Math.ceil(0.25 * (width - 1));
            double endY = Math.ceil(0.5 * (height - 1));
            new MonkeySpecialEvent(driver, startX, startY, endX, endY).injectEvent();
        }
        long lastTime = (Integer.parseInt(TIMING) * 60 * 1000);
        long startTime = System.currentTimeMillis();
        while (true) {
            long endTime = System.currentTimeMillis();
            try{
                switch (new MathRandom().PercentageRandom()) {
                    case 0: {
                        double x = Math.ceil(Math.random() * (width - 1));
                        double y = Math.ceil(Math.random() * (height - 1));

                        Screenshot.screenshot(RESULT_DIR, UDID, new Double(x).intValue(), new Double(y).intValue());
                        new MonkeyTapEvent(driver, x, y).injectEvent();
                        eventcount = eventcount + 1;
                        System.out.println("---EVENT执行了：" + eventcount + "次---");
                        break;
                    }
                    case 1: {
                        double startX = Math.ceil(Math.random() * (width - 1));
                        double startY = Math.ceil(Math.random() * (height - 1));
                        double endX = Math.ceil(Math.random() * (width - 1));
                        double endY = Math.ceil(Math.random() * (height - 1));

                        Screenshot.screenshot(RESULT_DIR, UDID, new Double(startX).intValue(), new Double(endY).intValue());
                        new MonkeySwipeEvent(driver, startX, startY, endX, endY).injectEvent();
                        eventcount = eventcount + 1;
                        System.out.println("---EVENT执行了：" + eventcount + "次---");
                        break;
                    }
                    case 2: {
                        Screenshot.screenshot(RESULT_DIR, UDID, backX, backY);
                        new MonkeyBackEvent(driver, backX, backY).injectEvent();
                        eventcount = eventcount + 1;
                        System.out.println("---EVENT执行了：" + eventcount + "次---");
                        break;
                    }
                    case 3: {
                        Random random = new Random();
                        int x = random.nextInt(submitX_max) % (submitX_max - submitX_mim + 1) + submitX_mim;
                        int y = random.nextInt(submitY_max) % (submitY_max - submitY_mim + 1) + submitY_mim;

                        Screenshot.screenshot(RESULT_DIR, UDID, x, y);
                        new MonkeySubmitEvent(driver, x, y).injectEvent();
                        eventcount = eventcount + 1;
                        System.out.println("---EVENT执行了：" + eventcount + "次---");
                        break;
                    }
                    case 4: {
                        Random random = new Random();
                        int x = random.nextInt(contentX_max) % (contentX_max - contentX_min + 1) + contentX_min;
                        int y = random.nextInt(contentY_max) % (contentY_max - contentY_mim + 1) + contentY_mim;

                        Screenshot.screenshot(RESULT_DIR, UDID, x, y);
                        new MonkeyContentEvent(driver, x, y).injectEvent();
                        eventcount = eventcount + 1;
                        System.out.println("---EVENT执行了：" + eventcount + "次---");
                        break;
                    }
                    case 5: {
                        Screenshot.screenshot(RESULT_DIR, UDID, special_point_x, special_point_y);
                        new MonkeyTapSpecialPointEvent(driver, special_point_x, special_point_y).injectEvent();
                        eventcount = eventcount + 1;
                        System.out.println("---EVENT执行了：" + eventcount + "次---");
                        break;
                    }
                    case 6: {
                        new MonkeyHomeKeyEvent(driver, UDID, BUNDLEID).injectEvent();
                        eventcount = eventcount + 1;
                        System.out.println("---EVENT执行了：" + eventcount + "次---");
                        break;
                    }
                }
            }
            catch (Exception e) {
                driver.quit();
                init();
                lastTime -= endTime-startTime;
                startTime = System.currentTimeMillis();
                System.out.println("*******************************************\n\n\n" +
                        "请在命令行输入 macaca server --verbose 启动服务\n\n\n" +
                        "*******************************************\n");
            }
            if((endTime - startTime) > lastTime) {
                System.out.println("已运行" + (endTime - startTime)/60/1000 + "分钟，任务即将结束");
                break;
            }
        }

        driver.quit();
    }


    private void init() throws IOException, InterruptedException {
        driver = new MacacaClient();
        JSONObject porps = new JSONObject();
        porps.put("platformName", "ios");
        porps.put("reuse", 3);
        porps.put("bundleId", BUNDLEID);
        porps.put("app", APPPATH);
        porps.put("udid", UDID);
        porps.put("autoAcceptAlerts", true);
        porps.put("proxyPort", Integer.parseInt(PROXYPORT));
        JSONObject desiredCapabilities = new JSONObject();
        desiredCapabilities.put("desiredCapabilities", porps);
        desiredCapabilities.put("host", HOST);
        desiredCapabilities.put("port", Integer.parseInt(PORT));
        try {
            driver.initDriver(desiredCapabilities);
        } catch (Exception e) {
            System.out.println("*******************************************\n\n\n" +
                    "请在命令行输入 macaca server --verbose 启动服务\n\n\n" +
                    "*******************************************\n");
        }
        //启动app守护进程
        // Shell.launchAPP(UDID, BUNDLEID, startTime, TIMING);
    }
}
