package snapgame.model

import snapgame.BaseSpec
import snapgame.model.Card.fromIntUnsafe

class GameStyleSpec extends BaseSpec {

  "BothMatch" should "return true if 2 cards are identical" in {
    List.range(0, Card.NUMBER_OF_CARDS).foreach { cardValue =>
      GameStyle.BothMatch(fromIntUnsafe(cardValue), fromIntUnsafe(cardValue)) should be(true)
    }
  }

  it should "return false if 2 cards are not identical" in {
    for {
      cardAIdx <- 0 until Card.NUMBER_OF_CARDS
      cardBIdx <- cardAIdx + 1 until Card.NUMBER_OF_CARDS
      cardA = fromIntUnsafe(cardAIdx)
      cardB = fromIntUnsafe(cardBIdx)
    } yield {
      withClue(s"$cardA vs $cardB")(GameStyle.BothMatch(cardA, cardB) should be(false))
    }
  }

  "SuiteMatch" should "return true if 2 cards have same suit" in {
    for {
      suitIdx <- 0 until Card.NUMBER_OF_SUITES
      cardAIdx <- 0 until Card.NUMBER_OF_VALUES
      cardBIdx <- 0 until Card.NUMBER_OF_VALUES
      if (cardBIdx > cardAIdx)
      cardA = fromIntUnsafe((suitIdx * Card.NUMBER_OF_VALUES) + cardAIdx)
      cardB = fromIntUnsafe((suitIdx * Card.NUMBER_OF_VALUES) + cardBIdx)
    } yield {
      withClue(s"$cardA vs $cardB")(GameStyle.SuiteMatch(cardA, cardB) should be(true))
    }
  }

  it should "return false if 2 cards have diff suit" in {
    for {
      suitAIdx <- 0 until Card.NUMBER_OF_SUITES
      suitBIdx <- suitAIdx + 1 until Card.NUMBER_OF_SUITES
      cardAIdx <- 0 until Card.NUMBER_OF_VALUES
      cardBIdx <- 0 until Card.NUMBER_OF_VALUES
      cardA = fromIntUnsafe((suitAIdx * Card.NUMBER_OF_VALUES) + cardAIdx)
      cardB = fromIntUnsafe((suitBIdx * Card.NUMBER_OF_VALUES) + cardBIdx)
    } yield {
      withClue(s"$cardA vs $cardB")(GameStyle.SuiteMatch(cardA, cardB) should be(false))
    }
  }

  "ValueMatch" should "return true if 2 cards have same value" in {
    for {
      cardAIdx <- 0 until Card.NUMBER_OF_VALUES
      suiteIdx <- 1 until Card.NUMBER_OF_SUITES
      cardA = fromIntUnsafe(cardAIdx)
      cardB = fromIntUnsafe((suiteIdx * Card.NUMBER_OF_VALUES) + cardAIdx)
    } yield {
      withClue(s"$cardA vs $cardB")(GameStyle.ValueMatch(cardA, cardB) should be(true))
    }
  }

  it should "return false if 2 cards have diff value" in {
    for {
      cardAIdx <- 0 until Card.NUMBER_OF_VALUES
      suiteIdx <- 1 until Card.NUMBER_OF_SUITES
      cardBIdx <- 0 until Card.NUMBER_OF_VALUES
      if (cardAIdx != cardBIdx)
      cardA = fromIntUnsafe(cardAIdx)
      cardB = fromIntUnsafe((suiteIdx * Card.NUMBER_OF_VALUES) + cardBIdx)
    } yield {
      withClue(s"$cardA vs $cardB")(GameStyle.ValueMatch(cardA, cardB) should be(false))
    }
  }

}
