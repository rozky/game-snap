package snapgame.model

import cats.data.{NonEmptyList, NonEmptyMap}
import cats.implicits._
import snapgame.BaseSpec
import snapgame.model.Card.CARD_0
import snapgame.model.Deck.{EmptyDeck, NonEmptyDeck}
import snapgame.model.GameStyle.BothMatch
import snapgame.model.Score.initialScores
import snapgame.model.SnapGame.{CompletedGame, GameWithSnap, GameWithoutSnap}

class SnapGameSpec extends BaseSpec {

  "newGame" should "create a game with 2 players each having initial score of 0" in {
    // when
    val game: GameWithoutSnap = SnapGame.newGame(PlayerCount.TWO, DeckCount.ONE, BothMatch, Seed.ascendingOrder())

    // then
    game.score should be(Score(NonEmptyMap.of(0L -> 0L, 1L -> 0L)))
    game.uncollectedCards should be(0L)
    game.lastCard should be(None)
  }

  "showNextCard" should "take 1 card from the deck" in {
    val game: GameWithoutSnap = SnapGame.newGame(PlayerCount.TWO, DeckCount.ONE, BothMatch, Seed.ascendingOrder())

    // when
    val (lastGame, lastCard) = SnapGame.showNextCard.run(game).value

    // then
    lastCard should be(Card.CARD_0)

    // and
    lastGame match {
      case actualGame: GameWithoutSnap =>
        actualGame.lastCard should be (Some(Card.CARD_0))
        actualGame.uncollectedCards should be (1)
        actualGame.score should be (game.score)
        actualGame.style should be (game.style)
      case _ =>
        lastGame should be (a[GameWithoutSnap])
    }
  }

  it should "take the last card and complete the game if there is no snap" in {
    val deck = deckWithSingleCard(CARD_0, 1)
    val game: GameWithoutSnap = GameWithoutSnap(initialScores(PlayerCount.TWO), 10L, None, deck, BothMatch)

    // when
    val (lastGame, lastCard) = SnapGame.showNextCard.run(game).value

    // then
    lastCard should be(Card.CARD_0)

    // and
    lastGame should be (a[CompletedGame])
    lastGame.asInstanceOf[CompletedGame].score should be (game.score)
  }

  it should "take the last card but do not complete the game if there is a snap" in {
    val deck = deckWithSingleCard(CARD_0, 1)
    val game: GameWithoutSnap = GameWithoutSnap(initialScores(PlayerCount.TWO), 1L, Some(CARD_0), deck, BothMatch)

    // when
    val (lastGame, lastCard) = SnapGame.showNextCard.run(game).value

    // then
    lastCard should be(Card.CARD_0)

    // and
    lastGame match {
      case actualGame: GameWithSnap =>
        actualGame.uncollectedCards should be (2L)
        actualGame.score should be (game.score)
        actualGame.deck should be (EmptyDeck)
      case _ =>
        lastGame should be (a[GameWithSnap])
    }
  }

  "snap" should "should update score of player winning this round" in {
    val deck = deckWithSingleCard(CARD_0, 1)
    val game = GameWithSnap(initialScores(PlayerCount.TWO), 9L, deck, BothMatch)

    // when
    val (lastGame, _) = SnapGame.snap(1L).run(game).value

    // then
    lastGame match {
      case actualGame: GameWithoutSnap =>
        actualGame.score.getScore(0L).value should be (0L)
        actualGame.score.getScore(1L).value should be (9L)

        // and
        actualGame.lastCard should be (None)
        actualGame.uncollectedCards should be (0L)
        actualGame.deck should be (game.deck)
      case _ =>
        lastGame should be (a[GameWithoutSnap])
    }
  }

  it should "should complete the game if a deck is empty" in {
    val game = GameWithSnap(initialScores(PlayerCount.TWO), 9L, EmptyDeck, BothMatch)

    // when
    val (lastGame, _) = SnapGame.snap(1L).run(game).value

    // then
    lastGame match {
      case actualGame: CompletedGame =>
        actualGame.score.getScore(0L).value should be (0L)
        actualGame.score.getScore(1L).value should be (9L)
      case _ =>
        lastGame should be (a[CompletedGame])
    }
  }

  private def deckWithSingleCard(card: Card, count: Long): NonEmptyDeck = {
    NonEmptyDeck(NonEmptyList.of(CardCounts(card, count)), Seed.ascendingOrder())
  }
}