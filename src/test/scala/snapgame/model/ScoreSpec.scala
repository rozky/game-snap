package snapgame.model

import cats.data.NonEmptyList
import snapgame.BaseSpec

class ScoreSpec extends BaseSpec {

  "forPlayers" should "create scores for players" in {
    // when
    val scores = Score.initialScores(PlayerCount(2L))

    // then
    scores.getScore(0L) should be(Some(0L))
    scores.getScore(1L) should be(Some(0L))
    scores.getScore(2L) should be(None)
  }

  "increment" should "should increment player score" in {
    val scores = Score.initialScores(PlayerCount(2))

    // when
    val actualScore = scores.incScore(1L, 10L)

    // then
    actualScore.getScore(0L) should be(Some(0L))
    actualScore.getScore(1L) should be(Some(10L))
  }

  it should "should increment multiple player scores multiple times" in {
    val scores = Score.initialScores(PlayerCount(2))

    // when
    val actualScore = scores
      .incScore(1L, 10L)
      .incScore(1L, 10L)
      .incScore(0L, 3L)

    // then
    actualScore.getScore(0L) should be(Some(3L))
    actualScore.getScore(1L) should be(Some(20L))
  }

  "winners" should "get single winner with highest score" in {
    val scores = Score.initialScores(PlayerCount(3))
      .incScore(1L, 10L)

    // when
    val winners = scores.winners

    // then
    winners should be(NonEmptyList.of(1L))
  }

  it should "get multiple winners" in {
    val scores = Score.initialScores(PlayerCount(3))
      .incScore(1L, 10L)
      .incScore(0L, 10L)

    // when
    val winners = scores.winners

    // then
    winners should be(NonEmptyList.of(0L, 1L))
  }
}
