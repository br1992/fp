package com.github.br1992.recursion.adhoc

import com.github.br1992.recursion.data.{Cons, ConsList, Nil}

object UnfoldList {

  def main(args: Array[String]): Unit = {
    val seed = 3

    println(unfoldList(3))
  }

  def unfoldList(seed: Int): ConsList[Int] = {
    if (seed <= 0) {
      Nil
    } else {
      Cons(seed, unfoldList(seed - 1))
    }
  }

}
