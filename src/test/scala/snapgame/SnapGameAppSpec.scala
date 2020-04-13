package snapgame

import java.io.{ByteArrayOutputStream, StringReader}

class SnapGameAppSpec extends BaseSpec {
  "main" should "run a game if valid inputs are provided" in {
    val inputs =
      """|1
         |both
      """.stripMargin

    // when
    val output = callMain(inputs)

    // then
    output should include("How many playing card decks to play with?")
    output should include("How cards should be matched: on [suit], [value] or [both]?")
    output should include("Winners are players with numbers: [ 0, 1 ]")
  }

  it should "should re-ask the deck count if invalid value is provided initially" in {
    val inputs =
      """|abc
         |1
         |both
      """.stripMargin

    // when
    val output = callMain(inputs)

    // then
    output should include("How many playing card decks to play with?How many playing card decks to play with?")
    output should include("How cards should be matched: on [suit], [value] or [both]?")
    output should include("Winners are players with numbers: [ 0, 1 ]")
  }

  private def callMain(inputs: String): String = {
    val out = new ByteArrayOutputStream()
    Console.withOut(out) {
      Console.withIn(new StringReader(inputs))  {

        SnapGameApp.main(Array())
      }
    }

    out.toString
  }
}
