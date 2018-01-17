package scheduler

import javax.inject.{Inject, Singleton}

import akka.actor.Actor
import factories.NotificationMessageFactory
import gateways.Extensions._
import gateways._
import play.api.Logger
import repositories.{IdlePullRequestFilterImpl, MemberRepository}
import useCases.{NotifyRecentlyAssignedUseCase, NotifyReviewersUseCase}

import scala.concurrent.ExecutionContext

@Singleton
class NotifyRecentlyAssignedActor @Inject()()(
  ec: ExecutionContext,
  memberRepository: MemberRepository,
  httpClient: HttpClient,
  gatewayConfig: GatewayConfig
) extends Actor {
  override def receive: Receive = {
    case _ =>
      if (TimeProviderImpl.est().isDuringOfficeHours) {
        Logger.info("Execute: NotifyRecentlyAssignedUseCase")
        new NotifyRecentlyAssignedUseCase(
          slackGateway = new SlackGatewayImpl(httpClient, gatewayConfig),
          gitHubGatway = new GitHubGatewayImpl(httpClient, gatewayConfig, TimeProviderImpl),
          notificationMessageFactory = new NotificationMessageFactory(TimeProviderImpl),
          pullRequestFilter = new IdlePullRequestFilterImpl(TimeProviderImpl),
          memberRepository = memberRepository
        ).execute()
      } else {
        Logger.info(s"Out of office hours... ${TimeProviderImpl.est().toString}")
      }
  }
}
