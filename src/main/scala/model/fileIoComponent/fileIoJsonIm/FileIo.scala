package model.fileIoComponent.fileIoJsonIm

import com.google.inject.Guice
import net.codingwell.scalaguice.InjectorExtensions.*
import UNO.MainModule
import model.fileIoComponent.IFileIo
import model.gameComponent.{IPlayer, IPlayerHand, IUnoField}
import play.api.libs.json._
import scala.io.Source

// Define custom Reads and Writes for IUnoField, IPlayer, and IPlayerHand
object JsonFormats {
  implicit val unoFieldReads: Reads[IUnoField] = new Reads[IUnoField] {
    def reads(json: JsValue): JsResult[IUnoField] = {
      // Implement custom deserialization logic for IUnoField
      JsSuccess(Guice.createInjector(new MainModule).instance[IUnoField])
    }
  }

  implicit val unoFieldWrites: Writes[IUnoField] = new Writes[IUnoField] {
    def writes(unoField: IUnoField): JsValue = {
      // Implement custom serialization logic for IUnoField
      Json.obj()
    }
  }

  implicit val playerReads: Reads[IPlayer] = new Reads[IPlayer] {
    def reads(json: JsValue): JsResult[IPlayer] = {
      // Implement custom deserialization logic for IPlayer
      JsSuccess(Guice.createInjector(new MainModule).instance[IPlayer])
    }
  }

  implicit val playerWrites: Writes[IPlayer] = new Writes[IPlayer] {
    def writes(player: IPlayer): JsValue = {
      // Implement custom serialization logic for IPlayer
      Json.obj()
    }
  }

  implicit val playerHandReads: Reads[IPlayerHand] = new Reads[IPlayerHand] {
    def reads(json: JsValue): JsResult[IPlayerHand] = {
      // Implement custom deserialization logic for IPlayerHand
      JsSuccess(Guice.createInjector(new MainModule).instance[IPlayerHand])
    }
  }

  implicit val playerHandWrites: Writes[IPlayerHand] = new Writes[IPlayerHand] {
    def writes(playerHand: IPlayerHand): JsValue = {
      // Implement custom serialization logic for IPlayerHand
      Json.obj()
    }
  }

  implicit val unoFieldFormat: Format[IUnoField] = Format(unoFieldReads, unoFieldWrites)
  implicit val playerFormat: Format[IPlayer] = Format(playerReads, playerWrites)
  implicit val playerHandFormat: Format[IPlayerHand] = Format(playerHandReads, playerHandWrites)
}

class FileIo extends IFileIo {
  import JsonFormats._

  override def load: IUnoField = {
    val source = Source.fromFile("uno.json")
    val lines = try source.mkString finally source.close()
    val json: JsValue = Json.parse(lines)
    val unoField = (json \ "unoField").as[IUnoField]
    val players = (json \ "players").as[List[IPlayer]]
    val playerHands = (json \ "playerHands").as[List[IPlayerHand]]
    unoField
  }

  override def save(unoField: IUnoField, players: List[IPlayer], playerHands: List[IPlayerHand]): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("uno.json"))
    pw.write(Json.prettyPrint(Json.obj(
      "unoField" -> Json.toJson(unoField),
      "players" -> Json.toJson(players),
      "playerHands" -> Json.toJson(playerHands)
    )))
    pw.close()
  }
}