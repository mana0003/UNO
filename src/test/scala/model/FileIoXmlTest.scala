package model

import model.cardComponent.{cardColors, cardValues}
import model.cardComponent.cardIm.Card
import model.gameComponent.gameIm.{Player, PlayerHand, UnoField}
import model.gameComponent.{IPlayer, IPlayerHand, IUnoField}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.fileIoComponent.fileIoXmlIm.FileIo
import scala.xml.{XML, Elem, PrettyPrinter}
import java.io.File


class FileIoXmlTest extends AnyWordSpec with Matchers {
  def normalize(xml: Elem): Elem = {
    def trimText(node: scala.xml.Node): scala.xml.Node = {
      node match {
        case e: scala.xml.Elem =>
          e.copy(child = e.child.map(trimText))
        case t: scala.xml.Text =>
          scala.xml.Text(t.text.trim)
        case other => other
      }
    }
    trimText(xml) match {
      case e: scala.xml.Elem => e
      case _ => throw new IllegalArgumentException("Expected an Elem")
    }
  }

  val testFileName = "testUno.xml"

  "A FileIo" should {

    "correctly load an UnoField from XML" in {
      val xmlData =
        <unoField>
          <players>
            <player>
              <id>1</id>
              <hand>
                <card>
                  <color>RED</color>
                  <value>FIVE</value>
                </card>
                <card>
                  <color>GREEN</color>
                  <value>SKIP</value>
                </card>
              </hand>
            </player>
            <player>
              <id>2</id>
              <hand>
                <card>
                  <color>BLUE</color>
                  <value>DRAW_TWO</value>
                </card>
              </hand>
            </player>
          </players>
          <topCard>
            <color>RED</color>
            <value>FIVE</value>
          </topCard>
          <currentPlayer>0</currentPlayer>
        </unoField>

      val pw = new java.io.PrintWriter(new File(testFileName))
      val prettyPrinter = new PrettyPrinter(80, 2)
      pw.write(prettyPrinter.format(xmlData))
      pw.close()

      val fileIo = new FileIo(testFileName)
      val loadedField = fileIo.load

      loadedField.players should have size 2

      val firstPlayer = loadedField.players.head
      firstPlayer.id should be(1)
      firstPlayer.hand.cards should have size 2
      firstPlayer.hand.cards.head.getColor should be(cardColors.RED)
      firstPlayer.hand.cards.head.getValue should be(cardValues.FIVE)

      val secondPlayer = loadedField.players.last
      secondPlayer.id should be(2)
      secondPlayer.hand.cards.head.getColor should be(cardColors.BLUE)
      secondPlayer.hand.cards.head.getValue should be(cardValues.DRAW_TWO)

      // Top card verification
      loadedField.topCard.getColor should be(cardColors.RED)
      loadedField.topCard.getValue should be(cardValues.FIVE)

      // Current player verification
      loadedField.currentPlayer should be(0)
    }

    "correctly save an UnoField to XML" in {
      val card1 = new Card(cardColors.RED, cardValues.FIVE)
      val card2 = new Card(cardColors.GREEN, cardValues.SKIP)
      val card3 = new Card(cardColors.BLUE, cardValues.DRAW_TWO)

      val player1 = Player(1, PlayerHand(List(card1, card2)))
      val player2 = Player(2, PlayerHand(List(card3)))

      val unoField = UnoField(List(player1, player2), card1, 0)

      val fileIo = new FileIo(testFileName)
      fileIo.save(unoField)

      val loadedXml = normalize(XML.loadFile(testFileName))
      (loadedXml \ "players" \ "player").length should be(2)
      (loadedXml \ "topCard" \ "card" \ "color").text should be("RED")
      (loadedXml \ "topCard" \ "card" \ "value").text should be("FIVE")
      (loadedXml \ "currentPlayer").text should be("0")

      new File(testFileName).delete()
    }

    "correctly parse players from XML" in {
      val xmlData =
        <unoField>
          <players>
            <player>
              <id>1</id>
              <hand>
                <card>
                  <color>RED</color>
                  <value>FIVE</value>
                </card>
                <card>
                  <color>GREEN</color>
                  <value>SKIP</value>
                </card>
              </hand>
            </player>
            <player>
              <id>2</id>
              <hand>
                <card>
                  <color>BLUE</color>
                  <value>DRAW_TWO</value>
                </card>
              </hand>
            </player>
          </players>
        </unoField>

      val pw = new java.io.PrintWriter(new File(testFileName))
      val prettyPrinter = new PrettyPrinter(80, 2)
      pw.write(prettyPrinter.format(xmlData))
      pw.close()

      val fileIo = new FileIo(testFileName)
      val loadedField = fileIo.load

      loadedField.players should have size 2

      val firstPlayer = loadedField.players.head
      firstPlayer.id should be(1)
      firstPlayer.hand.cards should have size 2
      firstPlayer.hand.cards.head.getColor should be(cardColors.RED)
      firstPlayer.hand.cards.head.getValue should be(cardValues.FIVE)

      val secondPlayer = loadedField.players.last
      secondPlayer.id should be(2)
      secondPlayer.hand.cards.head.getColor should be(cardColors.BLUE)
      secondPlayer.hand.cards.head.getValue should be(cardValues.DRAW_TWO)

      new File(testFileName).delete()
    }
  }
}
