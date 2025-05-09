package com.frogbubbletea.ustcoursemobile.network

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.select.Elements
import com.frogbubbletea.ustcoursemobile.data.Course
import com.frogbubbletea.ustcoursemobile.data.MatchingRequirement
import com.frogbubbletea.ustcoursemobile.data.Prefix
import com.frogbubbletea.ustcoursemobile.data.PrefixType
import com.frogbubbletea.ustcoursemobile.data.Quota
import com.frogbubbletea.ustcoursemobile.data.ReservedQuota
import com.frogbubbletea.ustcoursemobile.data.Section
import com.frogbubbletea.ustcoursemobile.data.SectionSchedule
import com.frogbubbletea.ustcoursemobile.data.Semester
import java.util.Locale

data class ScrapeResult(
    val scrapedPrefix: Prefix,
    val scrapedSemester: Semester,
    val prefixes: List<Prefix>,
    val semesters: List<Semester>,
    val courses: List<Course>
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

    // Get courses of page
    val coursesRaw: Elements = page.select("#classes > .course")
    val courses = coursesRaw.map { course ->
        // Extract course code
        val fullCode: String? = course.select(".courseanchor > a").first()?.attr("name")
        val coursePrefix: String = fullCode?.substring(0, 4) ?: ""  // Course.prefix
        val suffixCode: String = fullCode?.substring(4) ?: ""  // Course.code

        // Extract title and units
        val titleAndUnits: String = course
            .select(".courseinfo > .courseattrContainer > .subject")
            .first()
            ?.text()
            ?.split(
                "-",
                limit = 2
            )
            ?.last()
            ?.trim() ?: ""
        val unitsRegex = Regex("\\(\\d units*\\)")
        val unitsText: String? = unitsRegex.find(titleAndUnits)?.value
        val units: Int = unitsText?.substring(1, 2)?.toInt() ?: 0  // Course.units
        val title: String = unitsRegex.replace(titleAndUnits, "").trim()  // Course.title

        // Extract matching requirements if any
        val matchingRaw: String? = course.select(".courseinfo > .courseattrContainer > .matching").first()?.text()
        val matching: MatchingRequirement = when (matchingRaw) {  // Course.matching
            "[Matching between Lecture & Tutorial required]" -> MatchingRequirement.TUTORIAL
            "[Matching between Lecture & Lab required]" -> MatchingRequirement.LAB
            else -> MatchingRequirement.NONE
        }

        // Extract attributes
        // #classes > div:nth-child(1) > div.courseinfo > div.courseattrContainer > div:nth-child(2)
        val attrsRaw: Elements = course.select(".courseinfo > .courseattrContainer > .popup.attrword")
        val attributes = mutableMapOf<String, String>()  // Course.attributes
        attrsRaw.forEach { attr ->
            val attrTitleRegex = Regex("\\[|\\]")
            val attrTitle: String = attrTitleRegex.replace(
                input = attr.selectFirst(".crseattrword")?.text() ?: "",
                replacement = ""
            )
            val attrContent: String = attr.selectFirst(".popupdetail")?.text() ?: ""
            attributes[attrTitle] = attrContent
        }

        // Extract course info
        val infoTable: Element? = course.selectFirst(".courseinfo > .courseattr > .popupdetail > table")
        val infoRows: Elements? = infoTable?.select("tbody > tr")
        val info = mutableMapOf<String, String>()  // Course.info
        infoRows?.forEach { infoRow ->
            // Convert title to title case, do not add course info entries without a title
            val infoTitle: String? = infoRow.selectFirst("th")?.text()?.lowercase()?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            val infoContentRaw: Element? = infoRow.selectFirst("td")

            // Check if info row contains elements for Intended Learning Outcomes
            val iloSpan: String? = infoContentRaw?.selectFirst("span")?.text()
            val iloTableRows: Elements? = infoContentRaw?.selectFirst("table")?.select("tbody > tr")

            if (infoTitle != null) {
                if (infoContentRaw?.children()
                        .isNullOrEmpty() || iloSpan == null || iloTableRows.isNullOrEmpty()
                ) {
                    info[infoTitle] = infoContentRaw?.wholeText() ?: ""
                } else {
                    var iloText: String = "$iloSpan"
                    iloTableRows.forEach { iloTableRow ->
                        iloText += "\n${iloTableRow.text()}"
                    }
                    info[infoTitle] = iloText
                }
            }
        }

        // Extract sections
        val sectionsTable: Element? = course.selectFirst(".sections > tbody")
        val sectionsRows: Elements? = sectionsTable?.select(".newsect.secteven, .newsect.sectodd, .secteven, .sectodd")
        // TODO: continue extracting sections
        val sections = mutableListOf<Section>()  // Course.sections
        sectionsRows?.forEach { sectionRow ->
            val sectionColumns: Elements = sectionRow.select("td")

            if (sectionRow.attr("class").contains("newsect")) {  // Add new section
                // Get section code and class number
                val sectionCodeClassNbr: String = sectionColumns[0].text()
                val sectionCodeRegex = Regex("\\(\\d+\\)")
                val classNbrRegex = Regex("\\(|\\)")  // Remove surrounding parentheses
                val sectionCode: String = sectionCodeRegex.replace(sectionCodeClassNbr, "").trim()  // Course.sections[x].code
                val classNbrRaw: String = sectionCodeRegex.find(sectionCodeClassNbr)?.value ?: ""
                val classNbr: Int = classNbrRegex.replace(classNbrRaw, "").trim().toInt()  // Course.sections[x].classNbr

                // Get schedule
                val dateAndTimeRaw: String = sectionColumns[1].html()
                val dateAndTimeSplit: List<String> = dateAndTimeRaw.split("<br>")
                val effectivePeriod: String = dateAndTimeSplit[0].trim()  // Course.sections[x].schedules[y].effectivePeriod
                val dateTimes: String = dateAndTimeSplit.last().trim()  // Course.sections[x].schedules[y].dateTimes
                val venue: String = sectionColumns[2].text()  // Course.sections[x].schedules[y].venue

                // Get instructors
                val instructorsRaw: Elements = sectionColumns[3].select(".instructorList > a")
                // Course.sections[x].schedule[y].instructors
                val instructors: List<String> = if (instructorsRaw.size > 0) {
                    instructorsRaw.map { instructor ->
                        instructor.text()
                    }
                } else {  // If instructor is TBA
                    sectionColumns[3].select(".instructorList").map { instructor ->
                        instructor.text()
                    }
                }

                // Get TAs
                val tasRaw: Elements = sectionColumns[4].select(".taListContainer > .taList > a")
                val tas: List<String> = tasRaw.map { ta ->  // Course.sections[x].schedule[y].teachingAssistants
                    ta.text()
                }

                // Put schedule, instructors, TAs into SectionSchedule instance
                val schedule = SectionSchedule(
                    effectivePeriod = effectivePeriod,
                    dateTimes = dateTimes,
                    venue = venue,
                    instructors = instructors,
                    teachingAssistants = tas
                )

                // Get total quotas
                // Course.sections[x].totalQuota.quota
                val totalQuotaQuota: Int = if (sectionColumns[5].attr("class").contains("quota")) {  // If reserved quota is present
                    sectionColumns[5].selectFirst("span")?.text()?.toInt() ?: 0
                } else {  // If reserved quota is not present
                    sectionColumns[5].text().toInt()
                }
                val totalQuotaEnrol: Int = sectionColumns[6].text().toInt()  // Course.sections[x].totalQuota.enrol
                val totalQuotaAvail: Int = sectionColumns[7].text().toInt()  // Course.sections[x].totalQuota.avail
                val totalQuotaWait: Int = sectionColumns[8].text().toInt()  // Course.sections[x].totalQuota.wait
                val totalQuota = Quota(  // Course.sections[x].totalQuota
                    quota = totalQuotaQuota,
                    enrol = totalQuotaEnrol,
                    avail = totalQuotaAvail,
                    wait = totalQuotaWait,
                )

                // Get reserved quotas
                val reservedQuotas: List<ReservedQuota> = if (sectionColumns[5].attr("class").contains("quota")) {  // Course.sections[x].reservedQuotas
                    val reservedQuotasRaw = sectionColumns[5].select("div > .quotadetail > div").toList().drop(1)
                    reservedQuotasRaw.map { reservedQuota ->
                        val reservedQuotaDept: String = reservedQuota.text().split(":")[0].trim()  // Course.sections[x].reservedQuotas[z].dept
                        val reservedQuotaQEA: List<String> = reservedQuota.text().split(":").last().trim().split("/")
                        val reservedQuotaQuota: Int = reservedQuotaQEA[0].toInt()  // Course.sections[x].reservedQuotas[z].quota
                        val reservedQuotaEnrol: Int = reservedQuotaQEA[1].toInt()  // Course.sections[x].reservedQuotas[z].enrol
                        val reservedQuotaAvail: Int = reservedQuotaQEA[2].toInt()  // Course.sections[x].reservedQuotas[z].avail
                        ReservedQuota(  // Course.sections[x].reservedQuotas[z]
                            dept = reservedQuotaDept,
                            quota = reservedQuotaQuota,
                            enrol = reservedQuotaEnrol,
                            avail = reservedQuotaAvail
                        )
                    }
                } else {
                    listOf()
                }

                // Get remarks
                val remarks = mutableMapOf<String, String>()  // Course.sections[x].remarks
                val classNotes: List<String> = sectionColumns[9].selectFirst("div > .popup.classnotes > .popupdetail")?.html()?.split("<br>") ?: listOf()
                val consents: List<String> = sectionColumns[9].selectFirst("div > .popup.consent > .popupdetail")?.html()?.split("<br>") ?: listOf()
                classNotes.forEachIndexed { idx, classNote ->
                    remarks["Note ${idx + 1}"] = classNote.replace("&gt;", "").trim()  // Remove > symbol leading each remark
                }
                consents.forEachIndexed { idx, consent ->
                    remarks["Consent"] = consent.replace("&gt;", "").trim()
                }

                sections += Section(
                    code = sectionCode,
                    classNbr = classNbr,
                    schedules = listOf(schedule),
                    totalQuota = totalQuota,
                    reservedQuotas = reservedQuotas,
                    remarks = remarks
                )
            } else {  // Add additional schedules in section
                // Get schedule
                val dateAndTimeRaw: String = sectionColumns[1].html()
                val dateAndTimeSplit: List<String> = dateAndTimeRaw.split("<br>")
                val effectivePeriod: String = dateAndTimeSplit[0].trim()  // Course.sections[x].schedules[y].effectivePeriod
                val dateTimes: String = dateAndTimeSplit.last().trim()  // Course.sections[x].schedules[y].dateTimes
                val venue: String = sectionColumns[2].text()  // Course.sections[x].schedules[y].venue

                // Get instructors
                val instructorsRaw: Elements = sectionColumns[3].select(".instructorList > a")
                val instructors: List<String> = instructorsRaw.map { instructor ->  // Course.sections[x].schedule[y].instructors
                    instructor.text()
                }

                // Get TAs
                val tasRaw: Elements = sectionColumns[4].select(".taListContainer > .taList > a")
                val tas: List<String> = tasRaw.map { ta ->  // Course.sections[x].schedule[y].teachingAssistants
                    ta.text()
                }

                // Put schedule, instructors, TAs into SectionSchedule instance
                val schedule = SectionSchedule(
                    effectivePeriod = effectivePeriod,
                    dateTimes = dateTimes,
                    venue = venue,
                    instructors = instructors,
                    teachingAssistants = tas
                )

                // Add the additional schedule into section
                val newSection = Section(
                    code = sections.last().code,
                    classNbr = sections.last().classNbr,
                    schedules = sections.last().schedules + schedule,
                    totalQuota = sections.last().totalQuota,
                    reservedQuotas = sections.last().reservedQuotas,
                    remarks = sections.last().remarks,
                )
                sections[sections.lastIndex] = newSection
            }
        }

        Course(
            prefix = scrapedPrefix,
            code = suffixCode,
            semester = scrapedSemester,
            title = title,
            units = units,
            matching = matching,
            attributes = attributes.toMap(),
            info = info.toMap(),
            sections = sections.toList()
        )
    }

    return ScrapeResult(
        scrapedPrefix = scrapedPrefix,
        scrapedSemester = scrapedSemester,
        prefixes = prefixes,
        semesters = semesters,
        courses = courses
    )
}

//suspend fun scrapeCourses() {
//    delay(1000)
//}