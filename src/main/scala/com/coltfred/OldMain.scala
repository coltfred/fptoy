// package com.coltfred
// import cats._, cats.data._, cats.implicits._, cats.free._

// object Main {
//   def main(args: Array[String]): Unit = {

//   }

//   type Rng[A] = Free[RngOp, A]
//   sealed trait RngOp[A]
//   object RngOp {
//     case class SetSeed(i: Int) extends RngOp[Unit]
//     case object NextInt extends RngOp[Int]

//   }
//   import RngOp._

//   def nextInt: Rng[Int] = Free.liftF(NextInt)
//   def setSeed(i: Int): Rng[Unit] = Free.liftF(SetSeed(i))

//   type Console[A] = Free[ConsoleOp, A]
//   sealed trait ConsoleOp[A]
//   object ConsoleOp {
//     case object ReadLine extends ConsoleOp[String]
//     case class WriteLine(s: String) extends ConsoleOp[Unit]
//   }
// }

// // import ConsoleOp._
// //   def readLine: Console[String] = Free.liftF(ReadLine)
// //   def writeLine(s: String): Console[Unit] = Free.liftF(WriteLine(s))

// //   type InjectConsoleOp[F[_]] = Inject[ConsoleOp, F]
// //   def readAndWriteTwice: Console[Unit] = {
// //     for {
// //       v <- readLine
// //       _ <- writeLine(v)
// //       _ <- writeLine(v)
// //     } yield ()
// //   }

// //   def conditionalPrint(i: Int): Console[Unit] = if (i % 2 == 0) writeLine(i.toString) else writeLine("foo")

// //   type App[A] = Coproduct[Console, Rng, A]

// //   class Consoles[F[_]](implicit I: Inject[Console, F]) {
// //     def liftInject[A](c: Console[A]): Free[F, A] = Free.inject[Console, F](c)
// //   }

// //   def foo(implicit I: Consoles[App]): Free[App, Unit] = {
// //     import I._
// //     for {
// //       v <- liftInject(writeLine("blah"))
// //     } yield ()
// //   }

// //   object ConsoleInterpreter extends (Console ~> Id) {
// //     def apply[A](console: Console[A]) = console.foldMap(ConsoleOpInterpreter)
// //   }

// //   object ConsoleOpInterpreter extends (ConsoleOp ~> Id) {
// //     def apply[A](c: ConsoleOp[A]) = c match {
// //       case ReadLine => scala.io.StdIn.readLine()
// //       case WriteLine(s) => println(s)
// //     }
// //   }
