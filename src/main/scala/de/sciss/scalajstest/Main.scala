package de.sciss.scalajstest

import org.scalajs.dom.raw.HTMLAudioElement
import org.scalajs.jquery.jQuery

import scala.scalajs.js
import scala.scalajs.js.JSApp

object Main extends JSApp {
  def main(): Unit =
    jQuery(setupUI _)

  def setupUI(): Unit = {
    jQuery("#play-button").click(play _)
    jQuery("#stop-button").click(stop _)
    // jQuery("body").append("<p>Hello World</p>")
  }

  def addClickedMessage(): Unit =
    jQuery("body").append("<p>Hello World</p>")

  def player: HTMLAudioElement = jQuery("#player")(0).asInstanceOf[HTMLAudioElement]

  def play(): Unit = player.play()
  def stop(): Unit = player.pause()

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