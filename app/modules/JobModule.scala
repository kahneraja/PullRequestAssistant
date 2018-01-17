package modules

import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport
import scheduler.{NotifyOwnersActor, NotifyRecentlyAssignedActor, NotifyReviewersActor, Scheduler}

class JobModule extends AbstractModule with AkkaGuiceSupport {
  def configure(): Unit = {
    bindActor[NotifyReviewersActor]("NotifyReviewersActor")
    bindActor[NotifyRecentlyAssignedActor]("NotifyRecentlyAssignedActor")
    bindActor[NotifyOwnersActor]("NotifyOwnersActor")
    bind(classOf[Scheduler]).asEagerSingleton()
  }
}
