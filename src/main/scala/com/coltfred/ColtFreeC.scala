// package com.coltfred

// import cats._, cats.data._, cats.implicits._, cats.free._, cats.arrow._

// // case class ColtCofree[F[_]:Functor, A](head: A, tail: F[ColtCofree[F,A]])
// case class ColtFreeC[F[_], A](resume: A Xor F[ColtFreeC[Lambda[A => ColtCoyoneda[F, A, A]], A]]) {
//   def map[B](f: A => B): ColtFreeC[F, B] = resume match {
//     case Xor.Left(a) => ColtFreeC(Xor.Left(f(a)))
//     case Xor.Right(nested) => ColtFreeC(Xor.Right(nested.map(_.map(f))))
//   }

//   def flatMap[B](f: A => ColtFreeC[F, B]): ColtFreeC[F, B] = resume match {
//     case Xor.Left(a) => f(a)
//     case Xor.Right(nestedF) => ColtFreeC(Xor.Right(nestedF.map(_.flatMap(f))))
//   }
// }

// object ColtFreeC {
//   implicit def monad[F[_]: Functor]: Monad[ColtFreeC[F, ?]] = new Monad[ColtFreeC[F, ?]] {
//     def pure[A](x: A): ColtFreeC[F, A] = ColtFreeC[F, A](Xor.Left(x))
//     def tailRecM[A, B](a: A)(f: A => ColtFreeC[F, Either[A, B]]): ColtFreeC[F, B] = defaultTailRecM(a)(f)
//     def flatMap[A, B](fa: ColtFreeC[F, A])(f: A => ColtFreeC[F, B]): ColtFreeC[F, B] = fa.flatMap(f)
//   }

//   def liftF[F[_]: Functor, A](fa: F[A]): ColtFreeC[F, A] = ColtFreeC(Xor.Right(fa.map(a => ColtFreeC(Xor.Left(a)))))

//   def foldMap[F[_], A, M[_]: Monad](nt: FunctionK[F, M])(free: ColtFreeC[F, A]): M[A] = free.resume match {
//     case Xor.Left(a) => Monad[M].pure(a)
//     case Xor.Right(f) => nt(f).flatMap(foldMap(nt)(_))
//   }
// }
