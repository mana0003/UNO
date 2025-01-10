import controller.*
import controller.controllerComponent.ControllerIm.UnoController
import model.*
import model.gameComponent.gameIm.UnoField
import view.*

object Main {
  def main(args: Array[String]): Unit = {
    val field = new UnoField()
    val contr: UnoController = new UnoController(field) {
      // Implement the abstract methods here
    }

    // Initialize TUI and GUI
    val tui = new TUI(contr)
    new Thread(() => tui.asInstanceOf[TUI].startGame()).start()
    UnoGUI.launchApp(contr)
  }
}