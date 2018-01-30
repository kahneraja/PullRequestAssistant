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
            pullRequests.map { pullRequestWithFiles =>
              pullRequestWithFiles.map { pullRequestWithFilesAndComments =>
                val metric = new Metric(
                  title = pullRequestWithFilesAndComments.title,
                  url = pullRequestWithFilesAndComments.html_url,
                  created = startOfWeek(pullRequestWithFilesAndComments.created_at),
                  closed = startOfWeek(pullRequestWithFilesAndComments.closed_at.get),
                  hours = pullRequestWithFilesAndComments.created_at.until(pullRequestWithFilesAndComments.closed_at.get, ChronoUnit.HOURS).toInt,
                  changes = getChanges(pullRequestWithFilesAndComments),
                  comments = pullRequestWithFilesAndComments.comments.getOrElse(List.empty).size
                )
                metricsRepository.insert(metric)
              }
            }
          }
        }
      }
    }
  }

  private def getPullRequests = gitHubGateway.getRepos().map { repos =>
    repos.map { repo =>
      Thread.sleep(SLEEP_MILLI_SECONDS)
      gitHubGateway.getPullRequests(s"${repo.url}/pulls", "closed").map { pullRequests =>
        pullRequests.map { pullRequest =>
          Thread.sleep(SLEEP_MILLI_SECONDS)
          gitHubGateway.getFiles(s"${pullRequest.url}/files").map { files =>
            Thread.sleep(SLEEP_MILLI_SECONDS)
            gitHubGateway.getComments(pullRequest._links.comments.href).map { comments =>
              pullRequest.copy(files = Some(files), comments = Some(comments))
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
