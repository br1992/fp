package com.github.br1992.recursion.typeClasses

import cats.Functor

trait Comonad[W[_]] {
  def extract[A](wa: W[A]): A
  def duplicate[A](wa: W[A]): W[W[A]]
}

object Comonad {
  def identity(): Comonad[Identity] = new Comonad[Identity] {
    def extract[A](wa: Identity[A]): A = wa.value
    def duplicate[A](wa: Identity[A]): Identity[Identity[A]] = Identity(wa)
  }

  def cofree[F[_]](using F: Functor[F]): Comonad[[AA] =>> Cofree[F, AA]] = new Comonad[[AA] =>> Cofree[F, AA]] {
    def extract[A](wa: Cofree[F, A]): A = wa.head
    def duplicate[A](wa: Cofree[F, A]): Cofree[F, Cofree[F, A]] = {
      Cofree(wa, F.map(wa.tail)(duplicate))
    }
  }
}

case class Identity[A](value: A)
case class Cofree[F[_], A](head: A, tail: F[Cofree[F, A]])