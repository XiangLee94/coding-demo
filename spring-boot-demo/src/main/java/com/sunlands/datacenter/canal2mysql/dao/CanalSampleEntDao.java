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

package com.sunlands.datacenter.canal2mysql.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 功能描述： .  <BR>
 * 历史版本: <Br>
 * 开发者: hemengmeng  <BR>
 * 时间：2017年3月8日 上午10:33:52  <BR>
 * 变更原因：    <BR>
 * 变化内容 ：<BR>
 * 首次开发时间：2017年3月8日 上午10:33:52 <BR>
 * 描述：   <BR>
 * 版本：V1.0
 */
public interface CanalSampleEntDao {
	
	@Insert("${sql}")
	int insertData(@Param("sql") String sql);

	@Update("${sql}")
	int updateData(@Param("sql") String sql);
	
	@Delete("${sql}")
	int deleteData(@Param("sql") String sql);
}
