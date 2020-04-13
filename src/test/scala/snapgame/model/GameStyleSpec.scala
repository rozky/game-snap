package snapgame.model

import snapgame.BaseSpec

class GameStyleSpec extends BaseSpec {

  "Identical" should "return true if 2 cards are identical" in {
    List.range(0, Card.NUMBER_OF_CARDS).foreach { cardValue =>
      GameStyle.BothMatch(Card(cardValue), Card(cardValue)) should be(true)
      GameStyle.BothMatch(Card(cardValue), Card(cardValue + Card.NUMBER_OF_CARDS)) should be(true)
    }
  }

  it should "return false if 2 cards are not identical" in {
    for {
      cardIdxA <- 0 until Card.NUMBER_OF_CARDS
      cardIdxB <- cardIdxA + 1 until Card.NUMBER_OF_CARDS
      cardA = Card(cardIdxA) // todo don't create cards
      cardB = Card(cardIdxB)
    } yield {
      withClue(s"$cardA vs $cardB")(GameStyle.BothMatch(cardA, cardB) should be(false))
    }
  }

  "Suite" should "return true if 2 cards have same suit" in {
    for {
      cardIdxA <- 0 until Card.NUMBER_OF_VALUES
      cardIdxB <- 0 until Card.NUMBER_OF_VALUES
      cardA = Card(cardIdxA)
      cardB = Card(cardIdxB)
      cardC = Card(cardIdxB + Card.NUMBER_OF_CARDS)
    } yield {
      withClue(s"$cardA vs $cardB")(GameStyle.SuiteMatch(cardA, cardB) should be(true))
      withClue(s"$cardA vs $cardC")(GameStyle.SuiteMatch(cardA, cardC) should be(true))
    }
  }

  it should "return false if 2 cards have diff suit" in {
    for {
      cardIdxA <- 0 until Card.NUMBER_OF_VALUES
      suiteIdx <- 1 until Card.NUMBER_OF_SUITES
      cardA = Card(cardIdxA)
      cardB = Card(cardIdxA + suiteIdx * Card.NUMBER_OF_VALUES)
    } yield {
      withClue(s"$cardA vs $cardB")(GameStyle.SuiteMatch(cardA, cardB) should be(false))
    }
  }

  "Value" should "return true if 2 cards have same value" in {
    for {
      cardIdxA <- 0 until Card.NUMBER_OF_VALUES
      suiteIdx <- 0 until 2 * Card.NUMBER_OF_SUITES
      cardA = Card(cardIdxA)
      cardB = Card(cardIdxA + suiteIdx * Card.NUMBER_OF_VALUES)
    } yield {
      withClue(s"$cardA vs $cardB")(GameStyle.ValueMatch(cardA, cardB) should be(true))
    }
  }

  it should "return false if 2 cards have diff value" in {
    for {
      cardIdxA <- 0 until Card.NUMBER_OF_VALUES
      cardIdxB <- cardIdxA + 1 until Card.NUMBER_OF_VALUES
      cardA = Card(cardIdxA)
      cardB = Card(cardIdxB)
    } yield {
      withClue(s"$cardA vs $cardB")(GameStyle.ValueMatch(cardA, cardB) should be(false))
    }
  }

}
