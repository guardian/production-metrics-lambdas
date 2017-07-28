package metricsLambdas.logic

import com.gu.editorialproductionmetricsmodels.models.EventType.CapiContent
import com.gu.editorialproductionmetricsmodels.models.{CapiData, KinesisEvent}
import metricsLambdas.Config._
import metricsLambdas.models.{CapiResult, DebugFields, Tag}
import metricsLambdas.{KinesisWriter, Logging, Parser}
import metricsLambdas.resources.WSClientFactory

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._
import io.circe.syntax._
import io.circe.generic.auto._
import org.joda.time.DateTime

import scala.concurrent.ExecutionContext.Implicits.global

object CapiAPILogic extends Logging {

  private val wsClient = WSClientFactory.createClient

  def closeCapiRequestClient = wsClient.close

  private val capiSearchUrl = s"${capiUrl}search"

  private def capiQueryParams(currentPageNumber: Int) = List(
    ("show-tags", "tracking,newspaper-book"),
    ("show-fields", "internalOctopusCode,internalComposerCode,creationDate"),
    ("from-date", getYesterdaysDate),
    ("to-date", getYesterdaysDate),
    ("show-debug", "true"),
    ("page-size", "10"),
    ("page", currentPageNumber.toString))

  private def getYesterdaysDate = {
    val now = new DateTime()
    val yesterday = now.minusDays(1)
    yesterday.toString("YYYY-MM-dd")
  }

  private def capiRequest(currentPageNumber: Int) =
    wsClient.url(capiSearchUrl).withQueryString(capiQueryParams(currentPageNumber):_*)


  def queryCapi(pageNumber: Int)(implicit context:ExecutionContext) = {
    val response = capiRequest(pageNumber).get().map(r => {
      postArticlesToKinesis(r.body)
    })
    Await.ready(response, 300 seconds)
  }

  def getNumberOfPages = {
    val response = capiRequest(1).get().map(r => extractNumberOfPagesInResponse(r.body))
    Await.ready(response, 300 seconds)
  }

  def extractNumberOfPagesInResponse(response: String): Int =
    Parser.stringToCapiResponse(response).fold(
      error => {
        log.error("Could not parse capi response", error.json)
        0
      },
      _.response.pages)

  private def postArticlesToKinesis(response: String): Unit = {
    Parser.stringToCapiResponse(response).fold(
      error => log.error("Could not parse capi response", error.json),
      capi => capi.response.results.foreach(event => postArticleToKinesis(transform(event))))
  }

  private def postArticleToKinesis(kinesisEvent: KinesisEvent): Unit = KinesisWriter.write(kinesisEvent)

  private def transform(capiResult: CapiResult): KinesisEvent = {
    val capiData = CapiData(
      composerId = capiResult.fields.internalComposerCode,
      storyBundleId = capiResult.fields.internalOctopusCode,
      newspaperBookTag = getTagByType(capiResult.tags, "newspaper-book"),
      creationDate = capiResult.fields.creationDate,
      commissioningDesk = getTagByType(capiResult.tags, "tracking").get,
      startingSystem = getOriginatingSystem(capiResult.debug)
    )
    KinesisEvent(CapiContent, capiData.asJson)
  }

  private def getTagByType(tags: List[Tag], tagType: String): Option[String] =
    tags.collectFirst{case tag: Tag if tag.`type` == tagType => tag.id}

  private def getOriginatingSystem(debugFields: DebugFields) =
    debugFields.originatingSystem.fold("composer")(system => if (system == "fingerpost") "composer" else system.toLowerCase)

}
