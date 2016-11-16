package com.coltfred

object Fix {
  case class CofreeF[F[_], A, B](head: A, tail: F[B])
  type ColtCofreeTwo[F[_], A] = Fix[CofreeF[F, A, ?]]

}
case class Fix[F[_]](unfix: F[Fix[F]])

