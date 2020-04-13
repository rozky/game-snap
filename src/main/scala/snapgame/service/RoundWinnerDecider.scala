package snapgame.service

import snapgame.model.PlayerCount

import scala.util.Random


trait RoundWinnerDecider {
  def decide(playerCount: PlayerCount): Long
}

object RandomRoundWinnerDecider extends RoundWinnerDecider {
  override def decide(playerCount: PlayerCount): Long = Math.abs(Random.nextLong()) % playerCount.count
}
