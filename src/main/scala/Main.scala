// src/main/scala/Main.scala
//package scala
import controller.*
import model.*
import util.*
import view.*

object Main {

  def main(args: Array[String]): Unit = {
    val field = UnoField()
    val controller = new UnoController(field)
    val tui = new TUI(controller)
    tui.startGame()

  }

}
