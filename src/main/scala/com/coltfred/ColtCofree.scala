package com.coltfred
import cats._, cats.data._, cats.implicits._, cats.free._

case class ColtCofree[F[_]: Functor, A](head: A, tail: F[ColtCofree[F, A]]) {
  def map[B](f: A => B): ColtCofree[F, B] = ColtCofree(f(head), tail.map(_.map(f)))
  def extract: A = head
}

object ColtCofree {
  def instance[F[_]: Functor] = new Comonad[ColtCofree[F, ?]] {
    def coflatMap[A, B](fa: ColtCofree[F, A])(f: ColtCofree[F, A] => B): ColtCofree[F, B] = {
      ColtCofree(f(fa), Functor[F].map(fa.tail)(coflatMap(_)(f)))
    }
    def extract[A](x: ColtCofree[F, A]): A = x.extract
    def map[A, B](fa: ColtCofree[F, A])(f: A => B): ColtCofree[F, B] = fa.map(f)
  }
}
