package com.sunlands.datacenter.mail.task;

import com.sunlands.datacenter.mail.mail.CreateMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;

@Component
public class TransfformTask {
    @Autowired
    @Qualifier("primaryJdbcTemplate")
    protected JdbcTemplate jdbcTemplate1;

    @Scheduled(cron = "*/10 * * * * ?")
    public void startTask() throws IOException, MessagingException {
        System.out.println("insert start");
        CreateMail.test();
    }









}
