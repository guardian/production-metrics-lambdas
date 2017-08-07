# Production Metrics Lambdas

Runs once every 24 hours to collect metrics and sends them to the production metrics kinesis stream. Shared models are defined in the [editorial-production-metrics-lib](https://github.com/guardian/editorial-production-metrics-lib).

[Cloudformation Template](https://github.com/guardian/editorial-tools-platform/blob/master/cloudformation/editorial-production-metrics/EditorialProductionMetricsLambdas.yml)

## Running Locally

Composer credentials are needed to run the lambda. Get these from [janus](https://janus.gutools.co.uk).
There are [main classes](src/main/scala/metricsLambdas/mainclasses/) to run the lambda locally.

The lambda will post data to the PROD kinesis stream even when running locally. 
