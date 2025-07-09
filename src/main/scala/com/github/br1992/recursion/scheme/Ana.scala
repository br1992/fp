package com.github.br1992.recursion.scheme

import cats.Functor
import com.github.br1992.recursion.data.Fix

object Ana {

  def topDown[F[_], A](coalgebra: A => F[A])(seed: A)(using F: Functor[F]): Fix[F] = {
    val transformed = coalgebra(seed)
    val mapped = F.map(transformed)(topDown(coalgebra))
    Fix(mapped)
  }

  def topDown2[F[_], A](coalgebra: A => F[A])(using F: Functor[F]): A => Fix[F] =
    Fix.apply[F].compose((F.map(_: F[A])(topDown(coalgebra))).compose(coalgebra))
    
}
