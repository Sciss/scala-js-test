package de.sciss.scalajstest

import org.scalajs.dom.{AudioBuffer, Event, XMLHttpRequest, GainNode, AudioContext}
import org.scalajs.dom.raw.{OscillatorNode, HTMLAudioElement}
import org.scalajs.jquery.{jQuery => $}

import scala.scalajs.js
import scala.scalajs.js.typedarray.ArrayBuffer

object Main extends js.JSApp {
  def main(): Unit =
    $(setupUI _)

  def setupUI(): Unit = {
    $("#play-button").click(play _)
    $("#stop-button").click(stop _)
    $("#toggle-osc" ).click(toggleOscillator _)
    $("#fade-out"   ).click(fadeOut _)
    $("#fade-in"    ).click(fadeIn  _)
    $("#buffer-test").click(bufferTest _)
  }

  def addClickedMessage(): Unit =
    $("body").append("<p>Hello World</p>")

  def player: HTMLAudioElement = $("#player")(0).asInstanceOf[HTMLAudioElement]

  def play(): Unit = player.play()
  def stop(): Unit = player.pause()

  private var osc: OscillatorNode = _

  private lazy val audioContext = new AudioContext
  private lazy val gainNode: GainNode = {
    val n = audioContext.createGain()
    // n.gain
    n.connect(audioContext.destination)
    n
  }

  private def mkOsc(): OscillatorNode = {
    val o = audioContext.createOscillator()
    o.frequency.value = 666
    // ctx.resume()
    o.connect(gainNode)
    o.start()
    o
  }

  def fadeIn (): Unit = fade(in = 0, out = 1)
  def fadeOut(): Unit = fade(in = 1, out = 0)

  private var sched0v = 1.0
  private var sched1v = 1.0
  private var sched0t = 0.0
  private var sched1t = 0.0

  def fade(in: Double, out: Double): Unit = {
    val g         = gainNode
    val currTime  = g.context.currentTime
    // println("VALUE = " + g.gain.value)  // Firefox does not return correct dynamic value!

    g.gain.cancelScheduledValues(currTime)

    val value = if (sched1t == sched0t) sched1v else
      (math.min(currTime, sched1t) - sched0t) / (sched1t - sched0t) * (sched1v - sched0v) + sched0v

    val timeOut = currTime + 2.0

    sched0v = value
    sched0t = currTime
    sched1v = out
    sched1t = timeOut

    g.gain.linearRampToValueAtTime(value, currTime)
    g.gain.linearRampToValueAtTime(out  , timeOut )
  }

  def toggleOscillator(): Unit = {
    if (osc == null) {
      osc = mkOsc()
    } else {
      osc.stop()
      osc.disconnect(gainNode)
      osc = null
    }
  }

  //////////////////

  def bufferTest(): Unit = {
    val request = new XMLHttpRequest
    val urlToMp3File = "/staircase.mp3" // "http://sciss.de/noises2/staircase.mp3"
    request.open("GET", url = urlToMp3File, async = true)
    request.responseType = "arraybuffer"

    def onLoad(e: Event): Unit = {
      // val source = audioContext.createBufferSource()
      // source.buffer = audioContext.createBuffer(request.response, false);
      println(s"Response = ${request.response}")
      val audioData = request.response.asInstanceOf[ArrayBuffer]  // ja?!

      def gotBuffer(buffer: AudioBuffer): Unit = {
        println("We've got an AudioBuffer, yay!")
        val n = audioContext.createBufferSource()
        n.buffer = buffer
        n.connect(audioContext.destination)
        n.start()
      }

      /* val promiseBuffer = */ audioContext.decodeAudioData(audioData, gotBuffer _)

      // promiseBuffer.andThen(gotBuffer _)
    }

    request.onload = onLoad _
    request.send()
  }
}