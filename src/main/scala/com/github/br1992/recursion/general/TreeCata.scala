package com.github.br1992.recursion.general

import cats.Functor
import com.github.br1992.recursion.data.{BinTreeF, BinTreeNode, EmptyF, Fix, NodeF}
import com.github.br1992.recursion.scheme.Cata
import com.github.br1992.recursion.typeClasses.Functor.binTreeF

object TreeCata {

  def main(args: Array[String]): Unit = {
    val tree = fixNode(
      1,
      fixNode(2,
        fixNode(4, fixEmpty, fixEmpty),
        fixNode(5, fixEmpty, fixEmpty),
      ),
      fixNode(3,
        fixNode(6, fixEmpty, fixEmpty),
        fixNode(7, fixEmpty, fixEmpty),
      )
    )

    given functor: Functor[BinTreeNode[Int]] = binTreeF[Int]

    val result = Cata.bottomUp[[F] =>> BinTreeF[F, Int], Int] {
      case EmptyF => 0
      case NodeF(value, left, right) => value + left + right
    }(tree)

    println(result)
  }

  def fixNode[A](value: A, left: Fix[BinTreeNode[A]], right: Fix[BinTreeNode[A]]): Fix[BinTreeNode[A]] = {
    Fix(NodeF(value, left, right))
  }

  val fixEmptyMemo = Fix(EmptyF)

  def fixEmpty[A]: Fix[BinTreeNode[A]] = fixEmptyMemo.asInstanceOf
  
}
