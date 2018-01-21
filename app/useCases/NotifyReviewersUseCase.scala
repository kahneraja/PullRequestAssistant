package useCases

import domain.{GitHubMember, PullRequest, Repo}
import factories.NotificationMessageFactory
import gateways.{GitHubGateway, Logger, SlackGateway}
import repositories.{MemberRepository, PullRequestFilter}

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future
import scala.util.Success

class NotifyReviewersUseCase(
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
      notifyReviewers(lists.flatten)
    }
  }

  def notifyReviewers(pullRequests: List[PullRequest]): Future[Any] = {
    pullRequestFilter.filter(pullRequests).map { pullRequest =>
      pullRequest.requested_reviewers.map { reviewer =>
        notifyReviewer(pullRequest, reviewer)
      }.last
    }.last
  }

  private def notifyReviewer(pullRequest: PullRequest, githubReviewer: GitHubMember) = {
    memberRepository.findMember(githubReviewer.login).map { reviewer =>
      memberRepository.findMember(pullRequest.user.login).map { owner =>
        (reviewer, owner) match {
          case (Some(r), Some(o)) =>
            val message = notificationMessageFactory.buildReviewMessage(
              pullRequest = pullRequest,
              owner = o
            )
            slackGateway.postMessage(r.slack_name, message)
          case _ => Logger.log(s"unable to resolve ${githubReviewer.login}, ${pullRequest.user.login}")
        }
      }
    }
  }

}
