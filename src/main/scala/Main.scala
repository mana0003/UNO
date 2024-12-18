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
    new Thread(() => tui.startGame()).start()
    UnoGUI.launchApp(controller)
  }
}