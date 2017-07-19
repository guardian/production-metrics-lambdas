package metricsLambdas

import com.amazonaws.auth.{AWSCredentialsProviderChain, EnvironmentVariableCredentialsProvider, InstanceProfileCredentialsProvider}
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.regions.Regions

object Config {

  val region = Option(System.getenv("AWS_DEFAULT_REGION")).map(Regions.fromName).get

  val awsCredentialsProvider = new AWSCredentialsProviderChain(
    new EnvironmentVariableCredentialsProvider(),
    new ProfileCredentialsProvider("composer"),
    new InstanceProfileCredentialsProvider(false)
  )

}
