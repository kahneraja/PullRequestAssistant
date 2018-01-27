package useCases

import domain.GitHub.{Member, PullRequest}
import domain.User
import factories.NotificationMessageFactory
import filters.IdlePullRequestFilter
import gateways._
import repositories.UserRepository

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

class NotifyReviewersUseCase(
  slackGateway: SlackGateway,
  gitHubGateway: GitHubGateway,
  notificationMessageFactory: NotificationMessageFactory,
  userRepository: UserRepository,
  timeProvider: TimeProvider
) {

  def execute(): Future[List[Future[List[Future[List[Future[Option[User]]]]]]]] = {
    gitHubGateway.getRepos().map { repos =>
      repos.map { repo =>
        gitHubGateway.getPullRequests(s"${repo.url}/pulls").map { pullRequests =>
          IdlePullRequestFilter.filter(pullRequests, timeProvider).flatMap { pullRequest =>
            notifyRequestedReviewers(pullRequest)
            notifyTeams(pullRequest)
          }
        }
      }
    }
  }

  private def notifyTeams(pullRequest: PullRequest) = {
    pullRequest.requested_teams.map { team =>
      gitHubGateway.getTeamMembers(team.url).map { members =>
        members.map { member =>
          notifyMember(pullRequest, member)
        }
      }
    }

  }

  private def notifyRequestedReviewers(pullRequest: PullRequest) = {
    pullRequest.requested_reviewers.map { reviewer =>
      notifyMember(pullRequest, reviewer)
    }
  }

  private def notifyMember(pullRequest: PullRequest, githubReviewer: Member): Future[Option[domain.User]] = {
    userRepository.findUser(githubReviewer.login).flatMap {
      case None =>
        Logger.log(s"unable to resolve ${
          githubReviewer.login
        }")
        Future.successful(None)
      case Some(reviewer) => {
        userRepository.findUser(pullRequest.user.login).flatMap {
          case None =>
            Logger.log(s"unable to resolve ${
              pullRequest.user.login
            }")
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
