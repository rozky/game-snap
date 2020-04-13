package snapgame.model

import cats.data.NonEmptyList
import org.mockito.Mockito
import snapgame.BaseSpec
import snapgame.service.{RoundWinnerDecider, SnapGameSimulator}

class SnapGameSimulatorSpec extends BaseSpec {

  private val roundWinnerDecider: RoundWinnerDecider = mock[RoundWinnerDecider]

  private val simulator: SnapGameSimulator = SnapGameSimulator(roundWinnerDecider)


  "simulate" should "simulate a game played with 1 deck using BothMatch style with no snap" in {
    // when
    val scores = simulator.simulate(PlayerCount.TWO, DeckCount.ONE, GameStyle.BothMatch, Seed.ascendingOrder())

    // then
    scores.winners should be (NonEmptyList.of(0L, 1L))

    // and
    scores.getScore(0L).value should be(0L)
    scores.getScore(1L).value should be(0L)
  }

  it should "simulate a game played with 2 decks using BothMatch style with snaps" in {
    Mockito.when(roundWinnerDecider.decide(PlayerCount.TWO)).thenReturn(1L)

    // when
    // initial card slots: 0,0,1,1,2,2,3,3,4,4,5,5 ...
    val scores = simulator.simulate(PlayerCount.TWO, DeckCount.TWO, GameStyle.BothMatch, Seed.fixed(0L))

    // then
    scores.winners should be (NonEmptyList.of(1L))

    // and
    scores.getScore(0L).value should be(0L)
    scores.getScore(1L).value should be(104L)
  }

  it should "simulate a game played with 1 deck using SuiteMatch style" in {
    Mockito.when(roundWinnerDecider.decide(PlayerCount.TWO)).thenReturn(1L)

    // when
    val scores = simulator.simulate(PlayerCount.TWO, DeckCount.ONE, GameStyle.SuiteMatch, Seed.ascendingOrder())

    // then
    scores.winners should be (NonEmptyList.of(1L))

    // and
    scores.getScore(0L).value should be(0L)
    scores.getScore(1L).value should be(51L)
  }

  it should "simulate a game played with 1 deck using ValueMatch style" in {
    Mockito.when(roundWinnerDecider.decide(PlayerCount.TWO)).thenReturn(0L)

    // when
    // card order: 50, 13, 0, 1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 15,...., 49, 51
    // single match
    val scores = simulator.simulate(PlayerCount.TWO, DeckCount.ONE, GameStyle.ValueMatch, Seed.fixed(50L, 13L, 0L))

    // then
    scores.winners should be (NonEmptyList.of(0L))

    // and
    scores.getScore(0L).value should be(3L)
    scores.getScore(1L).value should be(0L)
  }
}
