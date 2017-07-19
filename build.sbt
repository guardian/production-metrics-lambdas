name := "production-metrics-lambdas"
organization  := "com.gu"

version := "1.0"

scalaVersion in ThisBuild := "2.11.11"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
  "com.amazonaws" % "aws-java-sdk-lambda" % "1.11.163",
  "com.amazonaws" % "aws-java-sdk-cloudwatch" % "1.11.163",
  "com.amazonaws" % "aws-java-sdk-config" %  "1.11.163"
)

enablePlugins(JavaAppPackaging, RiffRaffArtifact)

topLevelDirectory in Universal := None
packageName in Universal := normalizedName.value

riffRaffPackageType := (packageBin in Universal).value
riffRaffUploadArtifactBucket := Option("riffraff-artifact")
riffRaffUploadManifestBucket := Option("riffraff-builds")
riffRaffManifestProjectName :=  s"editorial-tools:${name.value}"
riffRaffBuildIdentifier :=  Option(System.getenv("BUILD_NUMBER")).getOrElse("DEV")