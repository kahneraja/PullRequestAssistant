package modules

import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport
import actors._

class JobModule extends AbstractModule with AkkaGuiceSupport {
  def configure(): Unit = {
    bindActor[NotifyReviewersActor]("NotifyReviewersActor")
    bindActor[NotifyRecentlyAssignedActor]("NotifyRecentlyAssignedActor")
    bindActor[NotifyOwnersActor]("NotifyOwnersActor")
    bindActor[CollectMetricsActor]("CollectMetricsActor")
    bind(classOf[Scheduler]).asEagerSingleton()
  }
}
