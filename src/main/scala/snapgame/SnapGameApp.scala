package snapgame

import cats.data.NonEmptyList
import snapgame.model.{DeckCount, GameStyle, PlayerCount}
import snapgame.service.{SnapGameSimulator, RandomRoundWinnerDecider}

import scala.io.StdIn

object SnapGameApp extends App {
  val playerCount = PlayerCount.TWO
  val deckCount = askDeckCount()
  val style = askStyle()

  private val simulator = SnapGameSimulator(RandomRoundWinnerDecider)
  private val winners: NonEmptyList[Long] = simulator.simulate(playerCount, deckCount, GameStyle.BothMatch)

  println(s"Winners are: $winners")

  private def askDeckCount(): DeckCount = {
    var count: Option[DeckCount] = None
    while(count.isEmpty) {
      count = parseDeckCount(StdIn.readLine("How many playing card decks to play with?"))
    }

    count.get
  }

  private def parseDeckCount(count: String): Option[DeckCount] = {
    try {
      DeckCount.fromLong(count.toLong)
    } catch {
      case _ : Exception => None
    }
  }

  private def askStyle(): GameStyle = {
    var style: Option[GameStyle] = None
    while(style.isEmpty) {
      style = parseStyle(StdIn.readLine("How cards should be matched: on [suit], [value] or [both]?"))
    }

    style.get
  }

  private def parseStyle(style: String): Option[GameStyle] = {
    style match {
      case "both" => Some(GameStyle.BothMatch)
      case "suit" => Some(GameStyle.SuiteMatch)
      case "value" => Some(GameStyle.ValueMatch)
      case _ => None
    }
  }
}
