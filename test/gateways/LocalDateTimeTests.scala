package gateways

import java.time.LocalDateTime

import gateways.Extensions._

class LocalDateTimeTests extends BaseSpec {

  "Midday Saturday" should "be out of office." in {
    val localDateTime = LocalDateTime.of(2018, 1, 6, 12, 0)
    localDateTime.isDuringOfficeHours shouldBe false
  }

  "Midday Sunday" should "be out of office." in {
    val localDateTime = LocalDateTime.of(2018, 1, 7, 12, 0)
    localDateTime.isDuringOfficeHours shouldBe false
  }

  "Monday 9am" should "be in office." in {
    val localDateTime = LocalDateTime.of(2018, 1, 1, 9, 0)
    localDateTime.isDuringOfficeHours shouldBe true
  }

  "Monday 5:59pm" should "be in office." in {
    val localDateTime = LocalDateTime.of(2018, 1, 1, 17, 59)
    localDateTime.isDuringOfficeHours shouldBe true
  }

  "Monday 6pm" should "be out of office." in {
    val localDateTime = LocalDateTime.of(2018, 1, 1, 18, 0)
    localDateTime.isDuringOfficeHours shouldBe false
  }

  "At 12:20pm" should "have 40 minutes to next hour" in {
    val localDateTime = LocalDateTime.of(2018, 1, 1, 0, 20)
    localDateTime.minutesToNextHour shouldBe 40
  }

  "At 8pm" should "have 4 hours to midnight" in {
    val localDateTime = LocalDateTime.of(2018, 1, 1, 20, 0)
    localDateTime.hoursToMidnight shouldBe 4
  }

  "At 2:30am" should "have 22 hours to midnight" in {
    val localDateTime = LocalDateTime.of(2018, 1, 1, 2, 30)
    localDateTime.hoursToMidnight shouldBe 22
  }

}
