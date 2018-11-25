package com.sunlands.datacenter.canal2mysql.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TransfformTask {
    @Autowired
    @Qualifier("primaryJdbcTemplate")
    protected JdbcTemplate jdbcTemplate1;

    @Scheduled(cron = "*/10 * * * * ?")
    public void startTask(){
        System.out.println("insert start");
        long st = System.currentTimeMillis();
        String values = "('2325290', 1, 1, '-1', '-1', 'iPhone6SPlus', 'ios11.1.2', '2.9.4', 'AppStore', '1600796;1599531;1600237;1602309;1600942;1602245;1600192', '2017-12-05 18:51:45', 'pullrefresh', 'homepage', 0, '2018-3-22 09:46:14')";
        String pre = "INSERT INTO test_table ( user_id, user_state, net_type, province, city, device_model, os_version, app_version, app_source, action_id, action_time, action_key, page_key, delete_flag, create_time) VALUES ";
        StringBuilder sql = new StringBuilder(pre);
        for (int i = 0; i <1000 ; i++) {
            sql.append(values+",");
        }
        sql.append(values);
        jdbcTemplate1.batchUpdate(sql.toString());
        long ed = System.currentTimeMillis();
        System.out.println("insert end time cost"+ (ed-st));

        System.out.println("every insert start");
        long st1 = System.currentTimeMillis();
        String sql1 = "INSERT INTO test_table ( user_id, user_state, net_type, province, city, device_model, os_version, app_version, app_source, action_id, action_time, action_key, page_key, delete_flag, create_time) VALUES ('2325290', 1, 1, '-1', '-1', 'iPhone6SPlus', 'ios11.1.2', '2.9.4', 'AppStore', '1600796;1599531;1600237;1602309;1600942;1602245;1600192', '2017-12-05 18:51:45', 'pullrefresh', 'homepage', 0, '2018-3-22 09:46:14') ";
        String[] sqls = new String[1000];
        for (int i = 0; i < 1000; i++) {
            sqls[i] = sql1;
        }
        jdbcTemplate1.batchUpdate(sqls);
        long ed1 = System.currentTimeMillis();
        System.out.println("insert end time cost"+ (ed1-st1));

    }









}
