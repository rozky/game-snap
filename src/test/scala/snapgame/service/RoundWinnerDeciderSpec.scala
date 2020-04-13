package snapgame.service

import snapgame.BaseSpec
import snapgame.model.PlayerCount

class RoundWinnerDeciderSpec extends BaseSpec {

  "RandomRoundWinnerDecider" should "pick winner at random" in {
    // when
    val results = Stream.range(0, 10000)
      .map(_ => RandomRoundWinnerDecider.decide(PlayerCount.TWO))
      .groupBy(identity)
      .mapValues(_.size)

    // then
    results should have size (2)
    results.get(0L).value should be > 4700
    results.get(1L).value should be > 4700
  }
}
