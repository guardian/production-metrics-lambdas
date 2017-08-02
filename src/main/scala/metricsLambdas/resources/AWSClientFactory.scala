package metricsLambdas.resources

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import metricsLambdas.Config._


object AWSClientFactory {

  def createS3Client(region: Regions, credentials: AWSCredentialsProvider) = AmazonS3ClientBuilder.standard().withRegion(region).withCredentials(awsCredentialsProvider).build()

  def createKinesisClient = AmazonKinesisClientBuilder.standard().withCredentials(awsCredentialsProvider).withRegion(region.getName).build()
}
