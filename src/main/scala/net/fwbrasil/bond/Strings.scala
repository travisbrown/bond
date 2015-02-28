package net.fwbrasil.bond

import shapeless._
import shapeless.syntax.singleton._
import shapeless._
import shapeless.syntax.singleton._
import net.fwbrasil.bond._

// trait EndsWith
//  trait MatchesRegex
//  trait Email
//  val Email =
//    new Validation0[String, Email] {
//      def apply(v: String) =
//        v.matches(emailPattern)
//    }

trait StartsWith[-T]
object StartsWith extends Validation1[String, StartsWith] {
  def isValid(v: String, p: String) =
    v.startsWith(p)
}