// src/main/scala/Main.scala
package scala

import scala.model.*
import scala.util.*
import scala.controller.*
import scala.view.*

object Main {

  def main(args: Array[String]): Unit = {
    val field = UnoField()
    val controller = new UnoController(field)
    val tui = new TUI(controller)
    tui.startGame()

  }

}
