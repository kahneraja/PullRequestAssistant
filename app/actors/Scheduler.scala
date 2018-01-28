package actors

import java.time.LocalDateTime
import javax.inject.{Inject, Named}

import gateways.Extensions._
import akka.actor.{ActorRef, ActorSystem}
import gateways.Logger

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class Scheduler @Inject()(
  val system: ActorSystem,
  @Named("NotifyReviewersActor") val notifyReviewersActor: ActorRef,
  @Named("NotifyRecentlyAssignedActor") val notifyRecentlyAssignedActor: ActorRef,
  @Named("NotifyOwnersActor") val notifyOwnersActor: ActorRef,
  @Named("CollectMetricsActor") val collectMetricsActor: ActorRef
)(implicit ec: ExecutionContext) {
  val now = LocalDateTime.now()

  Logger.log("Schedule Actors")

  system.scheduler.schedule(now.hoursToMidnight.hours, 1.days, collectMetricsActor, None)

  system.scheduler.schedule(now.minutesToNextHour.minutes, 1.hours, notifyReviewersActor, None)
  system.scheduler.schedule((now.minutesToNextHour + 20).minutes, 1.hours, notifyRecentlyAssignedActor, None)
  system.scheduler.schedule((now.minutesToNextHour + 40).minutes, 1.hours, notifyOwnersActor, None)
}
