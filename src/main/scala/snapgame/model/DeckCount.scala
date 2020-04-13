package snapgame.model

case class DeckCount private (count: Long) {
  require(count > 0, "Number of card sets must be greater then 0")
}

object DeckCount {
  def fromLong(count: Long): Option[DeckCount] = {
    if (count > 0) {
      Some(DeckCount(count))
    } else {
      None
    }
  }
}
