package com.github.br1992.recursion.scheme

import cats.Functor
import com.github.br1992.recursion.scheme.Ana.topDown
import com.github.br1992.recursion.scheme.Cata.bottomUp

object Hylo {

//  def refold[F[_], A, B](algebra: F[B] => B)(coalgebra: A => F[A])(using F: Functor[F]): A => B = topDown(coalgebra) andThen bottomUp(algebra)
  def refold[F[_], A, B](algebra: F[B] => B)(coalgebra: A => F[A])(using F: Functor[F]): A => B = coalgebra andThen (F.map(_)(refold(algebra)(coalgebra))) andThen algebra

}
