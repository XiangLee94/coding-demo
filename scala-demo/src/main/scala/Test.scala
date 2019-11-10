import org.apache.hadoop.hbase.util.{Bytes, MD5Hash}

import scala.collection.mutable.ListBuffer

object Test {
  val ROWKEY_SIZE_LIMIT = 1024 * 4
  def hashRowKeyGenerator(md5Length: Int, inId: String, sys_code: String): String = {
    var id = inId
    if (id.length() > ROWKEY_SIZE_LIMIT) {
      id = id.substring(0, 300)
    }
    val idBytes = Bytes.toBytes(id)
    val sysCode = if (sys_code == null || sys_code.isEmpty() || sys_code.length() < 2) {
      ""
    } else {
      Option(sys_code).getOrElse("no").substring(0, 2)
    }
    MD5Hash.getMD5AsHex(idBytes).substring(0, md5Length).concat(id).concat(sysCode) //substring不包括最后一个字节
  }

  def main(args: Array[String]): Unit = {
   val a = new ListBuffer[String]
    a+=("1")
    a+=("2")
    a+=("3")
    a+=("4")
    val list = a.toList


    list.toIterator.foreach(println)

    list.foreach(println)
  }
}
