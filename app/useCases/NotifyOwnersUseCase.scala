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

  def execute(): Future[List[Repo]] = {
    gitHubGatway.getRepos()
      .andThen {
        case Success(repos: List[Repo]) =>
          processAllPullRequests(repos)
      }
  }

  def processAllPullRequests(repos: List[Repo]): Unit = {
    val futures = for (repo <- repos)
      yield gitHubGatway.getPullRequests(s"${repo.url}/pulls")

    Future.sequence(futures)
      .andThen {
        case Success(lists: List[List[PullRequest]]) =>
          notifyOwners(lists.flatten)
      }
  }

  def notifyOwners(pullRequests: List[PullRequest]): Unit = {
    pullRequestFilter.filter(pullRequests).foreach { pullRequest =>
      notifyOwner(pullRequest)
    }
  }

  private def notifyOwner(pullRequest: PullRequest): Unit = {
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
    }}
  }

}
