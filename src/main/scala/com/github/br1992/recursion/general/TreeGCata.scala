package com.github.br1992.recursion.general

import cats.Functor
import cats.derived.semiauto
import com.github.br1992.recursion.data.*
import com.github.br1992.recursion.scheme.{Cata, GCata}
import com.github.br1992.recursion.typeClasses.{Cofree, Comonad, Identity, Recursive}

object TreeGCata {

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

    given r: Recursive[Fix[BinTreeNode[Int]], BinTreeNode[Int]] = Recursive.fix()
    given c: Comonad[Identity] = Comonad.identity()
    given f: Functor[BinTreeNode[Int]] = semiauto.functor
    given cA: Comonad[[A] =>> Cofree[BinTreeNode[Int], A]] = Comonad.cofree[BinTreeNode[Int]]
    given cf: Functor[Identity] = semiauto.functor
    given cAf: Functor[[A] =>> Cofree[BinTreeNode[Int], A]] = semiauto.functor

    val result = GCata.gcata[Fix[BinTreeNode[Int]], BinTreeNode[Int], Identity, Int](GCata.distCata) {
      case n @ NodeF(v, Identity(l), Identity(r)) => {
        println(n)
        v + l + r
      }
      case EmptyF => 0
    }(tree)
    println(result)

    println("---")

    val result2 = GCata.gcata[Fix[[F] =>> BinTreeF[F, Int]], [F] =>> BinTreeF[F, Int], [A] =>> Cofree[[F] =>> BinTreeF[F, Int], A], Int](GCata.distHisto) {
      case EmptyF => 0
      case n@NodeF(v, Cofree(lv, lAF), Cofree(rv, rAF)) => {
        println(n)
        v + lv + rv
      }
    }(tree)

    println(result2)
  }

  def fixNode[A](value: A, left: Fix[BinTreeNode[A]], right: Fix[BinTreeNode[A]]): Fix[BinTreeNode[A]] = {
    Fix(NodeF(value, left, right))
  }

  private val fixEmptyMemo = Fix(EmptyF)

  def fixEmpty[A]: Fix[BinTreeNode[A]] = fixEmptyMemo.asInstanceOf

}
