package metricsLambdas

import java.util.{Map => JMap}

import com.amazonaws.services.lambda.runtime.Context
import metricsLambdas.logic.CapiAPILogic
import scala.concurrent.duration._
import scala.concurrent.Await

class CapiMetricsLambda extends Logging{

  def run(event: JMap[String, Object], context: Context): Unit = {
    collectMetrics
  }

  def collectMetrics = {
    Await.ready(CapiAPILogic.collectYesterdaysCapiData, 300 seconds)
    log.info("Running the Capi Metrics Lambda.")
  }

  def shutdown = CapiAPILogic.closeCapiRequestClient
}
