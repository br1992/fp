package com.github.br1992

import zio.ZIO

class Parallelism {

  def inParallel(): ZIO[Any, Throwable, (String, String, String)] = {
    for {
      fib1 <- ZIO.succeed("Fiber 1").fork
      fib2 <- ZIO.succeed("Fiber 2").fork
      fib3 <- ZIO.succeed("Fiber 3").fork
      values <- (fib1 <*> fib2 <*> fib3).join
    } yield (values)
  }

}
