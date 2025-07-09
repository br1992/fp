package com.github.br1992.recursion.typeClasses

import cats.Functor
import com.github.br1992.recursion.data.{ConsF, ConsListF, NilF}

trait Corecursive[T, TF[_]] {
  def embed(data: TF[T]): T
}

object Corecursive {
  def consList[A](): Corecursive[List[A], [F] =>> ConsListF[F, A]] = {
    case ConsF(head, tail) => head :: tail
    case NilF => List.empty
  }
}

