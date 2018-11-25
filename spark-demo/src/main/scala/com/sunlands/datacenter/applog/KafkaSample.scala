package com.sunlands.datacenter.applog

import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.LongWritable
import org.apache.spark.SparkConf
import org.apache.spark.rdd.{RDD, UnionRDD}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat

import scala.reflect.ClassTag


object KafkaSampleMain {

  val checkpointDirectory = "hdfs://192.168.0.80:9000/test_app_log/checkpoint"

  def main(args: Array[String]): Unit = {
    if (args.length < 1) {
      System.err.println(s"""
                            |Usage: DealAppLog <hdfs>
                            |  <hdfs> is a url of a hdfs
                            |
        """.stripMargin)
      System.exit(1)
    }

    val hdfsDir = args(0)
    //StreamingExamples.setStreamingLogLevels()


    // Create context with 2 second batch interval
    val ssc = StreamingContext.getOrCreate(checkpointDirectory,createContext)
/*
    // Create direct  stream with dirname
    val dstream = namedTextFileStream(ssc, hdfsDir)
    def byFileTransformer(filename: String)(rdd: RDD[String]): RDD[(String, String)] =
      rdd.map(line =>{(filename, line)} )
    val transformed = dstream.
      transform(rdd => transformByFile(rdd, byFileTransformer))

    val conPool = Dispatcher.getC3p0DateSource()

    transformed.foreachRDD(
      rdd => {
        if( ! rdd.isEmpty()){
          val start  = System.currentTimeMillis()
          val table = Option(rdd.first()._1).map(i => i.substring(i.lastIndexOf("/")+1)).map(i => i.substring(i.lastIndexOf("log")+3)).getOrElse("")
          if(!table.equals("")){
            println(s"表名是 $table")
            rdd.mapPartitions(r => {
              val conn = conPool.getConnection
              val sql = s"INSERT INTO test_action_info_$table (user_id, user_state, net_type, province, city, device_model, os_version, app_version, app_source, action_id, action_time, action_key, page_key, delete_flag) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
              val pstm = conn.prepareStatement(sql)
              conn.setAutoCommit(false)
              val result = Dispatcher.dispatch(r,pstm)
              try{
                pstm.executeBatch()
                conn.commit()
              }catch {
                case e:Exception => e.printStackTrace()
              }
              Iterator(result)
            }).collect()
          }
          val end = System.currentTimeMillis()
          println("每个rdd处理耗时"+(end - start)/1000)
        }
        rdd.localCheckpoint()
    })
*/

    ssc.start()
    ssc.awaitTermination()
  }

  def createContext(): StreamingContext ={

    val conf = new SparkConf().setAppName("DealAppLog").setMaster("local")
      .set("spark.streaming.driver.writeAheadLog.closeFileAfterWrite","true")
     .set("spark.streaming.receiver.writeAheadLog.closeFileAfterWrite","true")

    val ssc = new StreamingContext(conf, Seconds(20))
    if(checkpointDirectory!=null &&checkpointDirectory.length()>0){
      ssc.checkpoint(checkpointDirectory)
    }

    val dstream = namedTextFileStream(ssc, "hdfs://192.168.0.80:9000/test_app_log")
    def byFileTransformer(filename: String)(rdd: RDD[String]): RDD[(String, String)] =
      rdd.map(line =>{(filename, line)} )
    val transformed = dstream.
      transform(rdd => transformByFile(rdd, byFileTransformer))

    val conPool = Dispatcher.getC3p0DateSource()

    transformed.foreachRDD(
      rdd => {
        if( ! rdd.isEmpty()){
          val start  = System.currentTimeMillis()
          val table = Option(rdd.first()._1).map(i => i.substring(i.lastIndexOf("/")+1)).map(i => i.substring(i.lastIndexOf("log")+3)).getOrElse("")
          if(!table.equals("")){
            println(s"表名是 $table")
            rdd.mapPartitions(r => {
              val conn = conPool.getConnection
              val sql = s"INSERT INTO test_action_info_$table (user_id, user_state, net_type, province, city, device_model, os_version, app_version, app_source, action_id, action_time, action_key, page_key, delete_flag) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
              val pstm = conn.prepareStatement(sql)
              conn.setAutoCommit(false)
              val result = Dispatcher.dispatch(r,pstm)
              try{
                pstm.executeBatch()
                conn.commit()
              }catch {
                case e:Exception => e.printStackTrace()
              }
              Iterator(result)
            }).collect()
          }
          val end = System.currentTimeMillis()
          println("每个rdd处理耗时"+(end - start)/1000)
        }
        rdd.localCheckpoint()
      })


    ssc
  }

  //create dStream and set rdd's name by using filename
  def namedTextFileStream(ssc: StreamingContext, directory: String): DStream[String] =
    ssc.fileStream[LongWritable, Text, TextInputFormat](directory)
      .transform( rdd =>
        new UnionRDD(rdd.context,
          rdd.dependencies.map( dep =>
            dep.rdd.asInstanceOf[RDD[(LongWritable, Text)]].map(_._2.toString).setName(dep.rdd.name)
          )
        )
      )

  //rdd transform operation
  def transformByFile[U: ClassTag](unionrdd: RDD[String],
                                   transformFunc: String => RDD[String] => RDD[U]): RDD[U] = {
    new UnionRDD(unionrdd.context,
      unionrdd.dependencies.map{ dep =>
        if (dep.rdd.isEmpty) None
        else {
          val filename = dep.rdd.name
          Some(
            transformFunc(filename)(dep.rdd.asInstanceOf[RDD[String]])
              .setName(filename)
          )
        }
      }.flatten
    )
  }

}