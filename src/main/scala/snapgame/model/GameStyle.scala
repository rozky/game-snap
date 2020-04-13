package snapgame.model

sealed trait GameStyle extends ((Card, Card) => Boolean)

object GameStyle {
  final object BothMatch extends GameStyle {
    override def apply(cardA: Card, cardB: Card): Boolean = {
      cardA.id % Card.NUMBER_OF_CARDS == cardB.id % Card.NUMBER_OF_CARDS
    }
  }

  final object SuiteMatch extends GameStyle {
    override def apply(cardA: Card, cardB: Card): Boolean = {
      cardA.id / Card.NUMBER_OF_VALUES == cardB.id / Card.NUMBER_OF_CARDS
    }
  }

  final object ValueMatch extends GameStyle {
    override def apply(cardA: Card, cardB: Card): Boolean = {
      cardA.id % Card.NUMBER_OF_VALUES == cardB.id % Card.NUMBER_OF_VALUES
    }
  }
}
