package snapgame.model

import snapgame.BaseSpec
import snapgame.model.Card.fromInt

class CardSpec extends BaseSpec {

  "Card" should "configure correct constants" in {
    Card.NUMBER_OF_SUITES should be (4)
    Card.NUMBER_OF_VALUES should be (13)
    Card.NUMBER_OF_CARDS should be (52)
    Card.CARDS should have size(52)
  }

  "getCard" should "return card by for invalid id" in {
    List.range(0, 52).foreach { cardId =>
      fromInt(cardId) should be(Some(Card(cardId)))
    }
  }

  it should "return None if id is invalid" in {
    fromInt(-1) should be (None)
    fromInt(53) should be (None)
  }
}
