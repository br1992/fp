package com.github.br1992.recursion.data

import cats.Functor

sealed trait BinTreeF[+F, +A]
case object EmptyF extends BinTreeF[Nothing, Nothing]
case class NodeF[F, A](value: A, left: F, right: F) extends BinTreeF[F, A]

type BinTreeNode[A] = [F] =>> BinTreeF[F, A]
