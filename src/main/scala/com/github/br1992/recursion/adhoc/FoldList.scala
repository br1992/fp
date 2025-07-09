package com.github.br1992.recursion.adhoc

import com.github.br1992.recursion.data.{Cons, ConsList, Nil}

object FoldList {

  def main(args: Array[String]): Unit = {
    val list = Cons(1, Cons(2, Cons(3, Nil)))

    println(sumList(list))
  }

  def sumList(list: ConsList[Int]): Int = {
    list.match {
      case Nil => 0
      case Cons(head, tail) => head + sumList(tail)
    }
  }

}
