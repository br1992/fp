package com.github.br1992.recursion.scheme

import cats.Functor
import com.github.br1992.recursion.typeClasses.{Cofree, Comonad, Identity, Recursive}

object GCata {

  /**
   * Implements a generalized catamorphism for arbitrary recursive data structures.
   * @param dist is the function that distributes the results of the previous steps of the recursion
   * @param gAlgebra is the function that determines the response of each step of the recursion
   * @param t is the fixed point of the recursive data structure (i.e. Fix[F])
   * @param R is the instance of Recursive from type T to F[T]
   * @param W is the instance of Comonad for the carrier type W
   * @param F is the Functor instance for the recursive data structure F
   * @param WF is the Functor instance for the carrier type W
   * @tparam T is the fixed point definition of the recursive data structure (i.e. Fix[F])
   * @tparam F is the recursive data structure in F-form (ex: BinTreeF[Int, _])
   * @tparam W is the type that carries the results of previous steps of the recursion
   * @tparam A is the result type to be returned by the algebra and gcata
   * @return
   */
  def gcata[T, F[_], W[_], A](dist: F[W[W[A]]] => W[F[W[A]]])(gAlgebra: F[W[A]] => A)(t: T)(using
    R: Recursive[T, F],
    W: Comonad[W],
    F: Functor[F],
    WF: Functor[W]
  ): A = {
    def gcataHelper(t: T): W[F[W[A]]] = {
      val projected = R.project(t)
      val partialResult = F.map(projected) { wrappedT =>
        val wfwa = gcataHelper(wrappedT)
        val wa = WF.map(wfwa)(gAlgebra)
        val wwa = W.duplicate(wa)
        wwa
      }
      dist(partialResult)
    }

    val wrappedResult = gcataHelper(t)
    val extractedResult = W.extract(wrappedResult)
    gAlgebra(extractedResult)
  }

  def distCata[F[_], A](input: F[Identity[Identity[A]]])(using F: Functor[F]): Identity[F[Identity[A]]] = {
    Identity(F.map(input)(i => i.value))
  }

  def distHisto[F[_], A](input: F[Cofree[F, A]])(using F: Functor[F]): Cofree[F, F[A]] = {
    val head = F.map(input) { cofree => cofree.head }
    val tail = F.map(input) { cofree => distHisto(cofree.tail) }
    Cofree[F, F[A]](head, tail)
  }

}
