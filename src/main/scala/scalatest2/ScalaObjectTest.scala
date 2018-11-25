package scalatest2

import scalatest.Person

object ScalaObjectTest {

  def main(args: Array[String]): Unit = {
    val a = (1l+""+2l)
    val b = (5l+""+5l)
    print(a+"  "+b)
  }

  def testOption : Unit = {

  }


  def testArray : Unit = {
    val arr = new Array[String](3)
    arr(0) = "laoli"
    arr(1) = "laoliu"
    arr(2) = "laowang"
    val Array(name1,name2,name3)=arr
    print(name3)
  }

}