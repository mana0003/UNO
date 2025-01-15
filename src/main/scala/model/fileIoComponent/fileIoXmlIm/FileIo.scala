package model.fileIoComponent.fileIoXmlIm

import com.google.inject.Guice
import net.codingwell.scalaguice.InjectorExtensions.*
import UNO.MainModule
import model.fileIoComponent.IFileIo
import model.gameComponent.{IPlayer, IPlayerHand, IUnoField}
import scala.xml.{Elem, XML}
import java.io.{File, PrintWriter}

class FileIo extends IFileIo {
  override def load: IUnoField = {
    val file = XML.loadFile("uno.xml")
    val injector = Guice.createInjector(new MainModule)
    val unoField = injector.instance[IUnoField]
    
    val players = (file \ "players" \ "player").map { playerNode =>
      // Deserialize player from XML
      injector.instance[IPlayer] // Replace this with actual deserialization logic
    }.toList
    
    val playerHands = (file \ "playerHands" \ "playerHand").map { handNode =>
      // Deserialize player hand from XML
      injector.instance[IPlayerHand] // Replace this with actual deserialization logic
    }.toList
    unoField
  }

  override def save(unoField: IUnoField, players: List[IPlayer], playerHands: List[IPlayerHand]): Unit = {
    val xml =
      <uno>
        <unoField>
          <!-- Serialize unoField to XML -->
        </unoField>
        <players>
          {players.map { player =>
            <player>
              <!-- Serialize player to XML -->
            </player>
          }}
        </players>
        <playerHands>
          {playerHands.map { hand =>
            <playerHand>
              <!-- Serialize player hand to XML -->
            </playerHand>
          }}
        </playerHands>
      </uno>

    val pw = new PrintWriter(new File("uno.xml"))
    pw.write(new scala.xml.PrettyPrinter(80, 2).format(xml))
    pw.close()
  }
}