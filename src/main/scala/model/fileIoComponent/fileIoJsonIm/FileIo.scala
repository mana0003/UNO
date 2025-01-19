package model.fileIoComponent.fileIoJsonIm

import com.google.inject.{Guice, Inject}
import net.codingwell.scalaguice.InjectorExtensions.*
import UNO.MainModule
import model.fileIoComponent.IFileIo
import model.gameComponent.{IPlayer, IPlayerHand, IUnoField}
import model.gameComponent.gameIm.{PlayerHand, UnoField, Player}
import play.api.libs.json._
import scala.io.Source
import model.cardComponent.ICard
import model.cardComponent.{cardColors, cardValues}
import model.cardComponent.cardIm.Card

object JsonFormats {
  implicit val cardWrites: Writes[ICard] = new Writes[ICard] {
    def writes(card: ICard): JsValue = {
      Json.obj(
        "color" -> card.getColor.toString,
        "value" -> card.getValue.toString
      )
    }
  }

  implicit val cardReads: Reads[ICard] = (json: JsValue) => {
    val color = (json \ "color").as[String]
    val value = (json \ "value").as[String]
    JsSuccess(new Card(cardColors.valueOf(color), cardValues.valueOf(value)))
  }

  implicit val unoFieldReads: Reads[IUnoField] = (json: JsValue) => {
    val players = (json \ "players").as[List[IPlayer]]
    val topCard = (json \ "topCard").as[ICard]
    val currentPlayer = (json \ "currentPlayer").as[Int]
    JsSuccess(UnoField(players, topCard, currentPlayer))
  }

  implicit val unoFieldWrites: Writes[IUnoField] = new Writes[IUnoField] {
    def writes(field: IUnoField): JsValue = {
      Json.obj(
        "players" -> Json.toJson(field.players),
        "topCard" -> Json.toJson(field.topCard),
        "currentPlayer" -> field.currentPlayer
      )
    }
  }

  implicit val playerReads: Reads[IPlayer] = (json: JsValue) => {
    val id = (json \ "id").as[Int]
    val hand = (json \ "hand").as[IPlayerHand]
    JsSuccess(Player(id, hand))
  }

  implicit val playerWrites: Writes[IPlayer] = new Writes[IPlayer] {
    def writes(player: IPlayer): JsValue = {
      Json.obj(
        "id" -> player.id,
        "hand" -> Json.toJson(player.hand)
      )
    }
  }

  implicit val playerHandReads: Reads[IPlayerHand] = (json: JsValue) => {
    val cards = (json \ "cards").as[List[ICard]]
    JsSuccess(PlayerHand(cards))
  }

  implicit val playerHandWrites: Writes[IPlayerHand] = new Writes[IPlayerHand] {
    def writes(hand: IPlayerHand): JsValue = {
      Json.obj(
        "cards" -> Json.toJson(hand.cards)
      )
    }
  }

  implicit val unoFieldFormat: Format[IUnoField] = Format(unoFieldReads, unoFieldWrites)
  implicit val playerFormat: Format[IPlayer] = Format(playerReads, playerWrites)
  implicit val playerHandFormat: Format[IPlayerHand] = Format(playerHandReads, playerHandWrites)
}

class FileIo @Inject extends IFileIo {
  import JsonFormats._

  override def load: IUnoField = {
    val source = Source.fromFile("uno.json")
    val lines = try source.mkString finally source.close()
    val json: JsValue = Json.parse(lines)
    //val unoField = (json \ "unoField").as[IUnoField]
    //val players = (json \ "players").as[List[IPlayer]]
    //val playerHands = (json \ "playerHands").as[List[IPlayerHand]]
    //unoField
    json.as[IUnoField]
  }
  import java.io._

  override def save(unoField: IUnoField): Unit = {
    val json = Json.toJson(unoField)
    val pw = new PrintWriter(new File("uno.json"))
    pw.write(Json.prettyPrint(json))
    pw.close()
  }
}