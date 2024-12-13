// src/main/scala/Main.scala
import controller.*
import model.*
import util.*
import view.*

object Main {

  def main(args: Array[String]): Unit = {
    val field = UnoField()
    val controller = new UnoController(field)
    
    // Initialize TUI and GUI
    val tui = new TUI(controller)
    UnoGUI.launchApp(controller)
    
    // Start the TUI
    tui.startGame()
  }

}