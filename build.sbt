name := "production-metrics-lambdas"
organization  := "com.gu"

version := "1.0"

scalaVersion in ThisBuild := "2.11.11"

libraryDependencies ++= Seq(
  "com.amazonaws"     % "aws-lambda-java-core"              % "1.1.0",
  "com.amazonaws"     % "aws-java-sdk-lambda"               % "1.11.163",
  "com.amazonaws"     % "aws-java-sdk-cloudwatch"           % "1.11.163",
  "com.amazonaws"     % "aws-java-sdk-s3"                   % "1.11.163",
  "com.amazonaws"     % "aws-java-sdk-config"               % "1.11.163",
  "com.amazonaws"     % "aws-lambda-java-log4j2"             % "1.5.0",
  "org.slf4j"         % "slf4j-log4j12"                     % "1.7.32",
  "com.typesafe.play" %% "play-ws"                          % "2.5.16",
  "io.circe"          %% "circe-parser"                     % "0.7.0",
  "io.circe"          %% "circe-generic"                    % "0.7.0",
  "com.beachape"      %% "enumeratum-circe"                 % "1.5.14",
  "com.amazonaws"     % "amazon-kinesis-client"             % "1.7.6",
  "com.gu"            %% "content-api-client"               % "11.48",
  "com.gu"            %% "editorial-production-metrics-lib" % "0.17"
)

enablePlugins(JavaAppPackaging, RiffRaffArtifact)

topLevelDirectory in Universal := None
packageName in Universal := normalizedName.value

riffRaffPackageType := (packageBin in Universal).value
riffRaffUploadArtifactBucket := Option("riffraff-artifact")
riffRaffUploadManifestBucket := Option("riffraff-builds")
riffRaffManifestProjectName :=  s"editorial-tools:${name.value}"
riffRaffBuildIdentifier :=  Option(System.getenv("BUILD_NUMBER")).getOrElse("DEV")
