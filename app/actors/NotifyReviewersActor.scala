package actors

import javax.inject.{Inject, Singleton}

import akka.actor.Actor
import factories.NotificationMessageFactory
import gateways.Extensions._
import gateways._
import play.api.Logger
import repositories.ContributorRepository
import useCases.NotifyReviewersUseCase

import scala.concurrent.ExecutionContext

@Singleton
class NotifyReviewersActor @Inject()()(
  ec: ExecutionContext,
  contributorRepository: ContributorRepository,
  httpClient: HttpClient,
  gatewayConfig: GatewayConfig,
  timeProvider: TimeProvider,
  slackGateway: SlackGateway,
  gitHubGateway: GitHubGateway
) extends Actor {
  override def receive: Receive = {
    case _ =>
      if (timeProvider.est().isDuringOfficeHours) {
        Logger.info("Execute: NotifyReviewersUseCase")
        new NotifyReviewersUseCase(
          slackGateway = slackGateway,
          gitHubGateway = gitHubGateway,
          notificationMessageFactory = new NotificationMessageFactory(timeProvider),
          contributorRepository = contributorRepository,
          timeProvider = timeProvider
        ).execute()
      } else {
        Logger.info(s"Out of office hours... ${timeProvider.est().toString}")
      }
  }
}
