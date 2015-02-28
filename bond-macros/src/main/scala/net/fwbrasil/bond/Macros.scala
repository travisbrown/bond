package net.fwbrasil.bond

import net.fwbrasil.smirror._
import scala.reflect.macros.whitebox.Context
import language.experimental.macros
import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import java.lang.reflect.Field

object Macros {
  
  implicit lazy val mirror = runtimeMirror(getClass.getClassLoader)

  def lift[U, M](c: Context)(value: c.Expr[U])(
    implicit u: c.WeakTypeTag[U],
    m: c.WeakTypeTag[M]): c.Tree = {
    import c.universe._

    println(showRaw(weakTypeTag[U]))
    println(showRaw(weakTypeTag[M]))

    val ConstantType(Constant(origin: Object)) = weakTypeTag[U].tpe.baseType(weakTypeTag[M].tpe.baseClasses.head).typeArgs.head
    val ConstantType(Constant(target: Object)) = weakTypeTag[M].tpe.typeArgs.head

    val name = weakTypeTag[M].tpe.baseClasses.head.companion.fullName
    
    val me = sClassOf(Class.forName(name)).companionObjectOption.flatMap(_.methods.find(_.name == "lift")).get
    
    val valid = me.invoke(origin, target).asInstanceOf[Boolean]
    
    if (!valid)
      c.error(c.enclosingPosition, s"failed to lift $origin to $target")
    q"""
      $value.asInstanceOf[$u with $m]
    """
  }
}