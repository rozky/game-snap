package snapgame.model

case class CardCounts private (card: Card, count: Long) {
  require(count > 0, "count must be greater than 0")
}
