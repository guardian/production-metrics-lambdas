package metricsLambdas

import java.util.Properties

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.auth.{AWSCredentialsProviderChain, DefaultAWSCredentialsProviderChain, EnvironmentVariableCredentialsProvider, InstanceProfileCredentialsProvider}
import com.amazonaws.regions.Regions
import metricsLambdas.resources.AWSClientFactory._

import scala.util.Try

object Config {

  private lazy val config = loadConfig

  val region: Regions = Option(System.getenv("AWS_DEFAULT_REGION")).map(Regions.fromName).getOrElse(Regions.EU_WEST_1)

  val stage: String = Option(System.getenv("Stage")).getOrElse("DEV").toUpperCase

  val awsCredentialsProvider = new AWSCredentialsProviderChain(
    new EnvironmentVariableCredentialsProvider(),
    new ProfileCredentialsProvider("composer"),
    new InstanceProfileCredentialsProvider(false),
    new DefaultAWSCredentialsProviderChain
  )

  val capiUrl: String = getConfig("capi.live.internal.url")
  val capiKey: String = getConfig("capi.key")

  val kinesisStreamName: String = getConfig("kinesis.publishingMetricsStream")

  private def loadConfig = {
    val s3Client = createS3Client(region, awsCredentialsProvider)
    val configPath = s"production-metrics-lambdas/$stage/config.properties"
    val configInputStream = s3Client.getObject("guconf-flexible", configPath)
    val context = configInputStream.getObjectContent
    val properties: Properties = new Properties()
    Try(properties.load(context)) orElse sys.error("Could not load config file from s3. This lambda will not run.")
    properties
  }

  private def getConfig(property: String) = Option(config.getProperty(property)) getOrElse sys.error(s"'$property' property missing.")

}
