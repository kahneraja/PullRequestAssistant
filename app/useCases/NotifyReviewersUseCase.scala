package useCases

import domain.GitHub.{Member, PullRequest}
import factories.NotificationMessageFactory
import filters.IdlePullRequestFilter
import gateways._
import repositories.UserRepository

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

class NotifyReviewersUseCase (
  slackGateway: SlackGateway,
  gitHubGatway: GitHubGateway,
  notificationMessageFactory: NotificationMessageFactory,
  userRepository: UserRepository,
  timeProvider: TimeProvider
) {

  def execute(): Future[List[Future[List[Future[Option[domain.User]]]]]] = {
    gitHubGatway.getRepos().map { repos =>
      repos.map { repo =>
        gitHubGatway.getPullRequests(s"${repo.url}/pulls").map { pullRequests =>
          IdlePullRequestFilter.filter(pullRequests, timeProvider).flatMap { pullRequest =>
            pullRequest.requested_reviewers.map { reviewer =>
              notify(pullRequest, reviewer)
            }
          }
        }
      }
    }
  }

  private def notify(pullRequest: PullRequest, githubReviewer: Member): Future[Option[domain.User]] = {
    userRepository.findUser(githubReviewer.login).flatMap {
      case None =>
        Logger.log(s"unable to resolve ${githubReviewer.login}")
        Future.successful(None)
      case Some(reviewer) => {
        userRepository.findUser(pullRequest.user.login).flatMap {
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
