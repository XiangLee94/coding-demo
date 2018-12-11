package com.lee.hbasedemo.test;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WebLogMapper implements RowMapper<WebLogDTO> {

    @Override
    public WebLogDTO mapRow(ResultSet resultSet, int i) throws SQLException {

        WebLogDTO webLogDTO = new WebLogDTO();
        webLogDTO.setId(resultSet.getString("id"));
        webLogDTO.setSystem_id(resultSet.getString("system_id"));
        webLogDTO.setUser_identify(resultSet.getString("user_identify"));
        webLogDTO.setEvent_id_str(resultSet.getString("event_id_str"));
        webLogDTO.setUnfixed_param(resultSet.getString("unfixed_param"));
        webLogDTO.setUrl(resultSet.getString("url"));
        webLogDTO.setIp(resultSet.getString("ip"));
        webLogDTO.setRegion(resultSet.getString("region"));
        webLogDTO.setSend_time(resultSet.getString("send_time"));
        webLogDTO.setCreate_time(resultSet.getString("create_time"));
        return webLogDTO;
    }
}
