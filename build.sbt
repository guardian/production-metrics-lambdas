name := "production-metrics-lambdas"
organization  := "com.gu"

version := "1.0"

ThisBuild / scalaVersion := "2.13.10"

lazy val awsVersion = "1.12.401"

libraryDependencies ++= Seq(
  "com.amazonaws"     %  "aws-lambda-java-core"             % "1.2.2",
  "com.amazonaws"     %  "aws-java-sdk-lambda"              % awsVersion,
  "com.amazonaws"     %  "aws-java-sdk-cloudwatch"          % awsVersion,
  "com.amazonaws"     %  "aws-java-sdk-s3"                  % awsVersion,
  "com.amazonaws"     %  "aws-java-sdk-config"              % awsVersion,
  "org.slf4j"         %  "slf4j-simple"                     % "2.0.5",
  "com.beachape"      %% "enumeratum-circe"                 % "1.7.2",
  "com.amazonaws"     %  "amazon-kinesis-client"            % "1.14.9",
  "com.gu"            %% "content-api-client"               % "19.2.0",
  "com.gu"            %% "editorial-production-metrics-lib" % "0.20-SNAPSHOT"
)

enablePlugins(JavaAppPackaging, RiffRaffArtifact)

Universal / topLevelDirectory := None
Universal / packageName := normalizedName.value

riffRaffPackageType := (Universal / packageBin).value
riffRaffManifestProjectName :=  s"editorial-tools:${name.value}"
