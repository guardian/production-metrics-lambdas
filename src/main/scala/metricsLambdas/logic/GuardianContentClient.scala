package metricsLambdas.logic

import com.gu.contentapi.client.ContentApiClient
import com.gu.contentapi.client.model.HttpResponse

import scala.concurrent.{ExecutionContext, Future}

class GuardianContentClient(override val apiKey: String) extends ContentApiClient {
  override def get(url: String, headers: Map[String, String])(implicit context: ExecutionContext): Future[HttpResponse] = ???

  def shutdown() = ???
}
