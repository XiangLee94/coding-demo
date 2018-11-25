package com.sunlands.datacenter.applog

import java.sql.{ PreparedStatement}
import java.util.Properties
import java.lang.Thread
import java.util
import java.util.concurrent.{Callable, Executors, Future}
import scala.collection.JavaConversions._
import com.alibaba.fastjson.{JSON}

//deal every line and seriallize into db
object Dispatcher {
  val  fixedThreadPool = Executors.newFixedThreadPool(20);

  def dispatch(records: Iterator[(String, String)], pstm : PreparedStatement) : (Int,Int) = {

    def setValue(pstm :PreparedStatement,obj: DataObject) :Unit = {
      pstm.setString(1,Option(obj.getUserId).getOrElse(""))
      pstm.setInt(2,Integer.valueOf(obj.getUserState))
      pstm.setInt(3,Integer.valueOf(obj.getNetType))
      pstm.setString(4,Option(obj.getProvince).getOrElse(""))
      pstm.setString(5,Option(obj.getCity).getOrElse("") )
      pstm.setString(6,Option(obj.getDeviceModel).getOrElse(""))
      pstm.setString(7,Option(obj.getOsVersion).getOrElse(""))
      pstm.setString(8,Option(obj.getAppVersion).getOrElse(""))
      pstm.setString(9,Option(obj.getAppSource).getOrElse(""))
      pstm.setString(10,Option(obj.getActionId).getOrElse(""))
      pstm.setString(11,Option(obj.getActionTime).getOrElse(""))
      pstm.setString(12,Option(obj.getActionKey).getOrElse(""))
      pstm.setString(13,Option(obj.getPageKey).getOrElse(""))
      pstm.setInt(14,Integer.valueOf(obj.getDeleteFlag))
    }
    var sCount = 0
    var fCount = 0
    val futures = new util.ArrayList[Future[util.List[DataObject]]]();
    records.foreach(r =>{
      try{
        if(r._2.indexOf("data") > 2){
          val value = r._2.substring(r._2.indexOf("data")-2);
          val dataObjectDto = JSON.parseObject(value,classOf[LineDTO])
          val s = SecurityUtil.decrypt(dataObjectDto.getData()(0),"XeXWDrMBAN[[XNDL");

          if(s.indexOf("fileURL") > 0){
            val urlDTO = JSON.parseObject(s,classOf[FileURLDTO])
            //The file server cannot withstand requests that are too fast
            Thread.sleep(20)
            val task : Callable[util.List[DataObject]] = new Callable[util.List[DataObject]] {
              override def call(): util.List[DataObject] = {
                try {
                  val lines = DownLoadUtils.downLoadByHttpConn(urlDTO.getFileURL)
                  DownLoadUtils.transFormToObject(lines)
                }catch {
                  case e:Exception => e.printStackTrace()
                    new util.ArrayList[DataObject]()
                }
              }
            }
            futures.add(fixedThreadPool.submit(task))
          }else{
            val dataObject = JSON.parseObject(s,classOf[DataObject])
            setValue(pstm,dataObject)
            pstm.addBatch()
            sCount += 1
          }
        }
      }catch{
        case e:Exception => e.printStackTrace()
          fCount += 1
      }
    }
    )
    for( r <- futures ){
      for( i<-r.get()){
        setValue(pstm,i)
        pstm.addBatch()
        sCount += 1
      }
    }
    (sCount,fCount)
  }
  def getC3p0DateSource(): ComboPooledDataSource ={
    val dataSource : ComboPooledDataSource = new ComboPooledDataSource(true)
    val prop =new Properties();
    val fis =  Thread.currentThread().getContextClassLoader()
      .getResourceAsStream("prop.properties")
    prop.load(fis);
    dataSource.setJdbcUrl(prop.getProperty("url"))
    dataSource.setDriverClass(prop.getProperty("driverClassName"))
    dataSource.setUser(prop.getProperty("username"))
    dataSource.setPassword(prop.getProperty("password"))
    dataSource.setMaxPoolSize(Integer.valueOf(prop.getProperty("maxPoolSize")))
    dataSource.setMinPoolSize(Integer.valueOf(prop.getProperty("minPoolSize")))
    dataSource.setAcquireIncrement(Integer.valueOf(prop.getProperty("acquireIncrement")))
    dataSource.setInitialPoolSize(Integer.valueOf(prop.getProperty("initialPoolSize")))
    dataSource.setMaxIdleTime(Integer.valueOf(prop.getProperty("maxIdleTime")))
    dataSource
  }

}
