package metricsLambdas

import com.amazonaws.services.cloudwatch.AmazonCloudWatch
import com.amazonaws.services.cloudwatch.model.{Dimension, MetricDatum, PutMetricDataRequest, StandardUnit}
import metricsLambdas.aws.AWSClientFactory


class CapiMetricsLambda extends Logging{

  def run(): Unit = {
    val cloudWatchClient = AWSClientFactory.createCloudWatchClient
    putMetricsData(cloudWatchClient)
    log.info("Running the Capi Metrics Lambda.")
  }

  def putMetricsData(client: AmazonCloudWatch) = {
    val data = new MetricDatum()
      .withMetricName("flexiblePolledSuccessfully")
      .withUnit(StandardUnit.None.toString)
      .withValue(1.0)
      .withDimensions(new Dimension().withName("Stage").withValue("PROD"))

    client.putMetricData(new PutMetricDataRequest().withNamespace("ProductionMetricsLambdaFlexible").withMetricData(data))
  }

}
