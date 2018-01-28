package actors

import javax.inject.{Inject, Singleton}

import akka.actor.Actor
import gateways._
import play.api.Logger
import repositories.MetricsRepository
import useCases.CollectMetricsUseCase

import scala.concurrent.ExecutionContext

@Singleton
class CollectMetricsActor @Inject()()(
  ec: ExecutionContext,
  gitHubGateway: GitHubGateway,
  metricsRepository: MetricsRepository,
  gatewayConfig: GatewayConfig
) extends Actor {
  override def receive: Receive = {
    case _ =>
      Logger.info("Execute: CollectMetricsUseCase")
      new CollectMetricsUseCase(
        gitHubGateway = gitHubGateway,
        metricsRepository = metricsRepository
      ).execute()
  }
}
