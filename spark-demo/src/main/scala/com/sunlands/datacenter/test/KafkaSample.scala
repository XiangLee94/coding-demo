package com.sunlands.datacenter.test

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializerFeature
import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils

/**
  * /data/soft/kafka_2.11-1.0.0/bin
  * ./kafka-console-producer.sh --broker-list 192.168.0.80:9092 -topic t1
  */
object KafkaSampleMain {
  def main(args: Array[String]): Unit = {
    if (args.length < 2) {
      System.err.println(s"""
                            |Usage: DirectKafkaWordCount <brokers> <topics>
                            |  <brokers> is a list of one or more Kafka brokers
                            |  <topics> is a list of one or more kafka topics to consume from
                            |
        """.stripMargin)
      System.exit(1)
    }

    //StreamingExamples.setStreamingLogLevels()

    val Array(brokers, topics) = args

    // Create context with 2 second batch interval
    val sparkConf = new SparkConf().setAppName("DealAppLog").setMaster("local")
    val ssc = new StreamingContext(sparkConf, Seconds(20))

    // Create direct  stream with dirname
    val messages1 = ssc.textFileStream("hdfs://192.168.2.139:9000/test_app_log/")
    println("driver 上运行")
    messages1.foreachRDD(
      rdd => {
        rdd.mapPartitions(lines =>{
          var a = 0
          var b = 0
          lines.foreach( line =>{
            if(line.contains("aaa"))
              a += 1
            else
              b += 1
            println("我是谁，我在哪"+line)
          })
          Iterator((a,b))
      }).collect().foreach(a=>{
          print(s"SUCCESS:${a._1}  and  FAIL:${a._2}")
        })
    })

    ssc.start()
    ssc.awaitTermination()
  }
}