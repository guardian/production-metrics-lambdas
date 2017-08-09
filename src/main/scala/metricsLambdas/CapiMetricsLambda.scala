package metricsLambdas

import java.util.{Map => JMap}

import com.amazonaws.services.lambda.runtime.Context
import metricsLambdas.logic.CapiAPILogic

class CapiMetricsLambda extends Logging{

  def run(event: JMap[String, Object], context: Context): Unit = {
    val functionName = context.getFunctionName
    log.info(s"This is the ${functionName}")
    collectMetrics
  }

  def collectMetrics = {
    log.info(s"The stage is ${Config.stage}")
//    CapiAPILogic.collectYesterdaysCapiData
    log.info("Running the Capi Metrics Lambda.")
  }

  def shutdown = CapiAPILogic.closeCapiRequestClient
}
