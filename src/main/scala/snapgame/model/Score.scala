package snapgame.model

import cats.implicits._
import cats.data.NonEmptyList.fromListUnsafe
import cats.data.{NonEmptyList, NonEmptyMap}

case class Score(private val scores: NonEmptyMap[Long, Long]) {
  def getScore(player: Long): Option[Long] = scores.lookup(player)

  def incScore(player: Long, increment: Long): Score =  {
    Score(scores.updateWith(player)(_ + increment))
  }

  def winners: NonEmptyList[Long] = {
    val winners: List[Long] = scores.toNel
      .toList
      .groupBy(_._2)
      .maxBy(_._1)
      ._2
      .map(_._1)

    NonEmptyList.fromListUnsafe(winners)
  }
}

object Score {

  def initialScores(players: PlayerCount): Score = {
    Score(fromListUnsafe(List.range(0L, players.count)).map(it => (it, 0L)).toNem)
  }
}
