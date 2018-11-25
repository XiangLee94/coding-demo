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
object KafkaSampleMain1 {
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
    val sparkConf = new SparkConf().setAppName("DirectKafkaWordCount").setMaster("local")
    val ssc = new StreamingContext(sparkConf, Seconds(10))


    // Create direct kafka stream with brokers and topics
    val topicsSet = topics.split(",").toSet
    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers)
    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc, kafkaParams, topicsSet)
    val testBro = "我是变量，取的到我吗？"
    println("driver 上运行")
    messages.map(f => (f._2, 1))
    messages.foreachRDD(rdd=>{


      println("rdd 到底是啥样子的：    "+JSON.toJSONString(rdd,SerializerFeature.WriteMapNullValue))
      val size = rdd.filter(_._2.contains('a')).mapPartitions(r=>{
        println("取之:"+testBro)
        println("mapPartition后特么是啥"+r+"             "+JSON.toJSONString(r,SerializerFeature.WriteMapNullValue))
        r.foreach(
          i=>println("每个都是啥"+i+"    "+JSON.toJSONString(i,SerializerFeature.WriteMapNullValue))
        )
        Iterator(r)
      }).count()
      println("有多少数据")

      rdd.collect().foreach(i=>println("collect 一遍之后是啥"+i+"            "+JSON.toJSONString(i,SerializerFeature.WriteMapNullValue)))

    })

    // Get the lines, split them into words, count the words and print
    //val lines = messages.map(_._2)
    //val words = lines.flatMap(_.split(" "))
    //val wordCounts = words.map(x => (x, 1L)).reduceByKey(_ + _)
    //wordCounts.print()

    // Start the computation
    ssc.start()
    ssc.awaitTermination()
  }
}