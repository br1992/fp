package com.github.br1992.recursion.typeClasses

import com.github.br1992.recursion.data.Fix

trait Recursive[T, F[_]] {
  def project(data: T): F[T]
}

object Recursive {
  def fix[F[_]](): Recursive[Fix[F], F] = (data: Fix[F]) => data.unfix
}