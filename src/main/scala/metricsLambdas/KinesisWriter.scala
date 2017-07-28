package metricsLambdas

import java.nio.ByteBuffer
import java.util.UUID

import com.amazonaws.services.kinesis.model.PutRecordRequest
import com.gu.editorialproductionmetricsmodels.models.KinesisEvent
import io.circe.syntax._
import io.circe.generic.auto._
import metricsLambdas.resources.AWSClientFactory
import Config._

object KinesisWriter {

  lazy val client = AWSClientFactory.createKinesisClient

  def write(event: KinesisEvent) = {
    val eventJson = event.asJson
    val eventString = eventJson.toString()
    postToKinesis(eventString)
  }

  private def postToKinesis(event: String) = {
    val streamEvent: ByteBuffer = ByteBuffer.wrap(event.getBytes)
    val partitionKey = UUID.randomUUID().toString
    val request: PutRecordRequest = new PutRecordRequest()
    request.setPartitionKey(partitionKey)
    request.setStreamName(kinesisStreamName)
    request.setData(streamEvent)
    client.putRecord(request)
  }

}
