package snapgame.model

import snapgame.BaseSpec

import cats.implicits._
class TestSpec extends BaseSpec {

  it should "d" in {
    println("Result: " + sumEven(Stream(2, 2, 2, 3, 11)).bifoldMap(identity, identity))
    println("Result: " + sumEven(Stream(2, 7, 2, 3, 11)).bifoldMap(identity, identity))
  }

  def sumEven(nums: Stream[Int]): Either[Int, Int] = {
    nums.foldM(0) {
      case (acc, n) if n % 2 == 0 => Either.right(acc + n)
      case (acc, n) => {
        println(s"Stopping on number: $n")
        Either.left(acc)
      }
    }
  }
}
