package com.github.br1992.recursion.general

import cats.Functor
import cats.derived.semiauto
import higherkindness.droste.data.Fix
import higherkindness.droste.data.list.{ConsF, ListF, NilF}
import higherkindness.droste.{Algebra, Coalgebra, GCoalgebra, Gather, Scatter, scheme}

object CapturedRainfall {

  def main(args: Array[String]): Unit = {
    val heights = (List(0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1), 0)

    given functor: Functor[[F] =>> ConsF[(Int, Int), F]] = semiauto.functor[[F] =>> ConsF[(Int, Int), F]]

//    scheme.gana(GCoalgebra[[F] =>> ListF[(Int, Int), F], (List[Int], Int), (Int, Int)]{
//        case (head :: tail, maxLeft) => ConsF((head, maxLeft), (tail, math.max(maxLeft, head)))
//        case (Nil, _) => NilF
//    })

//    scheme.hylo(
//      Algebra {
//        case NilF => (0, 0)
//        case ConsF((head, maxLeft), (sum, cont)) =>
//          val water = math.max(0, math.min(maxLeft, rightMax) - head)
//          water + tail._1
//      },
//      Coalgebra {
//        case (head :: tail, maxLeft) => ConsF((head, maxLeft), (tail, math.max(maxLeft, head)))
//        case (Nil, _) => NilF
//      }
//    )(functor)(heights)

  }

}
