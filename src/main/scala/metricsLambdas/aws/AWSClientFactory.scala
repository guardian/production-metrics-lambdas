package metricsLambdas.aws

import com.amazonaws.ClientConfiguration
import com.amazonaws.services.cloudwatch.{AmazonCloudWatch, AmazonCloudWatchClientBuilder}
import metricsLambdas.Config._

object AWSClientFactory {

  def createCloudWatchClient: AmazonCloudWatch =
    AmazonCloudWatchClientBuilder.standard().withRegion(region).withCredentials(awsCredentialsProvider).withClientConfiguration(new ClientConfiguration()).build()

}
