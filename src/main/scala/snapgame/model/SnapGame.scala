package snapgame.model

import cats.data.IndexedState
import snapgame.model.Deck.{EmptyDeck, NonEmptyDeck, newDeck}
import snapgame.model.Score.initialScores

sealed trait SnapGame {
  def score: Score
}

object SnapGame {

  final case class CompletedGame(override val score: Score) extends SnapGame

  sealed case class GameWithSnap(override val score: Score,
                                 private[model] val uncollectedCards: Long,
                                 private[model] val deck: Deck,
                                 private[model] val style: GameStyle) extends SnapGame

  final case class GameWithoutSnap(override val score: Score,
                                   private[model] val uncollectedCards: Long,
                                   private[model] val lastCard: Option[Card],
                                   private[model] val deck: NonEmptyDeck,
                                   private[model] val style: GameStyle) extends SnapGame

  def newGame(playerCount: PlayerCount, deckCount: DeckCount, style: GameStyle, seed: Seed): GameWithoutSnap = {
    GameWithoutSnap(initialScores(playerCount), 0L, None, newDeck(deckCount, seed), style)
  }

  val showNextCard: IndexedState[GameWithoutSnap, SnapGame, Card] = IndexedState {
    case GameWithoutSnap(score, uncollectedCards, lastCard, deck@NonEmptyDeck(_, _), style) =>

      Deck.takeCard.run(deck).value match {
        case (EmptyDeck, nextCard: Card) if compareCards(nextCard, lastCard, style) =>
          (GameWithSnap(score, uncollectedCards + 1L, EmptyDeck, style), nextCard)

        case (EmptyDeck, nextCard: Card) =>
          (CompletedGame(score), nextCard)

        case (deck: NonEmptyDeck, nextCard: Card) if compareCards(nextCard, lastCard, style) =>
          (GameWithSnap(score, uncollectedCards + 1L, deck, style), nextCard)

        case (deck: NonEmptyDeck, nextCard: Card) =>
          (GameWithoutSnap(score, uncollectedCards + 1L, Some(nextCard), deck, style), nextCard)
      }
  }

  def snap(player: Long): IndexedState[GameWithSnap, SnapGame, Any] = IndexedState {
    case GameWithSnap(score, uncollectedCards, EmptyDeck, _) =>
      (CompletedGame(score.incScore(player, uncollectedCards)), ())

    case GameWithSnap(score, uncollectedCards, deck: NonEmptyDeck, style) =>
      (GameWithoutSnap(score.incScore(player, uncollectedCards), 0, None, deck, style), ())
  }

  private def compareCards(currentCard: Card, lastCard: Option[Card], style: GameStyle): Boolean = {
    lastCard.filter(last => style(last, currentCard)).isDefined
  }
}
