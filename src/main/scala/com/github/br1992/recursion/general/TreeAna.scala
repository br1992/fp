package com.github.br1992.recursion.general

import cats.Functor
import com.github.br1992.recursion.data.{BinTreeF, BinTreeNode, EmptyF, Fix, NodeF}
import com.github.br1992.recursion.scheme.Ana
import com.github.br1992.recursion.typeClasses.Functor.binTreeF

object TreeAna {

  def main(args: Array[String]): Unit = {
    val seed = 1
    val levels = 3

    given functor: Functor[BinTreeNode[Int]] = binTreeF[Int]

    val result = Ana.topDown[BinTreeNode[Int], (Int, Int)] {
      case (_, level) if level <= 0 => EmptyF
      case (value, level) =>
        NodeF(value, (value * 2, level - 1), (value * 2 + 1, level - 1))
    }((seed, levels))

    println(result)
  }

}
