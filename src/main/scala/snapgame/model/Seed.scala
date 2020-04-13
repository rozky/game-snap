package snapgame.model

import scala.collection.immutable.Queue
import scala.util.Random

trait Seed {
  def current: Long
  def next: Seed
}

object Seed {
  final case class Randomized private (current: Long) extends Seed {
    require(current >= 0, "Seed must be greater or equal to 0")

    def next: Randomized = Randomized(Math.abs(new Random(current).nextLong()))
  }

  final case class Fixed private(current: Long, nextSeeds: Queue[Long]) extends Seed {
    require(current >= 0, "Seed must be greater or equal to 0")
    def next: Fixed = nextSeeds match {
      case x +: xs => Fixed(x, xs)
      case _ => this
    }
  }

  def randomized(seed: Long): Seed = Randomized(Math.abs(seed))

  def randomized(): Seed = Randomized(Math.abs(Random.nextLong()))

  def fixed(seed: Long): Seed = Fixed(Math.abs(seed), Queue.empty)

  def fixed(seed: Long, seeds: Long*): Seed = Fixed(Math.abs(seed), Queue.apply(seeds.map(Math.abs(_)):_*))
}


