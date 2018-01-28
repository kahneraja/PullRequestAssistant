package useCases

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

import domain.GitHub.{Metric, PullRequest}
import gateways.GitHubGateway
import repositories.MetricsRepository

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

class CollectMetricsUseCase(
  gitHubGateway: GitHubGateway,
  metricsRepository: MetricsRepository
) {
  def execute(): Future[Unit] = {
    metricsRepository.drop
    getPullRequests.map { repos =>
      repos.map { repo =>
        repo.map { pullRequests =>
          pullRequests.map { pullRequests =>
            pullRequests.map { pullRequestWithfiles =>
              val metric = new Metric(
                title = pullRequestWithfiles.title,
                url = pullRequestWithfiles.html_url,
                created = startOfWeek(pullRequestWithfiles.created_at),
                closed = startOfWeek(pullRequestWithfiles.closed_at.get),
                hours = pullRequestWithfiles.created_at.until(pullRequestWithfiles.closed_at.get, ChronoUnit.HOURS).toInt,
                changes = getChanges(pullRequestWithfiles)
              )
              metricsRepository.insert(metric)
            }
          }
        }
      }
    }
  }

  private def getPullRequests = {
    gitHubGateway.getRepos().map { repos =>
      repos.map { repo =>
        Thread.sleep(SLEEP_MILLI_SECONDS)
        gitHubGateway.getPullRequests(s"${repo.url}/pulls", "closed").map { pullRequests =>
          pullRequests.map { pullRequest =>
            Thread.sleep(SLEEP_MILLI_SECONDS)
            gitHubGateway.getFiles(s"${pullRequest.url}/files").map { files =>
              pullRequest.copy(files = Some(files))
            }
          }
        }
      }
    }
  }

  val SLEEP_MILLI_SECONDS: Int = 500

  private def startOfWeek(localDateTime: LocalDateTime) = {
    val dayOfWeek = localDateTime.getDayOfWeek.getValue - 1
    localDateTime.minusDays(dayOfWeek).toLocalDate
  }

  def getChanges(pullRequest: PullRequest): Int = {
    pullRequest.files.map { files =>
      files.map(_.changes).sum
    }.getOrElse(0)
  }
}
