package actors

import javax.inject.{Inject, Singleton}

import akka.actor.Actor
import factories.NotificationMessageFactory
import gateways.Extensions._
import gateways._
import play.api.Logger
import repositories.UserRepository
import useCases.NotifyOwnersUseCase

import scala.concurrent.ExecutionContext

@Singleton
class NotifyOwnersActor @Inject()()(
  ec: ExecutionContext,
  memberRepository: UserRepository,
  httpClient: HttpClient,
  gatewayConfig: GatewayConfig,
  timeProvider: TimeProvider
) extends Actor {
  override def receive: Receive = {
    case _ =>
      if (timeProvider.est().isDuringOfficeHours) {
        Logger.info("Execute: NotifyOwnersUseCase")
        new NotifyOwnersUseCase(
          slackGateway = new SlackGatewayImpl(httpClient, gatewayConfig),
          gitHubGatway = new GitHubGatewayImpl(httpClient, gatewayConfig, timeProvider),
          notificationMessageFactory = new NotificationMessageFactory(timeProvider),
          userRepository = memberRepository,
          timeProvider = timeProvider
        ).execute()
      } else {
        Logger.info(s"Out of office hours... ${timeProvider.est().toString}")
      }
  }
}
