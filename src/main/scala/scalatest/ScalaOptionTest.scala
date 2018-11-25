package scalatest

object ScalaOptionTest {
  def main(args: Array[String]): Unit = {
      val user1 = new User("laoli",12)
      val user2 = new User("laozhang",15)
      val user3 = new User("laoliu",17)
      val userMap = Map(1->user1,2->user2,3->user3)
      user1.setA(2)
      println(user1.getA())
      val user4 = user1
      user4.setA(1000)
      println(user1.getA())
  }
}



final class User(name:String , age :Int){
  val isAdult = {
    age > 18
  }
  val userName = name
  val userAge = age
  var a = 0
  def getName() : String = {
    userName
  }

  def getage() : Int = {
    userAge
  }

  override def toString: String = {
    "姓名 "+userName + "  年龄 " + userAge
  }
  def setA(value:Int): Unit ={
    a= value
  }
  def getA() :Int = {
    a
  }
}