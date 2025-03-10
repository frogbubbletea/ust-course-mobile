package com.frogbubbletea.usthong.network

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.select.Elements
import com.frogbubbletea.usthong.data.Prefix
import com.frogbubbletea.usthong.data.PrefixType
import com.frogbubbletea.usthong.data.Semester
import com.frogbubbletea.usthong.data.samplePrefixes
import com.frogbubbletea.usthong.data.sampleSemesters
import kotlinx.coroutines.*

data class ScrapeResult(
    val scrapedPrefix: Prefix,
    val scrapedSemester: Semester,
    val prefixes: List<Prefix>,
    val semesters: List<Semester>,
)

suspend fun scrapeCourses(
    prefix: Prefix? = null,
    semester: Semester? = null
): ScrapeResult {
    if (prefix != null && semester == null) {
        throw IllegalArgumentException("SemesterCode cannot be null if prefixName is given.")
    }

    // Connect to HKUST Course Quotas website
    var page: Document = if (prefix != null && semester != null) {
        Ksoup.parseGetRequest(url = "https://w5.ab.ust.hk/wcq/cgi-bin/${semester.code}/subject/${prefix.name}")  // Given Semester and prefix
    } else if (semester != null) {
        Ksoup.parseGetRequest(url = "https://w5.ab.ust.hk/wcq/cgi-bin/${semester.code}/")  // Given semester, first prefix
    } else {
        Ksoup.parseGetRequest(url = "https://w5.ab.ust.hk/wcq/cgi-bin/")  // Latest semester and first prefix
    }

    if (page.title() == "404 Not Found" && prefix != null && semester != null) {  // Check if given prefix exists in given semester
        page = Ksoup.parseGetRequest(url = "https://w5.ab.ust.hk/wcq/cgi-bin/${semester.code}/")  // Use first prefix if it doesn't
    } else if (page.title() == "404 Not Found" && semester != null) {  // Check if given semester exists
        page = Ksoup.parseGetRequest(url = "https://w5.ab.ust.hk/wcq/cgi-bin/")  // Use latest semester if it doesn't
    }

    // Get course code prefixes
    val prefixesRaw: Elements = page.select(".depts > #subjectItems > a")
    val prefixes: List<Prefix> = prefixesRaw.map { prefix ->
        Prefix(
            name = prefix.text(),
            type = when(prefix.className()) {
                "ug" -> PrefixType.UG
                "pg" -> PrefixType.PG
                else -> PrefixType.UNDEFINED
            },
        )
    }
    val scrapedPrefix = if (prefix != null && prefix in prefixes) {
        prefix
    } else {
        prefixes[0]
    }

    // Get semesters
    // body > div:nth-child(2) > div > div.term.show > div.dropdown-menu.dropdown-menu-right.show > a:nth-child(1)
    val semestersRaw: Elements = page.select(".search-box > .term > .dropdown-menu > .dropdown-item")
    val semesters: List<Semester> = semestersRaw.map { semester ->
        Semester(
            name = semester.text(),
            code = semester.attr("href").split("/").takeLast(2)[0].toInt()
        )
    }
    val scrapedSemester = if (semester != null && semester in semesters) {
        semester
    } else {
        semesters[0]
    }

    return ScrapeResult(
        scrapedPrefix = scrapedPrefix,
        scrapedSemester = scrapedSemester,
        prefixes = prefixes,
        semesters = semesters
    )
}

//suspend fun scrapeCourses() {
//    delay(1000)
//}