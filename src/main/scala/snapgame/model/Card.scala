package snapgame.model

case class Card private (id: Int) {
  require(id >= 0 && id < Card.NUMBER_OF_CARDS, s"id must be in a range of [0, ${Card.NUMBER_OF_CARDS}")
}

object Card {
  val NUMBER_OF_SUITES = 4
  val NUMBER_OF_VALUES = 13
  val NUMBER_OF_CARDS = NUMBER_OF_SUITES * NUMBER_OF_VALUES

  val CARDS: List[Card] = List.range(0, NUMBER_OF_CARDS).map(id => Card(id))

  val CARS_MAP: Map[Int, Card] = CARDS.map(card => (card.id, card)).toMap

  def fromInt(id: Int): Option[Card] = {
    CARS_MAP.get(id)
  }

  def fromIntUnsafe(id: Int): Card = {
    require(id >= 0 && id < Card.NUMBER_OF_CARDS, s"id must be in a range of [0, ${Card.NUMBER_OF_CARDS}")
    CARS_MAP.get(id).get
  }
}
