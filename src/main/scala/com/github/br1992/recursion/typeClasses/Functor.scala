package com.github.br1992.recursion.typeClasses

import cats.Functor
import com.github.br1992.recursion.data.{BinTreeF, BinTreeNode, EmptyF, NodeF, BinTree, Node, Empty}

object Functor {
  def binTreeF[A]: Functor[[F] =>> BinTreeF[F, A]] = new Functor[[F] =>> BinTreeF[F, A]] {
    def map[F1, F2](fa: BinTreeF[F1, A])(f: F1 => F2): BinTreeF[F2, A] = fa match {
      case EmptyF => EmptyF
      case NodeF(value, left, right) => NodeF(value, f(left), f(right))
    }
  }

  def binTree: Functor[[A] =>> BinTree[A]] = new Functor[[A] =>> BinTree[A]] {
    def map[A, B](fa: BinTree[A])(f: A => B): BinTree[B] = fa.match {
      case Empty => Empty
      case Node(value, left, right) => Node(f(value), map(left)(f), map(right)(f))
    }
  }

//  def fold[R, A](R r, func: (R, A) -> A): A {
//    val partialResult: A = fold(r.recurse(), func)
//    func(r, partialResult)
//  }
//
//  def unfold[R, A](A a, func: (A) -> A): R {
//    val nextSeed: A = func(a)
//    R.create(a, unfold(nextSeed, func))
//  }
}
