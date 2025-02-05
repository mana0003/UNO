package UNO
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import controller.controllerComponent.{ControllerIm, IUnoController}
import controller.controllerComponent.ControllerIm.UnoController
import model.gameComponent.{IPlayer, IPlayerHand, IUnoField}
import model.gameComponent.gameIm.{Player, PlayerHand, UnoField}
import model.cardComponent.cardIm.Card
import model.cardComponent.{ICard, cardColors, cardValues}
import model.fileIoComponent.IFileIo
import model.fileIoComponent.fileIoJsonIm.FileIo

class MainModule extends AbstractModule with ScalaModule {

  private val defaultPlayerCount: Int = 2

  override def configure(): Unit = {
    bind[IUnoField].to[UnoField]
    bind[IUnoController].to[UnoController]
    bind[ICard].to[Card]
    bind[IPlayer].to[Player]
    bind[IPlayerHand].to[PlayerHand]
    bind(classOf[IUnoController]).to(classOf[ControllerIm.UnoController])
    bind(classOf[IFileIo]).to(classOf[model.fileIoComponent.fileIoJsonIm.FileIo])
    bind(classOf[IUnoField]).to(classOf[UnoField])


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
