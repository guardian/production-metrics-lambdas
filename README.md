# Production Metrics Lambdas

Runs once every 24 hours to collect metrics and sends them to the production metrics kinesis stream. Shared models are defined in the [editorial-production-metrics-lib](https://github.com/guardian/editorial-production-metrics-lib).

[Cloudformation Template](https://github.com/guardian/editorial-tools-platform/blob/master/cloudformation/editorial-production-metrics/EditorialProductionMetricsLambdas.yml)

## Running Locally

Composer credentials are needed to run the lambda. Get these from [janus](https://janus.gutools.co.uk).
There are [main classes](src/main/scala/metricsLambdas/mainclasses/) to run the lambda locally.

```bash
sbt run
```

By default the lambda posts to the DEV kinesis stream when running locally. This can be changed by altering the [Config.scala](./src/main/scala/metricsLambdas/Config.scala) file. The data always comes from CAPI PROD as collecting data is a read only operation. The easiest way to read off the DEV stream is to run [Editorial Production Metrics](https://github.com/guardian/editorial-production-metrics) locally with the DEV configuration. 
