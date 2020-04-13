package snapgame.model

case class PlayerCount private (count: Long) {
  require(count > 1, "Number of players must be greater then 1")
}

object PlayerCount {
  val TWO = PlayerCount(2L)
}


