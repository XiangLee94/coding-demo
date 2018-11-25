package scalatest

import scala.reflect.ClassTag

object ScalaObjectTest {
  def main(args: Array[String]): Unit = {
    val p1 = new Person()
    val p2 = new Person()
    val list  = List(p1,p2)
    list.map(_.d).map(System.out.println)
  }
  def max(a:Int,b:Int) :Int = {
    if(a>b) return a
    else b

  }
}
class Person private[scalatest]{
  @transient
  val a : Option[String] = None
  val b = 2
  val c = 3
  var d = 4
}

