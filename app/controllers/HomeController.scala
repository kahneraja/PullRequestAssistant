package controllers

import java.time.temporal.ChronoUnit
import javax.inject._

import domain.GitHub.{PullRequest, PullRequestResponse}
import filters.ClosedPullRequestFilter
import gateways.GitHubGateway
import play.api.cache.AsyncCacheApi
import play.api.libs.json.Json
import play.api.mvc._
import repositories.UserRepository
import scala.concurrent.duration._

import scala.concurrent.{ExecutionContext, Future}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(
  cc: ControllerComponents,
  memberRepository: UserRepository,
  gitHubGateway: GitHubGateway,
  cache: AsyncCacheApi
)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  def index: Action[AnyContent] = Action {
    Ok(views.html.index("PullRequestAssistant"))
  }

  def metrics: Action[AnyContent] = Action.async {
    getPullRequests.map { pullRequests =>
      val responses = ClosedPullRequestFilter.filter(pullRequests).map { pullRequest =>
        val dayOfWeek = pullRequest.created_at.getDayOfWeek.getValue - 1
        val label = pullRequest.created_at.minusDays(dayOfWeek).toLocalDate
        val hours = pullRequest.created_at.until(pullRequest.closed_at.get, ChronoUnit.HOURS).toInt
        new PullRequestResponse(label, hours)
      }
      Ok(Json.toJson(responses))
    }
  }

  private def getPullRequests: Future[List[PullRequest]] = cache
    .getOrElseUpdate[List[PullRequest]]("pullRequests", 24.hours) {
    for {
      repos <- gitHubGateway.getRepos()
      pullRequests <- {
        val futures = repos.map { repo =>
          gitHubGateway.getPullRequests(s"${repo.url}/pulls", "closed")
        }
        Future.sequence(futures)
      }
    } yield pullRequests.flatten
  }

}
