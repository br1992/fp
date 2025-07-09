package com.github.br1992.ziodemo

import zio.{ZIO, ZLayer}

class Parallelism {

  def inParallel(): ZIO[A & B, Throwable, Unit] = {
    for {
      a <- ZIO.service[A]
      b <- ZIO.service[B]
      fib1 <- ZIO.succeed("Fiber 1").fork
      fib2 <- ZIO.succeed("Fiber 2").fork
      fib3 <- ZIO.succeed("Fiber 3").fork
      // result <- (fib1 <*> fib2 <*> fib3).join
    } yield ()
  }


  
  trait A
  trait B

}
