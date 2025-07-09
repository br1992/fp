package com.github.br1992.recursion

import cats.Functor
import cats.derived.semiauto
import com.github.br1992.recursion.typeClasses.{Comonad, Identity, Recursive}
import higherkindness.droste.{Project, scheme}

object GRecursionSchemes {

  def gcata[T,F[_], W[_], A, B](using R: Recursive[T, F])(dist: F[W[W[A]]] => W[F[W[A]]])(gAlgebra: F[W[A]] => A)(t: T)(using
    W: Comonad[W],
    F: Functor[F],
    WF: Functor[W]
  ): A = {
    def gcataHelper(t: T): W[F[W[A]]] = {
      val projected = R.project(t)
      val previousStep = F.map(projected) { wrappedT =>
        val a = gcataHelper(wrappedT)
        val b = WF.map(a)(gAlgebra)
        W.duplicate(b)
      }
      dist(previousStep)
    }

    val previousResult = gcataHelper(t)
    val extractedResult = W.extract(previousResult)
    gAlgebra(extractedResult)
  }

  def distCata[F[_], A](input: F[Identity[A]])(using F: Functor[F]): Identity[F[A]] = {
    Identity(F.map(input)(i => i.value))
  }

}
