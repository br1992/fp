//package com.github.br1992.recursion
//
//import cats.Functor
//import cats.derived.semiauto
//
//import higherkindness.droste.{Algebra, Coalgebra}
//import higherkindness.droste.data.list.{ListF, NilF, ConsF}
//import higherkindness.droste.scheme.hylo
//
//import scala.+:
//import scala.collection.immutable.Seq
//
//object ReversePolishNotation {
//
//  def main(args: Array[String]): Unit = {
//    val calculation = "15 7 1 1 + - / 3 * 2 1 1 + + -"
//    val result = rpn2(calculation)
//    println(result)
//  }
//
//  enum Token {
//    case Lit(value: Int)
//    case Op(op: (Int, Int) => Int)
//  }
//
//  given f: Functor[[F] =>> ListF[Token, F]] = semiauto.functor
//
//  def parseToken(token: String): Token = {
//    println(token)
//    token match {
//      case "+" => Token.Op(_ + _)
//      case "-" => Token.Op(_ - _)
//      case "*" => Token.Op(_ * _)
//      case "/" => Token.Op(_ / _)
//      case digit => Token.Lit(digit.toInt)
//    }
//  }
//
//  def rpn(calculation: String): Int = (refold2(evalRPN)(parseRPN)(calculation)(Seq.empty[Int])).head
//  def rpn2(calculation: String)(using F: Functor[[B] =>> ListF[Token, B]]): Int = hylo(
//    Algebra(evalRPN2),
//    Coalgebra {
//      parseRPN
//    }
//  )(F)(calculation)(Seq.empty[Int]).head
//
//  def parseRPN(calculation: String): ListF[Token, String] = {
//    if (calculation.isEmpty) {
//      NilF
//    } else {
//      val strippedLeading = calculation.stripLeading()
//      val (tokenString, rest) = strippedLeading.span(char => !char.isWhitespace)
//      val token = parseToken(tokenString)
//      ConsF(token, rest)
//    }
//  }
//
//  def evalRPN(calc: ListF[Token, Seq[Int] => Seq[Int]])(stack: Seq[Int]): Seq[Int] = {
//    println(stack)
//    calc match {
//      case ConsF(Token.Lit(int), continuation) => continuation(stack.prepended(int))
//      case ConsF(Token.Op(op), continuation) => {
//        println(op)
//        stack match {
//          case a :: b :: rest => {
//            val result = op(b, a)
//            continuation(rest.prepended(result))
//          }
//          case _ => throw new IllegalArgumentException("Not enough operands on the stack")
//        }
//      }
//      case NilF => stack
//      case _  => throw new IllegalArgumentException("Unexpected token in RPN expression: " + calc)
//    }
//  }
//
//  def evalRPN2(fa: ListF[Token, Seq[Int] => Seq[Int]]): Seq[Int] => Seq[Int] = {
//    fa match {
//      case NilF => identity
//      case ConsF(Token.Lit(num), continuation) => stack => continuation(stack.prepended(num))
//      case ConsF(Token.Op(op), continuation) => stack => {
//        stack match {
//          case a +: b +: rest => continuation(rest.prepended(op(b, a)))
//          case _ => throw new IllegalArgumentException("Not enough operands on the stack")
//        }
//      }
//    }
//  }
//
//  def refold2[F[_], A, B](algebra: F[B] => B)(coalgebra: A => F[A])(using F: Functor[F]): A => B = coalgebra andThen (F.map(_)(refold2(algebra)(coalgebra))) andThen algebra
//
//}
