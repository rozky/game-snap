package snapgame.model

import cats.data._
import cats.implicits._
import snapgame.BaseSpec
import snapgame.model.Card.fromIntUnsafe
import snapgame.model.Deck.{EmptyDeck, NonEmptyDeck}

class DeckSpec extends BaseSpec {

  "newDeck" should "should initialize deck correctly" in {

    // when
    val deck = Deck.newDeck(DeckCount(1), Seed.fixed(1L))

    // then
    deck.cardCounts.toList should be(Card.CARDS.map(CardCounts(_, 1)))
    deck.seed should be(Seed.fixed(1L))
    deck.cardCount should be(Card.NUMBER_OF_CARDS)
  }

  "takeCard" should "take 1 card from the deck containing 1 set of cards" in {
    val initDeck = Deck.newDeck(DeckCount(1), Seed.randomized(1L))

    // when
    val program = for {
      card <- takeCardUnsafe
    } yield card

    val (lastDeck, card) = program.run(initDeck).value

    // then correct card have been taken from the deck
    card should be(fromIntUnsafe(1))

    // and the taken card have been removed from the deck
    findCardCount(card, lastDeck) should be(None)

    // and other cards are unaffected
    Card.CARDS
      .filter(_ != card)
      .foreach { expectedCard =>
        findCardCount(expectedCard, lastDeck).value should be(CardCounts(expectedCard, 1))
      }

    // and seed was updated
    lastDeck.seed should be(Seed.randomized(4964420948893066024L))
  }

  it should "take 1 card from the deck containing 2 sets of cards" in {
    val initDeck = Deck.newDeck(DeckCount(2), Seed.fixed(1L))

    // when
    val program = for {
      card <- takeCardUnsafe
    } yield card

    val (lastDeck, card) = program.run(initDeck).value

    // then correct card have been taken from the deck
    card should be(fromIntUnsafe(0))

    // and card count of selected card is decremented by 1
    findCardCount(card, lastDeck).value should be(CardCounts(card, 1))

    // and other cards are unaffected
    Card.CARDS
      .filter(_ != card)
      .foreach { expectedCard =>
        findCardCount(expectedCard, lastDeck).value should be(CardCounts(expectedCard, 2))
      }
  }

  it should "take card from the deck" in {
    val deck = Deck.newDeck(DeckCount(1), Seed.fixed(1L, 50L, 5L))

    // when
    val program = for {
      card1 <- takeCardUnsafe
      card2 <- takeCardUnsafe
      card3 <- takeCardUnsafe
    } yield List(card1, card2, card3)

    val (lastDeck, playedCards) = program.run(deck).value

    // then expected cards have been taken
    playedCards should be(List(fromIntUnsafe(1), fromIntUnsafe(51), fromIntUnsafe(6)))

    // and the taken cards have been removed from the deck
    playedCards.foreach { card =>
      findCardCount(card, lastDeck) should be(None)
    }

    // and other cards are unaffected
    Card.CARDS
      .filter(!playedCards.contains(_))
      .foreach { expectedCard =>
        findCardCount(expectedCard, lastDeck).value should be(CardCounts(expectedCard, 1))
      }
  }

  it should "take all cards from the deck" in {
    val deck = Deck.newDeck(DeckCount(1), Seed.fixed(0L))

    // when
    type Agg = (Deck, List[Card])
    val result: Either[Agg, Agg] = Stream.continually(0)
      .foldLeftM((deck.asInstanceOf[Deck], List.empty[Card])) {
        case ((EmptyDeck, cards), _) => Either.left((EmptyDeck, cards))
        case ((deck: NonEmptyDeck, cards), _) =>
          Deck.takeCard.run(deck).value match {
            case (newDeck, card) => Either.right((newDeck, cards :+ card))
          }
      }

    // then
    result.left.value._1 should be(EmptyDeck)
    result.left.value._2 should have size (Card.NUMBER_OF_CARDS)
    result.left.value._2.zipWithIndex.foreach { case (card, idx) =>
      card should be(Card.fromIntUnsafe(idx))
    }
  }

  private val takeCardUnsafe: IndexedState[NonEmptyDeck, NonEmptyDeck, Card] = {
    Deck.takeCard.bimap(_.asInstanceOf[NonEmptyDeck], identity)
  }

  private def findCardCount(card: Card, deck: NonEmptyDeck): Option[CardCounts] = {
    deck.cardCounts.find(_.card == card)
  }
}
