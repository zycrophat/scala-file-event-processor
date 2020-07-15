package zycrophat.fileeventprocessor

import java.time.LocalDate

import com.github.plokhotnyuk.jsoniter_scala.core._
import com.github.plokhotnyuk.jsoniter_scala.macros._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MyFunctionTest extends AnyFlatSpec with Matchers  {
  implicit val codec1: JsonValueCodec[FileMetadata] = JsonCodecMaker.make

  "Json" must "be produced" in {
    val fileMetadata =
      FileMetadata(id = "12345", name = "blabla", LocalDate.of(2020, 7, 15).atTime(13, 37, 23, 223322332))

      writeToString(fileMetadata) shouldBe """{"id":"12345","name":"blabla","timestamp":"2020-07-15T13:37:23.223322332"}"""
  }
}
