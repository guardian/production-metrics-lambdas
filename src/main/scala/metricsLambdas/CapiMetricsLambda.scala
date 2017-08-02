package metricsLambdas

import java.util.{Map => JMap}

import com.amazonaws.services.lambda.runtime.Context
import metricsLambdas.logic.CapiAPILogic

class CapiMetricsLambda extends Logging{

  def run(event: JMap[String, Object], context: Context): Unit = {
    collectMetrics
  }

  def collectMetrics = {
    CapiAPILogic.collectYesterdaysCapiData
    log.info("Running the Capi Metrics Lambda.")
  }

  def shutdown = CapiAPILogic.closeCapiRequestClient
}
