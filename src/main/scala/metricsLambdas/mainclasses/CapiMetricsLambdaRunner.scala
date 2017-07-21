package metricsLambdas.mainclasses

import metricsLambdas.CapiMetricsLambda

object CapiMetricsLambdaRunner extends App{

  val capiMetricsLambda = new CapiMetricsLambda()
  capiMetricsLambda.collectMetrics
}
