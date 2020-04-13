package snapgame.service

import cats.data.NonEmptyList
import cats.implicits._
import snapgame.model.SnapGame.{CompletedGame, GameWithSnap, GameWithoutSnap, newGame}
import snapgame.model.{DeckCount, GameStyle, PlayerCount, Seed, SnapGame}

case class SnapGameSimulator(roundWinnerDecider: RoundWinnerDecider) {

  def simulate(playerCount: PlayerCount, deckCount: DeckCount, style: GameStyle): NonEmptyList[Long] = {
    val result: Either[CompletedGame, SnapGame] = Stream.continually(0)
      .foldLeftM(newGame(playerCount, deckCount, style, Seed.randomized()).asInstanceOf[SnapGame]) {
        case (game: CompletedGame, _) =>
          Either.left(game)

        case (game: GameWithoutSnap, _) =>
          Either.right(SnapGame.showNextCard.run(game).map(_._1).value)

        case (game: GameWithSnap, _) =>
          val winner = roundWinnerDecider.decide(playerCount)
          Either.right(SnapGame.snap(winner).run(game).map(_._1).value)
      }

    result match {
      case Left(CompletedGame(score)) => score.winners
      case _ => throw new IllegalStateException("the game is not over")
    }
  }
}
