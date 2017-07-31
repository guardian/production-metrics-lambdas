package metricsLambdas

import java.util.{Map => JMap}

import com.amazonaws.services.cloudwatch.AmazonCloudWatch
import com.amazonaws.services.cloudwatch.model.{Dimension, MetricDatum, PutMetricDataRequest, StandardUnit}
import com.amazonaws.services.lambda.runtime.Context
import metricsLambdas.logic.CapiAPILogic
import metricsLambdas.resources.AWSClientFactory

class CapiMetricsLambda extends Logging{

  def run(event: JMap[String, Object], context: Context): Unit = {
    collectMetrics
  }

  def collectMetrics = {
    val cloudWatchClient = AWSClientFactory.createCloudWatchClient
    putMetricsData(cloudWatchClient)
    log.info("Running the Capi Metrics Lambda.")
    CapiAPILogic.collectYesterdaysCapiData
  }

  def putMetricsData(client: AmazonCloudWatch) = {
    val data = new MetricDatum()
      .withMetricName("flexiblePolledSuccessfully")
      .withUnit(StandardUnit.None.toString)
      .withValue(1.0)
      .withDimensions(new Dimension().withName("Stage").withValue("PROD"))

    client.putMetricData(new PutMetricDataRequest().withNamespace("ProductionMetricsLambdaFlexible").withMetricData(data))
  }

  def shutdown = CapiAPILogic.closeCapiRequestClient
}
