/**
 * ProjectName:canal-dao<BR>
 * File name:  canalSampleDao.java     <BR>
 * Author: hemengmeng  <BR>
 * Project:canal-dao    <BR>
 * Version: v 1.0      <BR>
 * Date: 2017年3月8日 上午10:33:52 <BR>
 * Description:     <BR>
 * Function List:  <BR>
 */ 

package com.sunlands.datacenter.mail.dao;

import org.apache.ibatis.annotations.*;

/**

 */
public interface BusiDao {
	
    @Select("$sql")
	public String select(@Param("sql") String sql);
}
