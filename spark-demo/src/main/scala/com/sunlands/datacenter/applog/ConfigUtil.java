package com.sunlands.datacenter.applog;

import java.util.ResourceBundle;

/**
 * 加载配置文件
 *
 * @author 宣广海
 *
 * @version
 *
 * @since 2014年3月31日
 *
 * @update [刘曙][140408][增加对不同配置文件的支持]
 */
public class ConfigUtil {

    private static Object lock = new Object();
    /**
     *
     * 功能描述：读取Properties文件方法
     *
     * @param configName
     *            文件名称
     * @param key
     *            取字段名称
     * @return
     *
     * @author 陶盛龙
     *
     * @since 2014年5月22日
     *
     * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
     */
    public static String readProperties(String configName, String key) {
        String messageUrl = "";
        synchronized (lock) {
            messageUrl = ResourceBundle.getBundle(configName).getString(key);
        }
        return messageUrl;
    }
}
