package de.sciss.scalajstest

import scala.scalajs.js
import scala.scalajs.js.JSApp

object Main extends JSApp {
  def main(): Unit = {
    println("Hello world!")
    val ctx = js.Dynamic.newInstance(js.Dynamic.global.AudioContext)()
    val o = ctx.createOscillator()
    o.frequency.value = 666
    // o.start(0)
    // o.connect(ctx.destination)
  }
}