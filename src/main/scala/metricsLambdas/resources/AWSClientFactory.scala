package metricsLambdas.resources

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import metricsLambdas.Config._


object AWSClientFactory {

  def createCloudWatchClient = AmazonCloudWatchClientBuilder.standard().withRegion(region).withCredentials(awsCredentialsProvider).withClientConfiguration(new ClientConfiguration()).build()

  def createS3Client(region: Regions, credentials: AWSCredentialsProvider) = AmazonS3ClientBuilder.standard().withRegion(region).withCredentials(awsCredentialsProvider).build()

}
