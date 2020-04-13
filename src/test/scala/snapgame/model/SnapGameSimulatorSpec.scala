package snapgame.model

import cats.data.NonEmptyList
import snapgame.BaseSpec
import snapgame.service.{SnapGameSimulator, RoundWinnerDecider}

class SnapGameSimulatorSpec extends BaseSpec {

  private val roundWinnerDecider: RoundWinnerDecider = mock[RoundWinnerDecider]

  private val simulator: SnapGameSimulator = SnapGameSimulator(roundWinnerDecider)


  "simulate" should "simulate a game played with 1 deck using BothMatch style" in {
    // when
    val winners = simulator.simulate(PlayerCount.TWO, DeckCount.fromLong(1).get, GameStyle.BothMatch)

    // then
    winners should be (NonEmptyList.of(0, 1))
  }
}
