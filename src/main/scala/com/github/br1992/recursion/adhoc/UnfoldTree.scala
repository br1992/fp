package com.github.br1992.recursion.adhoc

import com.github.br1992.recursion.data.{BinTree, Empty, Node}

object UnfoldTree {

  def main(args: Array[String]): Unit = {
    val seed = 1
    val levels = 3

    println(unfoldTree(seed, levels))
  }

  def unfoldTree(seed: Int, levels: Int): BinTree[Int] = {
    if (levels <= 0) {
      Empty
    } else {
      Node(seed,
        unfoldTree(2 * seed, levels - 1),
        unfoldTree(2 * seed + 1, levels - 1)
      )
    }
  }
}
