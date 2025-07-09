package com.github.br1992.recursion.data

sealed trait BinTree[+A]
case object Empty extends BinTree[Nothing]
case class Node[+A](value: A, left: BinTree[A], right: BinTree[A]) extends BinTree[A]