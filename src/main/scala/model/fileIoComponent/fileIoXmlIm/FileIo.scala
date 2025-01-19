package model.fileIoComponent.fileIoXmlIm

import com.google.inject.{Guice, Inject}
import net.codingwell.scalaguice.InjectorExtensions.*
import UNO.MainModule
import model.fileIoComponent.IFileIo
import model.gameComponent.{IPlayer, IPlayerHand, IUnoField}
import scala.xml.{PrettyPrinter, XML}
import java.io.{File, PrintWriter}
import scala.util.Using
class FileIo @Inject() (var fileName: String) extends IFileIo {
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

  override def save(unoField: IUnoField): Unit = {
    Using(new PrintWriter(fileName)) { writer =>
      writer.write(PrettyPrinter(120, 4).format(unoField.toXml))
    }
    val pw = new PrintWriter(new File("uno.xml"))
    
    pw.write(new scala.xml.PrettyPrinter(80, 2).format(unoField.toXml))
    pw.close()
  }
}