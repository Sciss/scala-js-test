package de.sciss.scalajstest

import scala.scalajs.js
import scala.scalajs.js.JSApp
import org.scalajs.jquery.jQuery

import scala.scalajs.js.annotation.JSExport

object Main extends JSApp {
  def main(): Unit = ()

  @JSExport
  def addClickedMessage(): Unit =
    jQuery("body").append("<p>Hello World</p>")

  def makeSound(): Unit = {
    val ctx = js.Dynamic.newInstance(js.Dynamic.global.AudioContext)()
    println(s"AudioContext state = ${ctx.state}")
    val o = ctx.createOscillator()
    o.frequency.value = 666
    // ctx.resume()
    o.connect(ctx.destination)
    o.start()
    println("Done")
  }
}