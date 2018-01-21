package useCases

import domain.{GitHubMember, PullRequest, Repo}
import factories.NotificationMessageFactory
import gateways.{GitHubGateway, Logger, SlackGateway}
import repositories.{MemberRepository, PullRequestFilter}

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

class NotifyRecentlyAssignedUseCase(
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

    Future.sequence(futures).map { listOfLists =>
      notifyReviewers(listOfLists.flatten)
    }
  }

  def notifyReviewers(pullRequests: List[PullRequest]): Future[Any] = {
    pullRequestFilter.filter(pullRequests).map { pullRequest =>
      pullRequest.requested_reviewers.map { reviewer =>
        notifyReviewer(pullRequest, reviewer)
      }.last
    }.last
  }

  private def notifyReviewer(pullRequest: PullRequest, githubReviewer: GitHubMember): Future[Any] = {
    memberRepository.findMember(githubReviewer.login).map { reviewer =>
      memberRepository.findMember(pullRequest.user.login).map { owner => {
        (reviewer, owner) match {
          case (Some(r), Some(o)) => {
            val message = notificationMessageFactory.buildRecentlyAssignedMessage(
              pullRequest = pullRequest,
              owner = owner.get
            )
            slackGateway.postMessage(reviewer.get.slack_name, message)
          }
          case _ => Logger.log(s"unable to resolve ${pullRequest.user.login}, ${pullRequest.user.login}")
        }
      }
      }
    }
  }
}
