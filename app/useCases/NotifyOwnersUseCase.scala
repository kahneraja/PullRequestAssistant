package useCases

import domain.{PullRequest, Repo}
import factories.NotificationMessageFactory
import gateways.{GitHubGateway, Logger, SlackGateway}
import repositories.{MemberRepository, PullRequestFilter}

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future
import scala.util.Success

class NotifyOwnersUseCase(
  slackGateway: SlackGateway,
  gitHubGatway: GitHubGateway,
  notificationMessageFactory: NotificationMessageFactory,
  pullRequestFilter: PullRequestFilter,
  memberRepository: MemberRepository
) {

  def execute(): Future[Any] = {
    gitHubGatway.getRepos().map { repos =>
      processAllPullRequests(repos)
    }
  }

  def processAllPullRequests(repos: List[Repo]): Future[Any] = {
    val futures = for (repo <- repos)
      yield gitHubGatway.getPullRequests(s"${repo.url}/pulls")

    Future.sequence(futures).map { lists =>
      notifyOwners(lists.flatten)
    }
  }

  def notifyOwners(pullRequests: List[PullRequest]): Future[Any] = {
    pullRequestFilter.filter(pullRequests).map { pullRequest =>
      notifyOwner(pullRequest)
    }.last
  }

  private def notifyOwner(pullRequest: PullRequest): Future[Any] = {
    memberRepository.findMember(pullRequest.user.login).map { owner => {
      owner match {
        case Some(o) =>
          val message = notificationMessageFactory.buildOwnerMessage(
            pullRequest = pullRequest,
            o
          )
          slackGateway.postMessage(o.slack_name, message)
        case _ => Logger.log(s"unable to resolve ${pullRequest.user.login}")
      }
    }
    }
  }

}
