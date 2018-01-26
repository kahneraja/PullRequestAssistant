package useCases

import domain.GitHub.{Event, Member, PullRequest, Team}
import factories.NotificationMessageFactory
import filters.RecentReviewRequestEventFilter
import gateways.{GitHubGateway, Logger, SlackGateway, TimeProvider}
import repositories.UserRepository

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

class NotifyRecentlyAssignedUseCase(
  slackGateway: SlackGateway,
  gitHubGateway: GitHubGateway,
  notificationMessageFactory: NotificationMessageFactory,
  userRepository: UserRepository,
  timeProvider: TimeProvider
) {

  def execute(): Future[Any] = {
    gitHubGateway.getRepos().map { repos =>
      repos.map { repo =>
        gitHubGateway.getPullRequests(s"${repo.url}/pulls").map { pullRequests =>
          pullRequests.map { pullRequest =>
            gitHubGateway.getEvents(pullRequest._links.issue.href).map { events =>
              RecentReviewRequestEventFilter.filter(events, timeProvider).map { event =>
                (event.requested_reviewer, event.requested_team) match {
                  case (Some(reviewer),_) => notify(reviewer, event, pullRequest)
                  case (_,Some(team)) => notify(team, event, pullRequest)
                  case _ =>
                    Logger.log(s"no reviewers provided")
                    Future.successful(None)
                }
              }
            }
          }
        }
      }
    }
  }

  private def notify(team: Team, event: Event, pullRequest: PullRequest): Future[Any] = {
    gitHubGateway.getTeamMembers(team.url).map { members =>
      members.map { member =>
        notify(member, event, pullRequest)
      }
    }
  }

  private def notify(member: Member, event: Event, pullRequest: PullRequest): Future[Any] = {
    userRepository.findUser(member.login).flatMap {
      case None =>
        Logger.log(s"unable to resolve ${event.review_requester.get.login}")
        Future.successful(None)
      case Some(reviewer) =>
        userRepository.findUser(reviewer.github_name).flatMap {
          case None =>
            Logger.log(s"unable to resolve ${event.review_requester.get.login}")
            Future.successful(None)
          case Some(owner) =>
            val message = notificationMessageFactory.buildRecentlyAssignedMessage(event, pullRequest, owner)
            slackGateway.postMessage(reviewer.slack_name, message)
            Future.successful(Some(reviewer))
        }
    }
  }


}
