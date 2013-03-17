package com.coltfred
package fptoy
import scala.language.higherKinds

// 1. Start here. Observe this trait
trait Monad[M[_]] {
  def flatMap[A, B](a: M[A], f: A => M[B]): M[B]
  def unital[A](a: A): M[A]
}

// A simple data type, which turns out to satisfy the above trait
case class Inter[A](f: Int => A)

// So does this.
case class Identity[A](a: A)

// Monad implementations
object Monad {
  // 2. Replace ??? with an implementation
  def ListMonad: Monad[List] = new Monad[List] {
    def flatMap[A, B](a: List[A], f: A => List[B]): List[B] = a.flatMap(f)
    def unital[A](a: A): List[A] = List(a)
  }

  // 3. Replace ??? with an implementation
  def OptionMonad: Monad[Option] = new Monad[Option] {
    def flatMap[A, B](a: Option[A], f: A => Option[B]): Option[B] = a.flatMap(f)
    def unital[A](a: A): Option[A] = Option(a)
  }

  // 4. Replace ??? with an implementation
  def InterMonad: Monad[Inter] = new Monad[Inter] {
    def flatMap[A, B](a: Inter[A], f: A => Inter[B]): Inter[B] = Inter({ i: Int => f(a.f(i)).f(i) })
    def unital[A](a: A): Inter[A] = Inter({ i: Int => a })
  }

  // 5. Replace ??? with an implementation
  def IdentityMonad: Monad[Identity] = new Monad[Identity] {
    def flatMap[A, B](a: Identity[A], f: A => Identity[B]): Identity[B] = f(a.a)
    def unital[A](a: A): Identity[A] = Identity(a)
  }
}
object MonadicFunctions {
  // 6. Replace ??? with an implementation
  def sequence[M[_], A](as: List[M[A]], m: Monad[M]): M[List[A]] = {
    as.foldRight(m.unital(List[A]())) { (ma, acc) =>
      m.flatMap(ma, { a: A => m.flatMap(acc, { rest: List[A] => m.unital(a :: rest) }) })
    }
  }

  // 7. Replace ??? with an implementation
  def fmap[M[_], A, B](a: M[A], f: A => B, m: Monad[M]): M[B] = {
    m.flatMap(a, { aa: A => m.unital(f(aa)) })
  }

  // 8. Replace ??? with an implementation
  def flatten[M[_], A](a: M[M[A]], m: Monad[M]): M[A] = {
    m.flatMap(a, { ma: M[A] => ma })
  }

  // 9. Replace ??? with an implementation
  def apply[M[_], A, B](f: M[A => B], a: M[A], m: Monad[M]): M[B] = {
    m.flatMap(f, { ff: (A => B) => m.flatMap(a, { aa: A => m.unital(ff(aa)) }) })

  }

  // 10. Replace ??? with an implementation
  def filterM[M[_], A](f: A => M[Boolean], as: List[A], m: Monad[M]): M[List[A]] = {
    as.foldRight(m.unital(List[A]())) { (a, acc) =>
      m.flatMap(f(a), { b: Boolean =>
        if (b) {
          m.flatMap(acc, { tail: List[A] =>
            m.unital(a :: tail)
          })
        } else
          acc
      })

    }
    // as.map(a => m.flatMap(f(a), {b:Boolean => m.unital(a)}))
    // as.filter(a => m.flatMap(f(a), { b: Boolean => b }))
  }

  // 11. Replace ??? with an implementation
  def replicateM[M[_], A](n: Int, a: M[A], m: Monad[M]): M[List[A]] = {
    sequence(List.fill(n)(a), m)
  }

  // 12. Replace ??? with an implementation
  def lift2[M[_], A, B, C](f: (A, B) => C, a: M[A], b: M[B], m: Monad[M]): M[C] = {
    m.flatMap(a, { aa: A => m.flatMap(b, { bb: B => m.unital(f(aa, bb)) }) })
  }

  // lift3, lift4, etc. Interesting question: Can we have liftN?
}