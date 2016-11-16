package com.coltfred
import cats._, cats.data._, cats.implicits._, cats.free._

object Main {
  def main(args: Array[String]): Unit = {

  }

  type Rng[A] = Free[RngOp, A]
  sealed trait RngOp[A]
  object RngOp {
    case class SetSeed(i: Int) extends RngOp[Unit]
    case object NextInt extends RngOp[Int]

  }
  import RngOp._

  def nextInt: Rng[Int] = Free.liftF(NextInt)
  def setSeed(i: Int): Rng[Unit] = Free.liftF(SetSeed(i))

  type Console[A] = ColtFree[ConsoleOp, A]
  sealed trait ConsoleOp[A]
  object ConsoleOp {
    implicit val functor: Functor[ConsoleOp] = new Functor[ConsoleOp] {
      def map[A, B](fa: ConsoleOp[A])(f: A => B): ConsoleOp[B] = fa match {
        case ReadLine(next) => ReadLine(next andThen f)
        case WriteLine(s, next) => WriteLine(s, next andThen f)
      }
    }
    case class ReadLine[A](next: String => A) extends ConsoleOp[A]
    case class WriteLine[A](s: String, next: Unit => A) extends ConsoleOp[A]
  }

  import ConsoleOp._
  def readLine: Console[String] = ColtFree.liftF(ReadLine(identity))
  def writeLine(s: String): Console[Unit] = ColtFree.liftF(WriteLine(s, identity))

  def readAndWriteTwice: Console[Unit] = {
    for {
      v <- readLine
      _ <- writeLine(v)
      _ <- writeLine(v)
    } yield ()
  }

  def interpret: cats.arrow.FunctionK[ConsoleOp, Id] = new cats.arrow.FunctionK[ConsoleOp, Id] {
    def apply[A](op: ConsoleOp[A]): A = op match {
      case ReadLine(next) => next(scala.io.StdIn.readLine)
      case WriteLine(s, next) => next(println(s))
    }
  }
}

