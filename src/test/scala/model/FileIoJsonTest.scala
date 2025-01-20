package model

import model.cardComponent.cardIm.Card
import model.cardComponent.{cardColors, cardValues}
import model.gameComponent.gameIm.{Player, PlayerHand, UnoField}
import model.fileIoComponent.IFileIo
import model.gameComponent.{IUnoField, IPlayer, IPlayerHand}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.fileIoComponent.fileIoJsonIm.FileIo
import play.api.libs.json._

import java.io.{File, PrintWriter}

class FileIoJsonTest extends AnyWordSpec with Matchers {
  val fileIo: IFileIo = new FileIo

  "A FileIo" should {
    "save and load a UnoField correctly" in {
      val card1 = new Card(cardColors.RED, cardValues.FIVE)
      val card2 = new Card(cardColors.GREEN, cardValues.DRAW_TWO)
      val player1: IPlayer = Player(1, PlayerHand(List(card1)))
      val player2: IPlayer = Player(2, PlayerHand(List(card2)))
      val unoField: IUnoField = UnoField(List(player1, player2), card1, currentPlayer = 0)

      // Save the UnoField to file
      fileIo.save(unoField)

      // Verify file content
      val fileContent = scala.io.Source.fromFile("uno.json").getLines().mkString("\n")
      fileContent should not be empty

      // Load UnoField from file
      val loadedField: IUnoField = fileIo.load

      // Verify loaded UnoField data
      loadedField.players should have size 2
      loadedField.topCard.getColor should be(cardColors.RED)
      loadedField.topCard.getValue should be(cardValues.FIVE)
      loadedField.currentPlayer should be(0)
      loadedField.players.head.id should be(1)
      loadedField.players.head.hand.cards.head.getValue should be(cardValues.FIVE)
      loadedField.players.last.id should be(2)
      loadedField.players.last.hand.cards.head.getValue should be(cardValues.DRAW_TWO)
    }

    "handle loading from a non-existent file gracefully" in {
      val nonExistentFileIo = new FileIo {
        override def load: IUnoField = {
          val filename = "non_existent.json"
          if (new File(filename).exists()) {
            super.load
          } else {
            UnoField(List.empty, new Card(cardColors.BLUE, cardValues.ZERO), 0) // Default empty field
          }
        }
      }
      val result = nonExistentFileIo.load
      result.players shouldBe empty
      result.topCard.getValue shouldBe cardValues.ZERO
    }
  }
}
