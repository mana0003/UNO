package UNO
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import controller.controllerComponent.IUnoController
import controller.controllerComponent.ControllerIm.UnoController
import model.gameComponent.{IPlayer, IPlayerHand, IUnoField}
import model.gameComponent.gameIm.{Player, PlayerHand, UnoField}
import model.cardComponent.cardIm.{Card, cardColors, cardValues}
import model.cardComponent.ICard

class MainModule extends AbstractModule with ScalaModule {

  private val defaultPlayerCount: Int = 2

  override def configure(): Unit = {
    bind[IUnoField].to[UnoField]
    bind[IUnoController].to[UnoController]
    bind[ICard].to[Card]
    bind[IPlayer].to[Player]
    bind[IPlayerHand].to[PlayerHand]

    // Bindings for cardColors and cardValues
    bind[cardColors].toInstance(cardColors.RED) // Example binding, adjust as needed
    bind[cardValues].toInstance(cardValues.ZERO) // Example binding, adjust as needed

    // Binding for Option[cardColors]
    bind[Option[cardColors]].toInstance(Some(cardColors.RED)) // Example binding, adjust as needed

    // Bindings for List[ICard] and List[IPlayer]
    bind[List[ICard]].toInstance(List.empty[ICard]) // Example binding, adjust as needed
    bind[List[IPlayer]].toInstance(List.empty[IPlayer]) // Example binding, adjust as needed

    // Bindings for Integer
    bind[Integer].toInstance(defaultPlayerCount)
  }
}