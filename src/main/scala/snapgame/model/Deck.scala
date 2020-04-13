package snapgame.model

import cats.data.NonEmptyList.fromListUnsafe
import cats.data.{IndexedState, NonEmptyList}

sealed trait Deck

object Deck {

  final case class NonEmptyDeck(cardCounts: NonEmptyList[CardCounts],
                                seed: Seed) extends Deck {
    private[model] lazy val cardCount: Long = cardCounts.map(_.count).toList.sum
  }

  final case object EmptyDeck extends Deck

  private[model] def newDeck(deckCount: DeckCount, seed: Seed): NonEmptyDeck = {

    val cardCounts = fromListUnsafe(Card.CARDS.map(card => CardCounts(card, deckCount.count)))

    NonEmptyDeck(cardCounts, seed)
  }

  val takeCard: IndexedState[NonEmptyDeck, Deck, Card] = IndexedState {
    case _@NonEmptyDeck(NonEmptyList(CardCounts(card, 1), Nil), _) =>
      (EmptyDeck, card)

    case deck@NonEmptyDeck(cardCounts, seed) =>
      val nextSlotIdx = seed.current % deck.cardCount

      val (_, newCardCounts, nextCard) = cardCounts.foldLeft((0L, List[CardCounts](), Option.empty[Card])) {
        // selected card
        case ((sum, newCounts, _), CardCounts(card, count)) if isInsideRange(nextSlotIdx, sum, sum + count) =>
          if (count == 1) {
            (sum + count, newCounts, Some(card))
          } else {
            (sum + count, newCounts :+ CardCounts(card, count - 1), Some(card))
          }

        // not selected card
        case ((sum, newCounts, nextCard), cc@CardCounts(_, count)) =>
          (sum + count, newCounts :+ cc, nextCard)
      }

      (NonEmptyDeck(fromListUnsafe(newCardCounts), seed.next), nextCard.get)
  }

  private def isInsideRange(value: Long, min: Long, max: Long): Boolean = {
    value >= min && value < max
  }
}

