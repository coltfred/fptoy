package com.coltfred

import cats._, cats.data._, cats.implicits._, cats.free._, cats.arrow._

case class ColtFree[F[_]: Functor, A](resume: A Xor F[ColtFree[F, A]]) {
  def map[B](f: A => B): ColtFree[F, B] = resume match {
    case Xor.Left(a) => ColtFree(Xor.Left(f(a)))
    case Xor.Right(nested) => ColtFree(Xor.Right(nested.map(_.map(f))))
  }

  def flatMap[B](f: A => ColtFree[F, B]): ColtFree[F, B] = resume match {
    case Xor.Left(a) => f(a)
    case Xor.Right(nestedF) => ColtFree(Xor.Right(nestedF.map(_.flatMap(f))))
  }
}

object ColtFree {
  implicit def monad[F[_]: Functor]: Monad[ColtFree[F, ?]] = new Monad[ColtFree[F, ?]] {
    def pure[A](x: A): ColtFree[F, A] = ColtFree[F, A](Xor.Left(x))
    def tailRecM[A, B](a: A)(f: A => ColtFree[F, Either[A, B]]): ColtFree[F, B] = defaultTailRecM(a)(f)
    def flatMap[A, B](fa: ColtFree[F, A])(f: A => ColtFree[F, B]): ColtFree[F, B] = fa.flatMap(f)
  }

  def liftF[F[_]: Functor, A](fa: F[A]): ColtFree[F, A] = ColtFree(Xor.Right(fa.map(a => ColtFree(Xor.Left(a)))))

  def foldMap[F[_], A, M[_]: Monad](nt: FunctionK[F, M])(free: ColtFree[F, A]): M[A] = free.resume match {
    case Xor.Left(a) => Monad[M].pure(a)
    case Xor.Right(f) => nt(f).flatMap(foldMap(nt)(_))
  }

  final def resume[F[_], A](free: ColtFree[F, A])(implicit F: Functor[F]): Xor[F[ColtFree[F, A]], A] = free.resume match {
    case Xor.Left(a) => Xor.Right(a)
    case Xor.Right(f) => Xor.Left(f)
  }

  final def go[F[_], A](f: F[ColtFree[F, A]] => ColtFree[F, A])(free: ColtFree[F, A])(implicit F: Functor[F]): A = free.resume match {
    case Xor.Left(a) => a
    case Xor.Right(suspended) => go(f)(f(suspended))
  }

  final def run[F[_]: Comonad, A](free: ColtFree[F, A]): A = {
    val extraction: F[ColtFree[F, A]] => ColtFree[F, A] = Comonad[F].extract _
    go(extraction)(free)
  }

}
