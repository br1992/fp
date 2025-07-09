package com.github.br1992.recursion.data

import cats.Functor
import cats.derived.semiauto

import scala.Nil

sealed trait ConsListF[+F, +A]
case class ConsF[F, +A](head: A, tail: F) extends ConsListF[F, A]
case object NilF extends ConsListF[Nothing, Nothing]

object ConsListF {
  def f[A]: Functor[[F] =>> ConsListF[A, F]] = semiauto.functor

  def fromList[A](list: List[A]): ConsListF[List[A], A] = {
    list match {
      case Nil => NilF
      case head :: tail => ConsF(head, tail)
    }
  }
}
