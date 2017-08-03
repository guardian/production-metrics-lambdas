# Production Metrics Lambdas

Runs once every 24 hours to collect metrics and sends them to the production metrics kinesis stream. 

[Cloudformation Template](https://github.com/guardian/editorial-tools-platform/blob/master/cloudformation/editorial-production-metrics/EditorialProductionMetricsLambdas.yml)

## Running Locally

Composer credentials are needed to run the lambda. Get these from [janus](https://janus.gutools.co.uk).
There are [main classes](src/main/scala/metricsLambdas/mainclasses/) to run the lambda locally.
