package com.github.br1992.recursion.adhoc

import com.github.br1992.recursion.data.{BinTree, Empty, Node}

object FoldTree {

  def main(args: Array[String]): Unit = {
    val tree = Node(
      1,
      Node(2,
        Node(4, Empty, Empty),
        Node(5, Empty, Empty),
      ),
      Node(3,
        Node(6, Empty, Empty),
        Node(7, Empty, Empty),
      )
    )

    println(foldTree(tree)(0))
  }

  def foldTree(tree: BinTree[Int]): Int => String = {
    tree match {
      case Empty => (indents) => "Empty".indent(indents * 2)
      case Node(value, left, right) => (indents) => {
        val nextIndent = indents + 1
        val indentedValue: String = s"$value".indent(indents * 2)
        s"${foldTree(left)(indents + 1)}$indentedValue${foldTree(right)(indents + 1)}"
      }
    }
  }

}
