package com.github.br1992.ziodemo

import zio.ZIO
import zio.stm.{STM, TRef}

class Concurrency {

  def bothTransactions(var1: TRef[Int], var2: TRef[Int], var3: TRef[Int], var4: TRef[Int]): ZIO[Any, Throwable, Unit] = {
    STM.atomically {
      for {
        _ <- transaction1(var1, var2)
        _ <- transaction2(var3, var4)
      } yield ()
    }
  }

  def transaction1(var1: TRef[Int], var2: TRef[Int]): STM[Throwable, (Int, Int)] = {
    for {
      curVar1 <- var1.get
      curVar2 <- var2.get

      newVar1 = curVar1 + 1
      newVar2 = curVar2 - 1

      _ <- var1.set(newVar1)
      _ <- var2.set(newVar2)
    } yield (newVar1, newVar2)
  }

  def transaction2(var3: TRef[Int], var4: TRef[Int]): STM[Throwable, (Int, Int)] = {
    for {
      curVar3 <- var3.get
      curVar4 <- var4.get

      newVar3 = curVar3 * 2
      newVar4 = curVar4 / 2

      _ <- var3.set(newVar3)
      _ <- var4.set(newVar4)
    } yield (newVar3, newVar4)
  }

}
