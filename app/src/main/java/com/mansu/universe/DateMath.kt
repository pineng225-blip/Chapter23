package com.mansu.universe
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit.*

data class Span(val years: Long, val months: Long, val days: Long, val hours: Long, val minutes: Long, val seconds: Long)

/** Calendar-based elapsed time (handles leap years and month lengths via java.time). Never negative. */
fun span(from: LocalDateTime, to: LocalDateTime): Span {
    val end = if (to.isBefore(from)) from else to
    val y = YEARS.between(from, end); val a = from.plusYears(y)
    val m = MONTHS.between(a, end); val b = a.plusMonths(m)
    val d = DAYS.between(b, end); val c = b.plusDays(d)
    return Span(y, m, d, HOURS.between(c, end), MINUTES.between(c, end) % 60, SECONDS.between(c, end) % 60)
}
fun isBirthday(now: LocalDateTime) = now.month == Config.BIRTH.month && now.dayOfMonth == Config.BIRTH.dayOfMonth
fun nextBirthday(now: LocalDateTime): LocalDateTime {
    var n = Config.BIRTH.withYear(now.year)
    if (!n.isAfter(now)) n = n.plusYears(1)
    return n
}
