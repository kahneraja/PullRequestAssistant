package actors

import javax.inject.{Inject, Singleton}

import akka.actor.Actor
import factories.NotificationMessageFactory
import gateways.Extensions._
import gateways._
import play.api.Logger
import repositories.UserRepository
import useCases.NotifyReviewersUseCase

import scala.concurrent.ExecutionContext

@Singleton
class NotifyReviewersActor @Inject()()(
  ec: ExecutionContext,
  userRepository: UserRepository,
  httpClient: HttpClient,
  gatewayConfig: GatewayConfig
) extends Actor {
  override def receive: Receive = {
    case _ =>
      if (TimeProviderImpl.est().isDuringOfficeHours) {
        Logger.info("Execute: NotifyReviewersUseCase")
        new NotifyReviewersUseCase(
          slackGateway = new SlackGatewayImpl(httpClient, gatewayConfig),
          gitHubGatway = new GitHubGatewayImpl(httpClient, gatewayConfig, TimeProviderImpl),
          notificationMessageFactory = new NotificationMessageFactory(TimeProviderImpl),
          userRepository = userRepository,
          timeProvider = TimeProviderImpl
        ).execute()
      } else {
        Logger.info(s"Out of office hours... ${TimeProviderImpl.est().toString}")
      }
  }
}
