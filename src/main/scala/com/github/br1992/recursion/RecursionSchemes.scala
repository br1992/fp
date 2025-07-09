import cats.Functor
import cats.derived.semiauto
import com.github.br1992.recursion.data.{BinTreeNode, EmptyF, NodeF}
import higherkindness.droste.data.Attr
import higherkindness.droste.{CVAlgebra, Gather, scheme}

//package com.github.br1992.recursion
//
//import cats.Functor
//import cats.derived.semiauto
//import com.github.br1992.recursion.RecursionSchemes.BinTreeNodeTerm.{empty, node}
//import com.github.br1992.recursion.data.BinTreeF
//import higherkindness.droste.data.{Attr, Fix}
//import higherkindness.droste.{Algebra, CVAlgebra, GAlgebra, Gather, scheme}
//
//object RecursionSchemes {
//
//  def main(args: Array[String]): Unit = {
//    val tree = nodeF(
//      1,
//      node(2,
//        node(4, empty, empty),
//        node(5, empty, empty),
//      ),
//      node(3,
//        node(6, empty, empty),
//        node(7, empty, empty),
//      )
//    )
//
//    val recursive: Recursive[Term[BinTreeNode[Int]], BinTreeNode[Int]] = Recursive.term()
//    given r: Recursive[Term[BinTreeNode[Int]], BinTreeNode[Int]] = recursive
//    given c: Comonad[Identity] = Comonad.identity()
//    given f: Functor[BinTreeNode[Int]] = semiauto.functor
//    given Functor[[F] =>> BinTreeF[F, Any]] = semiauto.functor
//    given cf: Functor[Identity] = semiauto.functor
//
//    val a = GRecursionSchemes.gcata[Term[BinTreeNode[Int]], BinTreeNode[Int], Identity, Int, Int](GRecursionSchemes.distCata) {
//      case Node(v, l, r) => (v + l.value + r.value)
//      case Empty() => 0
//    }(tree)
//    println(a)
//
//    val a = scheme.gcata(
//      CVAlgebra[BinTreeNode[Int], Int] {
//        case EmptyF => 0
//        case NodeF(v, l, r) => {
//          val left = Attr.unapply(l).value
//          val right = Attr.unapply(r).value
//
//          println(v)
//          println(left._2)
//          println(right._2)
//
//          v + left._1 + right._1
//        }
//      }.gather(Gather.histo)
//    ).apply(tree)
////    println(a)
//
////    val result = bottomUp[BinTreeNode[Int], Int] {
////      case e: Empty[?, ?] => 0
////      case Node(value, left, right) => value + left + right
////    }(tree)
////    println(result)
//
//    val topDownResult = topDown[(Int, IndexedSeq[Int]), BinTreeNode[Int]] { (index, list) =>
//      if (index >= list.length) Empty()
//      else Node(list(index), (index * 2 + 1, list), (index * 2 + 2, list))
//    }(0, IndexedSeq(4, 2, 6, 1, 3, 5, 7))
//    println(topDownResult)
//  }
//
////  val BinTreeFSum: GAlgebra[[F] =>> BinTreeF[F, ?], Int, Int] = GAlgebra {
////    def apply(fa: BinTreeNode[Int]): Int = fa match {
////      case Empty() => 0
////      case Node(value, left, right) => value + left + right
////    }
////  }
//
////  given Functor[BinTreeNode[Int]] = semiauto.functor
//
//  given f[A]: Functor[BinTreeNode[A]] with
//    def map[F1, F2](fa: BinTreeF[F1, A])(f: F1 => F2): BinTreeF[F2, A] = fa match {
//      case Empty() => Empty()
//      case Node(value, left, right) =>
//        Node(value, f(left), f(right))
//    }
//
////  given f[F, A]: Functor[BinTreeF[Term, A]] with
////    def map[B](f: A => B)(fa: BinTreeF[F, A]): BinTreeF[F, B] = fa match {
////      case Empty() => Empty()
////      case Node(value, left, right) =>
////        Node(f(value), map(f)(left), map(f)(right))
////    }
//  
////  case object BinTreeNodeTerm {
////    def empty[A]: Term[BinTreeNode[A]] = Term[BinTreeNode[A]](Empty())
////    def node[A](value: A, left: Term[BinTreeNode[A]], right: Term[BinTreeNode[A]]): Term[BinTreeNode[A]] =
////      Term[BinTreeNode[A]](Node(value, left, right))
////  }
//  case object BinTreeNodeTerm {
//    def empty[A]: Term[BinTreeNode[A]] = Term[BinTreeNode[A]](Empty)
//    def node[A](value: A, left: Term[BinTreeNode[A]], right: Term[BinTreeNode[A]]): Term[BinTreeNode[A]] =
//      Term[BinTreeNode[A]](Node(value, left, right))
//  }
//
//
////  type Algebra[F[_], A] = F[A] => A
//
//  // catamorphism - collapse the recursive structure into a single value
//  def bottomUp[F[_], A](
//      algebra: F[A] => A
//  )(term: Term[F])(using F: Functor[F]): A = {
//    val unwrapped = term.unfix
//    val mapped = F.map(unwrapped)(bottomUp(algebra))
//    algebra(mapped)
//  }
//
//  // anamorphism - build a recursive structure from a seed value
//  def topDown[A, F[_]](
//      coalgebra: A => F[A]
//  )(seed: A)(using F: Functor[F]): Term[F] = {
//    val transformed = coalgebra(seed)
//    val mapped = F.map(transformed)(topDown(coalgebra))
//    Term(mapped)
//  }
//
//  def refold[F[_], A, B](algebra: F[B] => B)(coalgebra: A => F[A])(using F: Functor[F]) = topDown(coalgebra) andThen bottomUp(algebra)
//  def refold2[F[_], A, B](algebra: F[B] => B)(coalgebra: A => F[A])(using F: Functor[F]): A => B = coalgebra andThen (F.map(_)(refold2(algebra)(coalgebra))) andThen algebra
//
//  case class Term[F[_]](unfix: F[Term[F]])
//  
//}
