package com.github.br1992.recursion.data

case class Fix[F[_]](unfix: F[Fix[F]])
