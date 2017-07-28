package metricsLambdas

import cats.syntax.either._
import io.circe.{Json, parser}
import io.circe.generic.auto._
import metricsLambdas.models.CapiResponse

sealed trait ParsingError {def json: String}
case class StringToJsonParsingError(json: String) extends ParsingError
case class JsonToCapiResponseParsingError(json: String) extends ParsingError

object Parser {

  def stringToCapiResponse(json: String): Either[ParsingError, CapiResponse] = {
    for {
      parsedJson <- stringToJson(json)
      capiResponse <- jsonToCapiResponse(parsedJson)
    } yield capiResponse
  }

  def stringToJson(json: String): Either[ParsingError, Json] =
    parser.parse(json).fold(_ => Left(StringToJsonParsingError(json)), Right(_))

  def jsonToCapiResponse(json: Json): Either[ParsingError, CapiResponse] =
    json.as[CapiResponse].fold(_ => Left(JsonToCapiResponseParsingError(json.toString)), Right(_))
}
