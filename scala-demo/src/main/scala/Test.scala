import org.apache.hadoop.hbase.util.{Bytes, MD5Hash}

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
    print(hashRowKeyGenerator(4, "5144802", "finance"))
  }
}
