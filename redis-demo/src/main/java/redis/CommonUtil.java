package redis;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

//import java.io.IOException;
//import java.text.ParseException;
//import java.text.ParsePosition;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Properties;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;

public class CommonUtil {
	private static final String SUCCESS_FLAG = "1";
	private static final String ERROR_FLAG = "0";


    /**
     * 序列化
     * 
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
        }
        return null;
    }
    

	
	/**
	 * 
	 * 功能描述：将double类型的数据转换成以3位逗号隔开的字符串
	 *
	 * @return
	 * 
	 * @author 
	 *
	 * @since 2018年2月27日
	 *
	 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static String getFormatNumber(double amount){
	    DecimalFormat df = new DecimalFormat("#,###"); 
        return df.format(amount);
	}

    /**
     * 反序列化
     * 
     * @param bytes
     * @return
     */
    public static Object deserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {

        }
        return null;
    }
	
	/*public static String[] getLastDays(int n){
		if(n<1){
			return null;
		}
		String[] days = new String[n];
		
		for(int i=1;i<=n;i++){
			  Calendar calendar1 = Calendar.getInstance();
			  SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			  calendar1.add(Calendar.DATE, -i);
			  String daysAgo = sdf1.format(calendar1.getTime());
			 days[i-1]=daysAgo;
		}
		
		return days;
	}
	public static String getLastNDay(int n){
		if(n<1){
			return null;
		}
		
		  Calendar calendar1 = Calendar.getInstance();
		  SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		  calendar1.add(Calendar.DATE, -n);
		  String daysAgo = sdf1.format(calendar1.getTime());
		return daysAgo;
	}*/
	
	/**
	 * 加载配置文件
	 * 
	 * @return
	public static Properties loadConfig(String propertyName) {

		Properties props = new Properties();
		try {
			props.load(CommonUtil.class.getResourceAsStream("/" + propertyName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return props;
	}*/
	
	/**
	 * 判读str是否为null
	 * @return
	 */
	public static boolean strIsNull(String str){
		
		boolean flag = false;
		if(str == null){
			flag = true;
		}else if(str.trim().length() ==0){
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 指标参数获取
	 * @param propertyName 配置文件名称
	 * @param paramName	参数名称
	 * @return
	public static String getParamValue(String propertyName,String paramName){
		String paramValue = "";
		Properties prop = loadConfig(propertyName);
		paramValue = prop.getProperty(paramName);
		return paramValue;
	}*/
	
	/**
	 * 
	 * @return
	public static List<String> strToList(String str){
		List<String> strList = null;
		if(!strIsNull(str)){
			strList = new ArrayList<String>();
			String[] strArr = str.split(",");
			for(int i=0; i<strArr.length; i++){
				strList.add(strArr[i]);
			}
		}
		return strList;
	}*/
	
	/** 
	 * 随机指定范围内N个不重复的数 
	 * 最简单最基本的方法 
	 * @param min 指定范围最小值 
	 * @param max 指定范围最大值 
	 * @param n 随机数个数 
	public static int[] randomIntegers(int min, int max, int n){  
	    if (n > (max - min) || max < min|| n<=0) {  
	           return null;  
	       }  
	    int[] result = new int[n];
	    for(int i=0;i<n;i++){
	    	result[i]=-1;
	    }
	    int count = 0;  
	    while(count < n) {  
	        int num = (int) (Math.random() * (max - min)) + min;  
	        boolean flag = true;  
	        for (int j = 0; j < count+1; j++) {  
	            if(num == result[j]){  
	                flag = false;  
	                break;  
	            }  
	        }  
	        if(flag){  
	            result[count] = num;  
	            count++;  
	        }  
	    }  
	    return result;  
	}  */
	
	/*解析sql
	public static List<String> resolveSql(
				Map<String, String> checkParam, 
				String configFileName) throws Exception{
			
			//control层拼装查询条件
			
			List<String> sqlListRet = new ArrayList<String>();
			
			//分组条件
			String gourpStr = null;
			if(checkParam.containsKey("group")){
				gourpStr = checkParam.get("group");	
			}
			
			//排序条件
			String orderStr = null;
			if(checkParam.containsKey("group")){
				orderStr = checkParam.get("order");
			}							
			
			Properties prop = CommonUtil.loadConfig(configFileName);
			
			//是否配置多个group 
			String difgroupStr = prop.getProperty("difgroup");
			//是否根据查询参数查找要查询的列
			String difcheckStr = prop.getProperty("difcheck");
			//是否动态配置table
			String diftableStr = prop.getProperty("diftable");
			
			//一级参数按照查询时间划分出的类别
			String baseQuotaType = prop.getProperty("baseQuotaType");
			//按照,划分批次 
			String[] baseQuotaTypeArr = baseQuotaType.split(",");
			for(int i=0; i<baseQuotaTypeArr.length; i++){
				
				StringBuffer sqlStr = new StringBuffer("");
				//一级参数的名称
				String paramName = "";
				if(!strIsNull(difcheckStr)&& "true".equals(difcheckStr)){
					if(checkParam.containsKey("check"+baseQuotaTypeArr[i])){
						String checkName = checkParam.get("check"+baseQuotaTypeArr[i]);
						paramName = prop.getProperty(checkName);
					}else{
						throw  new Exception("找不到查询列！");
					}
				}else{
					paramName = prop.getProperty(baseQuotaTypeArr[i]);
				}
				
				//开始拼装sql
				sqlStr.append("select ");
				//拼装要查询的参数
				sqlStr.append(paramName);
				
				//拼装分组的参数
				if(!strIsNull(difgroupStr)&& "true".equals(difgroupStr)){
					sqlStr.append(checkParam.get("group"+baseQuotaTypeArr[i]));
				}
				if(checkParam.containsKey("param"+baseQuotaTypeArr[i])){
					sqlStr.append(checkParam.get("param"+baseQuotaTypeArr[i]));
				}
				sqlStr.append(" from ");
				//拼装表名
				if(checkParam.containsKey("table"+baseQuotaTypeArr[i])){
					String tableName = checkParam.get("table"+baseQuotaTypeArr[i]);
					sqlStr.append(prop.getProperty(tableName));
				}else{
					throw  new Exception("找不到表名！");
				}
				
				//拼装分组的参数
				if(!strIsNull(diftableStr)&& "true".equals(diftableStr)){
					sqlStr.append(checkParam.get("tableLeft"+baseQuotaTypeArr[i]));
				}
				
				sqlStr.append(" where 1=1 ");
				//拼装where查询条件
				sqlStr.append(checkParam.get("where"+baseQuotaTypeArr[i]));
				//拼装 group by 条件
				if(!CommonUtil.strIsNull(gourpStr)){
					sqlStr.append(" group by ");
					sqlStr.append(gourpStr);
				}
				//拼装order by 条件
				if(!CommonUtil.strIsNull(orderStr)){
					sqlStr.append(" order by ");
					sqlStr.append(orderStr);
				}
				sqlListRet.add(sqlStr.toString());
			}
			return sqlListRet;
		}*/
	
	
	/*解析sumsql
		public static List<String> resolveSumSql(
					Map<String, String> checkParam, 
					String configFileName) throws Exception{
				
				//control层拼装查询条件
				
				List<String> sqlListRet = new ArrayList<String>();
				
				//分组条件
				String gourpStr = null;
				if(checkParam.containsKey("group")){
					gourpStr = checkParam.get("group");	
				}
				
				//排序条件
				String orderStr = null;
				if(checkParam.containsKey("group")){
					orderStr = checkParam.get("order");
				}					
				
				Properties prop = CommonUtil.loadConfig(configFileName);
							
				
				//一级参数按照查询时间划分出的类别
				String sumQuotaType = prop.getProperty("sumQuotaType");
				
				
				//是否根据查询参数查找要查询的列
				String difSumcheckStr = prop.getProperty("difSumcheck");
				
				//按照,划分批次 
				String[] sumQuotaTypeArr = sumQuotaType.split(",");
				for(int i=0; i<sumQuotaTypeArr.length; i++){
					
					StringBuffer sqlStr = new StringBuffer("");
					//一级参数的名称
					String paramName = "";
					if(!strIsNull(difSumcheckStr)&& "true".equals(difSumcheckStr)){
						if(checkParam.containsKey("check"+sumQuotaTypeArr[i])){
							String checkName = checkParam.get("check"+sumQuotaTypeArr[i]);
							paramName = prop.getProperty(checkName);
						}else{
							throw  new Exception("找不到查询列！");
						}
					}else{
						paramName = prop.getProperty(sumQuotaTypeArr[i]);
					}
					
					
					//开始拼装sql
					sqlStr.append("select ");
					//拼装要查询的参数
					sqlStr.append(paramName);
					
					if(checkParam.containsKey("param"+sumQuotaTypeArr[i])){
						sqlStr.append(checkParam.get("param"+sumQuotaTypeArr[i]));
					}
					sqlStr.append(" from ");
					//拼装表名
					if(checkParam.containsKey("table"+sumQuotaTypeArr[i])){
						String tableName = checkParam.get("table"+sumQuotaTypeArr[i]);
						sqlStr.append(prop.getProperty(tableName));
					}else{
						throw  new Exception("找不到表名！");
					}
					
					sqlStr.append(" where 1=1 ");
					//拼装where查询条件
					sqlStr.append(checkParam.get("where"+sumQuotaTypeArr[i]));
					//拼装 group by 条件
					if(!CommonUtil.strIsNull(gourpStr)){
						sqlStr.append(" group by ");
						sqlStr.append(gourpStr);
					}
					//拼装order by 条件
					if(!CommonUtil.strIsNull(orderStr)){
						sqlStr.append(" order by ");
						sqlStr.append(orderStr);
					}
					sqlListRet.add(sqlStr.toString());
				}
				return sqlListRet;
			}*/
	
		/**
		 * 
		 * 功能描述：获取当天日期
		 *
		 * @return
		 * 
		 * @author Libin
		 *
		 * @since 2017年7月25日
		 *
		 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
		 */
		public static String getNowDate(){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Calendar  localTime = Calendar.getInstance();
			Date nowDate = localTime.getTime();
			return formatter.format(nowDate);
		}
		
	
		/**
		 * 方法说明：获取当年年初日期 . <BR>
		 * @see com.sunlands.eagle.action.ProvinceController <BR>
		 * @return
		 * @return: String
		 * @Author: hemengmeng <BR>
		 * @Datetime：2016年12月15日 下午4:25:24 <BR>
		public static String ThisYear() {
			Calendar  localTime = Calendar.getInstance();
	        int x = localTime.get(Calendar.YEAR);
	        return x + "-01" + "-01";
	    }*/

		
		/**
		 * 方法说明：获取当年年末日期 . <BR>
		 * @see com.sunlands.eagle.tool.CommonUtil <BR>
		 * @return
		 * @return: String
		 * @Author: hemengmeng <BR>
		 * @Datetime：2016年12月20日 下午5:07:42 <BR>
		public static String ThisYearEnd() {
			Calendar  localTime = Calendar.getInstance();
	        int x = localTime.get(Calendar.YEAR);
	        return x + "-12" + "-31";
	    }*/
		/**
		 * 方法说明：获取上年年初日期 . <BR>
		 * @see com.sunlands.eagle.action.ProvinceController <BR>
		 * @return
		 * @return: String
		 * @Author: hemengmeng <BR>
		 * @Datetime：2016年12月15日 下午5:05:13 <BR>
		public static String LastYear() {
			Calendar  localTime = Calendar.getInstance();
	        int x = localTime.get(Calendar.YEAR)-1;
	        return x + "-01" + "-01";
	    }*/
		
		
		/**
		 * 方法说明：获取上年年末日期 . <BR>
		 * @see com.sunlands.eagle.tool.CommonUtil <BR>
		 * @return
		 * @return: String
		 * @Author: hemengmeng <BR>
		 * @Datetime：2016年12月20日 下午5:08:12 <BR>
		public static String LastYearEnd() {
			Calendar  localTime = Calendar.getInstance();
	        int x = localTime.get(Calendar.YEAR)-1;
	        return x + "-12" + "-31";
	    }*/
		
		/**
		 * 方法说明：获取上上年年初日期 . <BR>
		 * @see com.sunlands.eagle.action.ProvinceController <BR>
		 * @return
		 * @return: String
		 * @Author: hemengmeng <BR>
		 * @Datetime：2016年12月15日 下午5:05:50 <BR>
		public static String BeforeLastYear() {
			Calendar  localTime = Calendar.getInstance();
	        int x = localTime.get(Calendar.YEAR)-2;
	        return x + "-01" + "-01";
	    }*/
		
		/**
		 * 方法说明：获取上上年年末日期 . <BR>
		 * @see com.sunlands.eagle.tool.CommonUtil <BR>
		 * @return
		 * @return: String
		 * @Author: hemengmeng <BR>
		 * @Datetime：2016年12月20日 下午5:08:45 <BR>
		public static String BeforeLastYearEnd() {
			Calendar  localTime = Calendar.getInstance();
	        int x = localTime.get(Calendar.YEAR)-2;
	        return x + "-12" + "-31";
	    }*/
		
		
		/**
		 * 从左边截取字符串
		 * @param str
		 * @param index
		 * @return
		 */
		public static String subStrFromLeft(String str, Integer index){
			String strRe = null;
				
			if(!strIsNull(str)){
				if(str.length()<index){
					index = str.length();
				}
				strRe = str.substring(0, index);
			}			
			return 	strRe;
		}
		
		/**
		 * 从右边截取字符串
		 * @param str
		 * @param index
		 * @return
		public static String subStrFromRight(String str, Integer index){
			String strRe = null;
				
			if(!strIsNull(str)){
				int length = str.length();
				strRe = str.substring(length-index, length);
			}			
			return 	strRe;
		}*/
		
		
		/**
		 * 将短时间格式字符串转换为时间 yyyy-MM-dd
		 *
		 * @param strDate
		 * @return
		public static Date strToDate(String strDate) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			ParsePosition pos = new ParsePosition(0);
			Date strtodate = formatter.parse(strDate, pos);
			return strtodate;
		}*/
		
		/**
		 * 将短时间格式时间转换为字符串yyyy-MM-dd
		 * @param dDate
		 * @return
		 */
		public static String dateToStr(Date dDate) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String sDate = formatter.format(dDate);
			return sDate;
		}
		
		/**
		 * 根据传入日期获取所在周末的日期
		 * @param dateStr
		 * @return
		// 获得本周星期日的日期
		public static String getSundayWeek(String sDate) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(strToDate(sDate));
			int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
			if(1 != dayWeek){
				cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); // 获取本周日的日期
				cal.add(Calendar.WEEK_OF_YEAR, 1);
				sDate = dateToStr(cal.getTime());
			}
			return sDate;
		}*/
		
		
		/**
		 * 方法说明：根据传入日期获取据周一日期 . <BR>
		 * @see com.sunlands.eagle.util.CommonUtil <BR>
		 * @param sDate
		 * @return
		 * @return: String
		 * @Author: hemengmeng <BR>
		 * @Datetime：2017年2月23日 下午4:53:37 <BR>
		public static String getMondayWeek(String sDate) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(strToDate(sDate));
			int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
			if(1 != dayWeek){
				cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 获取本周一的日期
//				cal.add(Calendar.WEEK_OF_YEAR, 1);
				sDate = dateToStr(cal.getTime());
			}else{
				cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 获取本周一的日期
				cal.add(Calendar.WEEK_OF_YEAR, -1);
				sDate = dateToStr(cal.getTime());
			}
			return sDate;
		}*/
		

		
		
		
		/**
		 * 方法说明：判断是否为空，true：不为空，false：为空 . <BR>
		 * @see com.sunlands.eagle.tool.CommonUtil <BR>
		 * @param str
		 * @return
		 * @return: boolean
		 * @Author: hemengmeng <BR>
		 * @Datetime：2016年12月16日 下午6:14:16 <BR>
		public static boolean IFNull(String str){
			boolean flag = true;
			if(str == null){
				flag = false;
			}
			if("null".equals(str.trim())){
				flag = false;
			}
			if(str.trim() == ""){
				flag = false;
			}
			if("".equals(str.trim())){
				flag = false;
			}
			if("''".equals(str.trim())){
				flag = false;
			}
			
			return flag;
		}*/
		
		/* 获取去年的当前日期
		public static String getLastYearDay(String sDate) {
			String str = "";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar lastYearDate = Calendar.getInstance();
			ParsePosition pos = new ParsePosition(0);
			lastYearDate.setTime(sdf.parse(sDate, pos));
			lastYearDate.add(Calendar.YEAR, -1);// 减一个月，变为下月的1号
			str = sdf.format(lastYearDate.getTime());

			return str;
		}*/
		
		/**
		 * 方法说明：昨天. <BR>
		 * @see com.sunlands.eagle.tool.CommonUtil <BR>
		 * @return
		 * @return: String
		 * @Author: hemengmeng <BR>
		 * @Datetime：2016年12月26日 下午1:37:20 <BR>
		public static String getLastDay(){
			Calendar   cal   =   Calendar.getInstance();
			  cal.add(Calendar.DATE,   -1);
			  String yesterday = new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());
			  return yesterday;
		}*/
		
		/**
		 * 方法说明： 当前月份 . <BR>
		 * @see com.sunlands.eagle.tool.CommonUtil <BR>
		 * @return
		 * @return: String
		 * @Author: hemengmeng <BR>
		 * @Datetime：2016年12月26日 下午1:37:28 <BR>
		public static String ThisMon() {
			Calendar  localTime = Calendar.getInstance();
	        int x = localTime.get(Calendar.YEAR);
	        int y = localTime.get(Calendar.MONTH)+1;
	        String m = y+"";
	        if(y<10){
	        	m = "0"+y;
	        }
	        return x + "-" + m + "-01";
	    }*/
		
		/**
		 * 方法说明：往前推90天 . <BR>
		 * @see com.sunlands.eagle.tool.CommonUtil <BR>
		 * @return
		 * @return: String
		 * @Author: hemengmeng <BR>
		 * @Datetime：2016年12月26日 下午1:37:56 <BR>
		public static String getLast90Day(){
			Calendar   cal   =   Calendar.getInstance();
			cal.add(Calendar.DATE,   -91);
			String thisY = ThisYear();
			String yesterday = new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());
			if(!thisY.substring(0, 4).equals(yesterday.substring(0, 4)) ){
				yesterday = thisY;
			}
			return yesterday;
		}*/
		
		/**
		 * 获取传入时间所在月的第一天日期
		 * @param sDate
		 * @return
		public static String getMonthFirstDay(String sDate){
			String strRe = "";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(strToDate(sDate));
			cal.add(Calendar.MONTH, 0);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			strRe = sdf.format(cal.getTime());
			return strRe;
		}*/
		
		/**
		 * 方法说明：获取日期对应上月当天 . <BR>
		 * @see com.sunlands.eagle.util.CommonUtil <BR>
		 * @param sDate
		 * @return
		 * @return: String
		 * @Author: hemengmeng <BR>
		 * @Datetime：2017年2月23日 下午6:16:28 <BR>
		public static String getLastMonthDay(String sDate){
			String strRe = "";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(strToDate(sDate));
			cal.add(Calendar.MONTH, -1);
//			cal.set(Calendar.DAY_OF_MONTH, 1);
			strRe = sdf.format(cal.getTime());
			return strRe;
		}*/
		
		/**
		 * 方法说明：获取日期对应下月当天 . <BR>
		 * @see com.sunlands.eagle.util.CommonUtil <BR>
		 * @param sDate
		 * @return
		 * @return: String
		 * @Author: hemengmeng <BR>
		 * @Datetime：2017年2月23日 下午6:16:28 <BR>
		public static String getAfertMonthDay(String sDate){
			String strRe = "";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(strToDate(sDate));
			cal.add(Calendar.MONTH, 1);
			strRe = sdf.format(cal.getTime());
			return strRe;
		}*/
		
		/**
		 * 方法说明：日期加/减N天 . <BR>
		 * @see com.sunlands.eagle.util.CommonUtil <BR>
		 * @param sDate
		 * @param n
		 * @return
		 * @return: String
		 * @Author: hemengmeng <BR>
		 * @Datetime：2017年2月24日 上午10:31:25 <BR>
		public static String getLastDay(String sDate,int n){
			String strRe = "";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(strToDate(sDate));
			cal.add(Calendar.DATE,   n);
			strRe = sdf.format(cal.getTime());
			return strRe;
		}*/


		/*public static String getLastMoth(String Date){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String  lastday = "";
			try {
				Calendar cale = Calendar.getInstance();
				cale.setTime(format.parse(Date));
				cale.add(Calendar.MONTH, 1);
				cale.set(Calendar.DAY_OF_MONTH, 0);
				lastday = format.format(cale.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
	        
			return lastday;
		}*/
		
		/**
		 * 两个时间之间的天数
		 */
		public static long getDistanceYears(String time1,String time2){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
			 Date one;  
		     Date two;  
		     long years=0,timeStamp1=0,timeStamp2=0; 
		     try {  
		    	if(time1.contains("-")){
		    		    one = df.parse(time1);  
			            timeStamp1 = one.getTime();  
		    	}else{
		    		timeStamp1 = Long.valueOf(time1);
		    	}
		    	if(time2.contains("-")){
	    		    two = df.parse(time2);  
		            timeStamp2 = two.getTime();  
		    	}else{
		    		timeStamp2 = Long.valueOf(time2);
		    	}
	            long diff ; 
	            if(timeStamp1<timeStamp2) {  
	                diff = timeStamp2 - timeStamp1;  
	            } else {  
	                diff = timeStamp1 - timeStamp2;  
	            }  
	            long days = diff / (1000 * 60 * 60 * 24);  
	            years = days/365;
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
		    return years;  
		     			
		}
		
		/**
		 * 两个时间之间的天数
		 * @param Date
		 * @return
		public static long getDistanceDays(String Date){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
			 Date one;  
		     Date two;  
		     long days=0; 
		     try {  
	            one = df.parse(subStrFromLeft(Date,10));  
	            two = df.parse(subStrFromRight(Date, 10));  
	            long time1 = one.getTime();  
	            long time2 = two.getTime();  
	            long diff ;  
	            if(time1<time2) {  
	                diff = time2 - time1;  
	            } else {  
	                diff = time1 - time2;  
	            }  
	            days = diff / (1000 * 60 * 60 * 24)+1;  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
		    return days;  
		     			
		}*/
		
		/*public static List<String> getDistanceTimeStr(String Date,String dateType,long distanceDay){
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			List<String>dateArr = new ArrayList<String>();
			//int num = Integer.parseInt(dateType);
			try{
				String startStr  = subStrFromLeft(Date,10);
				Date startDate = df.parse(startStr); 
				if("1".equals(dateType)){
					for(int i=0; i<distanceDay; i++){
						dateArr.add(getLastDay(df.format(startDate),i));
					}
				}else if("2".equals(dateType)){
					String startSunday = getSundayWeek(startStr);
					dateArr.add(startSunday);
					for(int i =1;i< distanceDay; i++ ){
						startSunday = getLastDay(startSunday, 7);
						dateArr.add(startSunday);
					}					
				}else if("3".equals(dateType)){
					String startMonth = getMonthFirstDay(startStr);
					dateArr.add(startMonth);
					for(int i =1;i< distanceDay; i++ ){
						startMonth = getAfertMonthDay(startMonth);
						dateArr.add(startMonth);
					}
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}

			return dateArr;
		}		*/		
		/**
		 * 
		 * 功能描述：获取上年
		 *
		 * @return
		 * 
		 * @author Libin
		 *
		 * @since 2017年6月29日
		 *
		 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
		public static String getBeforeYear(){
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.YEAR, -1);
			Date lastYear = c.getTime();
			return df.format(lastYear);			
		}*/
		
		/**
		 * 
		 * 功能描述：获取今年
		 *
		 * @return
		 * 
		 * @author Libin
		 *
		 * @since 2017年6月29日
		 *
		 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
		public static String getNowYear(){
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			Date lastYear = new Date();
			return df.format(lastYear);			
		}*/
		
		/**
		 * 
		 * 功能描述：获取上个月
		 *
		 * @return
		 * 
		 * @author Libin
		 *
		 * @since 2017年6月29日
		 *
		 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
		public static String getBeforeMonth(){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.MONTH, -1);
			Date lastMonth = c.getTime();
			return df.format(lastMonth);			
		}*/
		
		/**
		 * 
		 * 功能描述：获取当前月
		 *
		 * @return
		 * 
		 * @author Libin
		 *
		 * @since 2017年7月5日
		 *
		 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
		public static String thisMonNum() {
			Calendar  localTime = Calendar.getInstance();
	        Integer y = localTime.get(Calendar.MONTH)+1;
	        return y.toString();
	    }*/
		
		
		
		
		/**
		 * 
		 * 功能描述：是否符合正则表达式
		 *
		 * @param regex	正则表达式
		 * @param sourceStr 要匹配的str
		 * @return
		 * 
		 * @author Libin
		 *
		 * @since 2017年7月25日
		 *
		 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
		public static boolean isConformRegex(String regex,String sourceStr){
			boolean flag = false;
			
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(sourceStr);
			while (m.find()) {
				flag = true;
			}
			return flag;
		}*/
		
		/**
		 * 
		 * 功能描述：获取符合正则的str
		 *
		 * @param regex
		 * @param sourceStr
		 * @return
		 * 
		 * @author Libin
		 *
		 * @since 2017年7月25日
		 *
		 * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
		public static String conformRegexStr(String regex,String sourceStr){
			
			String returnStr = "";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(sourceStr);
			while (m.find()) {
				returnStr = m.group();
			}
			return returnStr;
		}			*/
		
		
}
