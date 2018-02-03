package actors

import javax.inject.{Inject, Singleton}

import akka.actor.Actor
import gateways._
import play.api.Logger
import repositories.MetricRepository
import useCases.CollectMetricsUseCase

import scala.concurrent.ExecutionContext

@Singleton
class CollectMetricsActor @Inject()()(
  ec: ExecutionContext,
  gitHubGateway: GitHubGateway,
  metricRepository: MetricRepository,
  gatewayConfig: GatewayConfig
) extends Actor {
  override def receive: Receive = {
    case _ =>
      Logger.info("Execute: CollectMetricsUseCase")
      new CollectMetricsUseCase(
        gitHubGateway = gitHubGateway,
        metricRepository = metricRepository
      ).execute()
  }
}
