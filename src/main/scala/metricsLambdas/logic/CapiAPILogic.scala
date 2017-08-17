package metricsLambdas.logic

import com.gu.contentapi.client.GuardianContentClient
import com.gu.contentapi.client.model.SearchQuery
import com.gu.contentapi.client.model.v1.TagType.{NewspaperBook, Tracking}
import com.gu.contentapi.client.model.v1.{Content, Debug, Tag, TagType}
import com.gu.editorialproductionmetricsmodels.models.EventType.CapiContent
import com.gu.editorialproductionmetricsmodels.models.OriginatingSystem.{Composer, InCopy}
import com.gu.editorialproductionmetricsmodels.models.{CapiData, KinesisEvent, OriginatingSystem}
import metricsLambdas.Config._
import metricsLambdas.{KinesisWriter, Logging}
import scala.concurrent. Future
import io.circe.syntax._
import io.circe.generic.auto._
import org.joda.time.{DateTime, DateTimeZone}

import scala.concurrent.ExecutionContext.Implicits.global

case class TimePeriod(startDate: DateTime, endDate: DateTime)

object CapiAPILogic extends Logging {

  val client = new GuardianContentClient(capiKey)

  def searchQuery(pageNumber: Option[Int] = None): SearchQuery = {
    val timePeriod = get24HourTimePeriod
    val query = SearchQuery()
      .fromDate(timePeriod.startDate)
      .toDate(timePeriod.endDate)
      .pageSize(200)
      .showFields("creationDate,internalComposerCode,internalOctopusCode")
      .showTags("newspaper-book,tracking")
      .boolParam("show-debug", true)
    pageNumber.fold(query)(query.page(_))
  }

  def get24HourTimePeriod: TimePeriod = {
    val endDate = new DateTime(DateTimeZone.UTC).withTimeAtStartOfDay()
    val startDate = endDate.minusDays(1)
    TimePeriod(startDate, endDate)
  }

  def numberOfPages: Future[Int] = client.getResponse(searchQuery()).map(_.pages)

  def getArticles(acc: Future[Seq[Content]] = Future(Seq.empty), pageNumber: Int = 1, totalPages: Int): Future[Seq[Content]] = {
    if (pageNumber > totalPages) acc else {
      val articleList = client.getResponse(searchQuery(Some(pageNumber))).flatMap(response => {
        log.info(s"Content API response status: ${response.status}.")
        if (response.status == "ok") acc.map(_ ++ response.results) else acc
      })
      getArticles(articleList, pageNumber + 1, totalPages)
    }
  }

  def collectYesterdaysCapiData =
    for {
      pages <- numberOfPages
      articles <- getArticles(totalPages = pages)
      kinesisEvents = transformArticlesToKinesisEvents(articles)
    } yield postArticlesToKinesis(kinesisEvents)

  def transformArticlesToKinesisEvents(articles: Seq[Content]): Seq[KinesisEvent] = {
    log.info(s"Total number of articles: ${articles.length}")
    articles.flatMap(transform)
  }

  def postArticlesToKinesis(events: Seq[KinesisEvent]) = {
    log.info(s"Number of articles processed: ${events.length}")
    events.map(KinesisWriter.write)
  }

  def transform(article: Content): Option[KinesisEvent] = {
    val capiData = for {
      fields <- article.fields
      composerId <- fields.internalComposerCode
      storyBundleId = fields.internalOctopusCode
      creationDate <- fields.creationDate
      newspaperBookTag = getTagByType(article.tags, NewspaperBook)
      commissioningDesk <- getTagByType(article.tags, Tracking)
      debugFields <- article.debug
      startingSystem = getOriginatingSystem(debugFields)
    } yield CapiData(
        composerId = composerId,
        storyBundleId = storyBundleId,
        newspaperBookTag = newspaperBookTag,
        creationDate = creationDate.iso8601,
        commissioningDesk = commissioningDesk,
        originatingSystem = startingSystem)

    capiData.fold[Option[KinesisEvent]]({
      log.info(s"Failed to transform CAPI article to CapiData with ID: ${article.id}")
      None})(data => Some(KinesisEvent(CapiContent, data.asJson)))
  }

  def getTagByType(tags: Seq[Tag], tagType: TagType): Option[String] =
    tags.collectFirst{case tag: Tag if tag.`type` == tagType => tag.id}

  def getOriginatingSystem(debugFields: Debug) =
    debugFields.originatingSystem.fold[OriginatingSystem](Composer)(system => if (system.toLowerCase == "incopy") InCopy else Composer)

  def closeCapiRequestClient = client.shutdown()

}
