package com.github.br1992.recursion.general

import cats.Functor
import cats.derived.semiauto
import com.github.br1992.recursion.data.*
import com.github.br1992.recursion.scheme.{Cata, GCata}
import com.github.br1992.recursion.typeClasses.{Comonad, Identity, Recursive}
import higherkindness.droste.data.{Attr, Fix}
import higherkindness.droste.{Algebra, CVAlgebra, Gather, scheme}

object TreeGCataDroste {

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

    given c: Comonad[Identity] = Comonad.identity()
    given f: Functor[BinTreeNode[Int]] = semiauto.functor
    given cf: Functor[Identity] = semiauto.functor

    val a = scheme.gcata(
      CVAlgebra[[F] =>> BinTreeF[F, Int], Int] {
        case EmptyF => 0
        case NodeF(v, l, r) => {
          val left = Attr.unapply(l).value
          val right = Attr.unapply(r).value

          println(NodeF(v, l, r))

          v + left._1 + right._1
        }
      }.gather(Gather.histo)
    ).apply(tree)
    println(a)
  }

  def fixNode[A](value: A, left: Fix[BinTreeNode[A]], right: Fix[BinTreeNode[A]]): Fix[BinTreeNode[A]] = {
    Fix(NodeF(value, left, right))
  }

  val fixEmptyMemo = Fix(EmptyF)

  def fixEmpty[A]: Fix[BinTreeNode[A]] = fixEmptyMemo.asInstanceOf


}
