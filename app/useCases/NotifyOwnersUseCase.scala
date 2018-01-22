package useCases

import domain.{Member, PullRequest, Repo}
import factories.NotificationMessageFactory
import gateways.{GitHubGateway, Logger, SlackGateway}
import repositories.{MemberRepository, PullRequestFilter}

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

class NotifyOwnersUseCase(
  slackGateway: SlackGateway,
  gitHubGatway: GitHubGateway,
  notificationMessageFactory: NotificationMessageFactory,
  pullRequestFilter: PullRequestFilter,
  memberRepository: MemberRepository
) {

  def execute(): Future[List[Future[List[Future[Option[Member]]]]]] = {
    gitHubGatway.getRepos().map { repos =>
      processAllPullRequests(repos)
    }
  }

  def processAllPullRequests(repos: List[Repo]): List[Future[List[Future[Option[Member]]]]] = {
    repos.map { repo =>
      gitHubGatway.getPullRequests(s"${repo.url}/pulls").map { pullRequest =>
        notifyOwners(pullRequest)
      }
    }
  }

  def notifyOwners(pullRequests: List[PullRequest]): List[Future[Option[Member]]] = {
    pullRequestFilter.filter(pullRequests).map { pullRequest =>
      notifyOwner(pullRequest)
    }
  }

  private def notifyOwner(pullRequest: PullRequest): Future[Option[Member]] = {
    memberRepository.findMember(pullRequest.user.login).flatMap {
      case None =>
        Logger.log(s"unable to resolve ${pullRequest.user.login}")
        Future.successful(None)
      case Some(owner) =>
        val message = notificationMessageFactory.buildOwnerMessage(pullRequest = pullRequest, owner = owner)
        slackGateway.postMessage(owner.slack_name, message)
        Future.successful(Some(owner))
    }
  }


}
