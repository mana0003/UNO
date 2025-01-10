import controller.*

import com.google.inject.{AbstractModule, Guice, Inject, Injector}
import controller.controllerComponent.IUnoController
import controller.controllerComponent.ControllerIm.UnoController
import model.gameComponent.gameIm.{Player, PlayerHand, UnoField}
import model.cardComponent.cardIm.{Card, randomCard}
import view.*
import UNO.MainModule

object Main {
  def main(args: Array[String]): Unit = {
    val injector: Injector = Guice.createInjector(new MainModule)
    val controller = injector.getInstance(classOf[IUnoController])

    val players = List(Player(0, PlayerHand(List())))
    val topCard = randomCard
    val currentPlayer = 0
    val field = new UnoField(players, topCard, currentPlayer)

    val contr: IUnoController = new UnoController(field)

    // Initialize TUI and GUI
    val tui = new TUI(contr)
    new Thread(() => tui.asInstanceOf[TUI].startGame()).start()
    UnoGUI.launchApp(contr)
  }
}