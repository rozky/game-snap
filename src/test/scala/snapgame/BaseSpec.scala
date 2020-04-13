package snapgame

import org.scalatest.{EitherValues, FlatSpec, Matchers, OptionValues}
import org.scalatestplus.mockito.MockitoSugar

trait BaseSpec extends FlatSpec with Matchers with OptionValues with EitherValues with MockitoSugar
