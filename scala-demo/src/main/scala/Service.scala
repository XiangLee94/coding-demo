object Service {
  val PvlPar = "(t_pv_log.*)".r
  val IslPar = "(t_info_submit_log.*)".r
  def main(args: Array[String]): Unit = {
    val str = "t_pv_log_11"
    str match{
      case "ent_ord_details" =>
        println("ent_ord_details")
      case "t_user_info" =>
        println("t_user_info")
      case PvlPar(str) =>
        println("t_pv_log")
      case IslPar(str) =>
        println("t_info_submit_log")
    }
  }
}
