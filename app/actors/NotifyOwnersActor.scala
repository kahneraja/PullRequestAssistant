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
  slackGateway: SlackGateway,
  gitHubGateway: GitHubGateway,
  timeProvider: TimeProvider
) extends Actor {
  override def receive: Receive = {
    case _ =>
      if (timeProvider.est().isDuringOfficeHours) {
        Logger.info("Execute: NotifyOwnersUseCase")
        new NotifyOwnersUseCase(
          slackGateway = slackGateway,
          gitHubGatway = gitHubGateway,
          notificationMessageFactory = new NotificationMessageFactory(timeProvider),
          userRepository = memberRepository,
          timeProvider = timeProvider
        ).execute()
      } else {
        Logger.info(s"Out of office hours... ${timeProvider.est().toString}")
      }
  }
}
