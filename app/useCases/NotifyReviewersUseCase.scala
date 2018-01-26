package useCases

import domain.GitHub.{Member, PullRequest, Repo}
import factories.NotificationMessageFactory
import gateways.{GitHubGateway, Logger, SlackGateway}
import repositories.{MemberRepository, PullRequestFilter}

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

class NotifyReviewersUseCase (
  slackGateway: SlackGateway,
  gitHubGatway: GitHubGateway,
  notificationMessageFactory: NotificationMessageFactory,
  pullRequestFilter: PullRequestFilter,
  memberRepository: MemberRepository
) {

  def execute(): Future[List[Future[List[Future[Option[domain.User]]]]]] = {
    gitHubGatway.getRepos().map { repos =>
      processAllPullRequests(repos)
    }
  }

  def processAllPullRequests(repos: List[Repo]): List[Future[List[Future[Option[domain.User]]]]] = {
    repos.map { repo =>
      gitHubGatway.getPullRequests(s"${repo.url}/pulls").map { pullRequest =>
        notifyReviewers(pullRequest)
      }
    }
  }

  def notifyReviewers(pullRequests: List[PullRequest]): List[Future[Option[domain.User]]] = {
    pullRequestFilter.filter(pullRequests).flatMap { pullRequest =>
      pullRequest.requested_reviewers.map { reviewer =>
        notifyReviewer(pullRequest, reviewer)
      }
    }
  }

  private def notifyReviewer(pullRequest: PullRequest, githubReviewer: Member): Future[Option[domain.User]] = {
    memberRepository.findMember(githubReviewer.login).flatMap {
      case None =>
        Logger.log(s"unable to resolve ${githubReviewer.login}")
        Future.successful(None)
      case Some(reviewer) => {
        memberRepository.findMember(pullRequest.user.login).flatMap {
          case None =>
            Logger.log(s"unable to resolve ${pullRequest.user.login}")
            Future.successful(None)
          case Some(owner) =>
            val message = notificationMessageFactory.buildReviewMessage(pullRequest = pullRequest, owner = owner)
            slackGateway.postMessage(reviewer.slack_name, message)
            Future.successful(Some(reviewer))
        }
      }
    }
  }

}
