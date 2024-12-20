package main

import controller._
import model._
import view._

object Main {
  def main(args: Array[String]): Unit = {
    val field = new UnoField()
    val contr: UnoController = new UnoController(field) {
      // Implement the abstract methods here
    }

    // Initialize TUI and GUI
    val tui: IView = new TUI(contr)
    new Thread(() => tui.asInstanceOf[TUI].startGame()).start()
    UnoGUI.launchApp(contr)
  }
}