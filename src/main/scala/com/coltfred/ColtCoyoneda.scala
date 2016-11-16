package com.coltfred

import cats._, cats.data._, cats.implicits._, cats.free._, cats.arrow._

case class ColtCoyoneda[F[_], I, A](fi: F[I], ia: I => A)

