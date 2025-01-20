package model.fileIoComponent.fileIoXmlIm

import com.google.inject.{Guice, Inject}
import net.codingwell.scalaguice.InjectorExtensions.*
import UNO.MainModule
import model.fileIoComponent.IFileIo
import model.gameComponent.{IPlayer, IPlayerHand, IUnoField}
import scala.xml.{PrettyPrinter, XML}
import java.io.{File, PrintWriter}
import model.gameComponent.gameIm.{PlayerHand, UnoField, Player}
import model.cardComponent.{cardColors, cardValues}
import model.cardComponent.ICard
import model.cardComponent.cardIm.Card

import scala.util.Using
class FileIo @Inject() (var fileName: String) extends IFileIo {
  private val injector = Guice.createInjector(new MainModule)
  override def load: IUnoField = {
    val file = XML.loadFile(fileName)
    val players = (file \ "players" \ "player").map { playerNode =>
      val id = (playerNode \ "id").text.toInt
      val hand = PlayerHand(
        (playerNode \ "hand" \ "card").map { cardNode =>
          val color = cardColors.valueOf((cardNode \ "color").text)
          val value = cardValues.valueOf((cardNode \ "value").text)
          Card(color, value).asInstanceOf[ICard]
        }.toList
      )
      Player(id, hand).asInstanceOf[IPlayer]
    }.toList

    val topCardNode = (file \ "topCard").head
    val topCard = Card(
      color = cardColors.valueOf((topCardNode \ "color").text),
      value = cardValues.valueOf((topCardNode \ "value").text)
    ).asInstanceOf[ICard]

    val currentPlayer = (file \ "currentPlayer").text.toInt
    return UnoField(players, topCard, currentPlayer).asInstanceOf[IUnoField]
    //val injector = Guice.createInjector(new MainModule)
    /*val unoField = injector.instance[IUnoField]

    val players = (file \ "players" \ "player").map { playerNode =>
      // Deserialize player from XML
      injector.instance[IPlayer] // Replace this with actual deserialization logic
    }.toList

    val playerHands = (file \ "playerHands" \ "playerHand").map { handNode =>
      // Deserialize player hand from XML
      injector.instance[IPlayerHand] // Replace this with actual deserialization logic
    }.toList
    unoField*/
  }

  override def save(unoField: IUnoField): Unit = {
    /*Using(new PrintWriter(fileName)) { writer =>
      writer.write(PrettyPrinter(120, 4).format(unoField.toXml))
    }
    val pw = new PrintWriter(new File("uno.xml"))

    pw.write(new scala.xml.PrettyPrinter(80, 2).format(unoField.toXml))
    pw.close()*/
    val xml =
      <unoField>
        <players>
          {unoField.players.map(_.toXml)}
        </players>
        <topCard>
          {unoField.topCard.toXml}
        </topCard>
        <currentPlayer>
          {unoField.currentPlayer}
        </currentPlayer>
      </unoField>

    val prettyPrinter = new PrettyPrinter(80, 2)
    val formattedXml = prettyPrinter.format(xml)
    val pw = new PrintWriter(new File(fileName))
    try {
      pw.write(formattedXml)
    } finally {
      pw.close()
    }
  }
}