package com.github.br1992.recursion.scheme

import cats.Functor
import com.github.br1992.recursion.data.Fix

object Cata {

  def bottomUp[F[_], A](algebra: F[A] => A)(term: Fix[F])(using F: Functor[F]): A = {
    val unwrapped = term.unfix
    val mapped = F.map(unwrapped)(bottomUp(algebra))
    algebra(mapped)
  }

  def bottomUpComp[F[_], A](algebra: F[A] => A)(using F: Functor[F]): Fix[F] => A =
    { (fix: Fix[F]) => fix.unfix }.andThen(F.map(_)(bottomUp(algebra))).andThen(algebra)
}
