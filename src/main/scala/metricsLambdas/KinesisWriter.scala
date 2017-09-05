package metricsLambdas

import java.nio.ByteBuffer
import java.util.UUID

import com.amazonaws.services.kinesis.AmazonKinesis
import com.amazonaws.services.kinesis.model.PutRecordRequest
import com.gu.editorialproductionmetricsmodels.models.KinesisEvent
import io.circe.generic.auto._
import io.circe.syntax._
import metricsLambdas.Config._
import metricsLambdas.resources.AWSClientFactory

object KinesisWriter extends Logging {

  private lazy val client: AmazonKinesis = AWSClientFactory.createKinesisClient

  def write(event: KinesisEvent) = {
    val eventJson = event.asJson
    val eventString = eventJson.toString()
    postToKinesis(eventString, kinesisStreamName)
  }

  private def postToKinesis(event: String, kinesisStreamName: String) = {
    val streamEvent: ByteBuffer = ByteBuffer.wrap(event.getBytes)
    val partitionKey = UUID.randomUUID().toString
    val request: PutRecordRequest = new PutRecordRequest()
    request.setPartitionKey(partitionKey)
    request.setStreamName(kinesisStreamName)
    request.setData(streamEvent)
    try {
      client.putRecord(request)
    } catch {
      case e: Throwable => log.error(s"Could not post to kinesis stream $kinesisStreamName ${e.getMessage} ${e.getStackTrace}")
    }
  }

}
