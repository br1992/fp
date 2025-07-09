package com.github.br1992.recursion.data

sealed trait ConsList[+A]
case object Nil extends ConsList[Nothing]
case class Cons[+A](head: A, tail: ConsList[A]) extends ConsList[A]
