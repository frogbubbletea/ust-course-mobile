package com.frogbubbletea.usthong.data

val sampleSemesters: List<String> = listOf("2024-25 Summer", "2024-25 Spring", "2024-25 Winter", "2024-25 Fall")

// val samplePrefixes: List<String> = listOf("ACCT", "AESF", "AIAA", "AISC", "AMAT", "BEHI", "BIBU", "BIEN", "BSBE", "BTEC", "CENG", "CHEM", "CHMS", "CIEM", "CIVL", "CMAA", "COMP", "CPEG", "CSIC", "CSIT", "CTDL", "DASC", "DBAP", "DRAP", "DSAA", "DSCT", "ECON", "EEMT", "EESM", "ELEC", "EMIA", "ENEG", "ENGG", "ENTR", "ENVR", "ENVS", "EOAS", "EVNG", "EVSM", "FINA", "FTEC", "GBUS", "GFIN", "GNED", "HLTH", "HMAW", "HMMA", "HUMA", "IBTM", "IEDA", "IIMP", "INTR", "IOTA", "IPEN", "ISDN", "ISOM", "JEVE", "LABU", "LANG", "LIFS", "MAED", "MAFS", "MARK", "MASS", "MATH", "MCEE", "MECH", "MESF", "MFIT", "MGCS", "MGMT", "MICS", "MILE", "MIMT", "MSBD", "MSDM", "MTLE", "NANO", "OCES", "PDEV", "PHYS", "PPOL", "RMBI", "ROAS", "SBMT", "SCIE", "SEEN", "SHSS", "SMMG", "SOSC", "SUST", "TEMG", "UCOP", "UGOD", "UPOP", "UROP", "UTOP", "WBBA")

val samplePrefixes: List<Prefix> = listOf(
    Prefix(
        name = "ACCT",
        type = PrefixType.UG
    ),
    Prefix(
        name = "COMP",
        type = PrefixType.UG
    ),
    Prefix(
        name = "EEMT",
        type = PrefixType.PG
    )
)

val sampleCourses: List<Course> = listOf(
    Course(
        prefix = "COMP",
        code = "2012",
        title = "Object-Oriented Programming and Data Structures",
        units = 4,
        matching = MatchingRequirement.LAB,
        attributes = mapOf(
            "4Y" to "Students admitted before 2022",
            "CC22" to "Students admitted from 2022",
            "READ" to "Reading Material"
        ),
        info = mapOf(
            "Pre-requisite" to "COMP 2011",
            "Exclusion" to "COMP 2012H",
            "Description" to "To learn the fundamental concepts and techniques behind object-oriented programming. They include: abstract data types; creation, initialization, and destruction of objects; class hierarchies; polymorphism, inheritance and dynamic binding; generic programming using templates. To learn the object-oriented view of data structures: linked lists, stacks, queues, binary trees, and algorithms such as searching and hashing."
        ),
        sections = listOf(
            Section(
                code = "L1",
                classNbr = 2060,
                schedules = listOf(
                    SectionSchedule(
                        effectivePeriod = "MoWe 09:00AM - 10:20AM",
                        dateTimes = listOf("MoWe 09:00AM - 10:20AM"),
                        venue = listOf("Rm 2464, Lift 25-26 (122)"),
                        instructors = listOf("PAPADOPOULOS, Dimitris"),
                        teachingAssistants = listOf()
                    )
                ),
                totalQuota = Quota(
                    quota = 99,
                    enrol = 69,
                    avail = 30,
                    wait = 1
                ),
                reservedQuotas = listOf(),
                remarks = mapOf()
            ),
            Section(
                code = "L2",
                classNbr = 2062,
                schedules = listOf(
                    SectionSchedule(
                        effectivePeriod = "MoWe 10:30AM - 11:50AM",
                        dateTimes = listOf("MoWe 10:30AM - 11:50AM"),
                        venue = listOf("Rm 2464, Lift 25-26 (122)"),
                        instructors = listOf("SANDER, Pedro"),
                        teachingAssistants = listOf()
                    )
                ),
                totalQuota = Quota(
                    quota = 85,
                    enrol = 78,
                    avail = 7,
                    wait = 1
                ),
                reservedQuotas = listOf(),
                remarks = mapOf()
            ),
            Section(
                code = "L3",
                classNbr = 2064,
                schedules = listOf(
                    SectionSchedule(
                        effectivePeriod = "TuTh 04:30PM - 05:50PM",
                        dateTimes = listOf("TuTh 04:30PM - 05:50PM"),
                        venue = listOf("Rm 2407, Lift 17-18 (126)"),
                        instructors = listOf("TSOI, Yau Chat"),
                        teachingAssistants = listOf()
                    )
                ),
                totalQuota = Quota(
                    quota = 92,
                    enrol = 92,
                    avail = 0,
                    wait = 13
                ),
                reservedQuotas = listOf(),
                remarks = mapOf()
            ),
            Section(
                code = "L4",
                classNbr = 2066,
                schedules = listOf(
                    SectionSchedule(
                        effectivePeriod = "WeFr 04:30PM - 05:50PM",
                        dateTimes = listOf("WeFr 04:30PM - 05:50PM"),
                        venue = listOf("Rm 2502, Lift 25-26 (120)"),
                        instructors = listOf("CHAN, Ki Cecia"),
                        teachingAssistants = listOf()
                    )
                ),
                totalQuota = Quota(
                    quota = 70,
                    enrol = 66,
                    avail = 4,
                    wait = 0
                ),
                reservedQuotas = listOf(),
                remarks = mapOf()
            ),
            Section(
                code = "LA1",
                classNbr = 2068,
                schedules = listOf(
                    SectionSchedule(
                        effectivePeriod = "Fr 10:30AM - 12:20PM",
                        dateTimes = listOf("Fr 10:30AM - 12:20PM"),
                        venue = listOf("Rm 2502, Lift 25-26 (120)"),
                        instructors = listOf("PAPADOPOULOS, Dimitris"),
                        teachingAssistants = listOf(
                            "CHUNG, Tsz Wa",
                            "KHURANA, Vinayak",
                            "LI, Chenyue",
                            "LIU, Shuoling",
                            "MENG, Yihao",
                            "WANG, Jindu",
                            "WANG, Xuezhen",
                            "WANG, Yucheng",
                            "YIN, Zixin",
                            "ZENG, Yunfan",
                            "ZHANG, Yizhen",
                            "ZHUNIS, Assem"
                        )
                    )
                ),
                totalQuota = Quota(
                    quota = 99,
                    enrol = 69,
                    avail = 30,
                    wait = 1
                ),
                reservedQuotas = listOf(),
                remarks = mapOf()
            ),
            Section(
                code = "LA2",
                classNbr = 2070,
                schedules = listOf(
                    SectionSchedule(
                        effectivePeriod = "Mo 03:00PM - 04:50PM",
                        dateTimes = listOf("Mo 03:00PM - 04:50PM"),
                        venue = listOf("Rm 2465, Lift 25-26 (122)"),
                        instructors = listOf("SANDER, Pedro"),
                        teachingAssistants = listOf(
                            "CHUNG, Tsz Wa",
                            "KHURANA, Vinayak",
                            "LI, Chenyue",
                            "LIU, Shuoling",
                            "MENG, Yihao",
                            "WANG, Jindu",
                            "WANG, Xuezhen",
                            "WANG, Yucheng",
                            "YIN, Zixin",
                            "ZENG, Yunfan",
                            "ZHANG, Yizhen",
                            "ZHUNIS, Assem"
                        )
                    )
                ),
                totalQuota = Quota(
                    quota = 85,
                    enrol = 78,
                    avail = 7,
                    wait = 1
                ),
                reservedQuotas = listOf(),
                remarks = mapOf()
            ),
            Section(
                code = "LA3",
                classNbr = 2071,
                schedules = listOf(
                    SectionSchedule(
                        effectivePeriod = "Mo 05:30PM - 07:20PM",
                        dateTimes = listOf("Mo 05:30PM - 07:20PM"),
                        venue = listOf("Rm 4620, Lift 31-32 (126)"),
                        instructors = listOf("TSOI, Yau Chat"),
                        teachingAssistants = listOf(
                            "CHUNG, Tsz Wa",
                            "KHURANA, Vinayak",
                            "LI, Chenyue",
                            "LIU, Shuoling",
                            "MENG, Yihao",
                            "WANG, Jindu",
                            "WANG, Xuezhen",
                            "WANG, Yucheng",
                            "YIN, Zixin",
                            "ZENG, Yunfan",
                            "ZHANG, Yizhen",
                            "ZHUNIS, Assem"
                        )
                    )
                ),
                totalQuota = Quota(
                    quota = 92,
                    enrol = 92,
                    avail = 0,
                    wait = 13
                ),
                reservedQuotas = listOf(),
                remarks = mapOf()
            ),
            Section(
                code = "LA4",
                classNbr = 2072,
                schedules = listOf(
                    SectionSchedule(
                        effectivePeriod = "Fr 12:30PM - 02:20PM",
                        dateTimes = listOf("Fr 12:30PM - 02:20PM"),
                        venue = listOf("Lecture Theater F (133)"),
                        instructors = listOf("CHAN, Ki Cecia"),
                        teachingAssistants = listOf(
                            "CHUNG, Tsz Wa",
                            "KHURANA, Vinayak",
                            "LI, Chenyue",
                            "LIU, Shuoling",
                            "MENG, Yihao",
                            "WANG, Jindu",
                            "WANG, Xuezhen",
                            "WANG, Yucheng",
                            "YIN, Zixin",
                            "ZENG, Yunfan",
                            "ZHANG, Yizhen",
                            "ZHUNIS, Assem"
                        )
                    )
                ),
                totalQuota = Quota(
                    quota = 70,
                    enrol = 66,
                    avail = 4,
                    wait = 0
                ),
                reservedQuotas = listOf(),
                remarks = mapOf()
            ),
        )
    ),
    Course(
        prefix = "ACCT",
        code = "5210",
        title = "Managerial Accounting Foundations",
        units = 2,
        matching = MatchingRequirement.NONE,
        attributes = mapOf(
            "DELI" to "Mode of Delivery"
        ),
        info = mapOf(
            "Attributes" to "[BLD] Blended learning",
            "Vector" to "[2-0-0:2]",
            "Exclusion" to "IMBA 5060",
            "Description" to "Use of accounting data in decision-making, financial planning, control, and performance evaluation within organizations.",
            "Intended learning outcomes" to "On successful completion of the course, students will be able to:\n" +
                    "1.\tInterpret and explain cost concepts and related terms.\n" +
                    "2.\tInterpret cost-volume-profit analysis.\n" +
                    "3.\tEstimate the cost and price of a product.\n" +
                    "4.\tApply core managerial accounting information to strategic decision making.\n" +
                    "5.\tIdentify the internal budgeting process.\n" +
                    "6.\tConstruct cost management tools such as Activity-Based Costing.\n" +
                    "7.\tOrganize managerial accounting information to conduct performance evaluations.\n" +
                    "8.\tIdentify managerial accounting information as a competitive tool."
        ),
        sections = listOf(
            Section(
                code = "L1",
                classNbr = 1015,
                schedules = listOf(
                    SectionSchedule(
                        effectivePeriod = "16-FEB-2025 - 16-FEB-2025",
                        dateTimes = listOf("Su 09:00AM - 12:20PM"),
                        venue = listOf("Rm 2001, LSK Bldg"),
                        instructors = listOf("SHIEH, Tony"),
                        teachingAssistants = listOf("CHEUNG, Mandy")
                    ),
                    SectionSchedule(
                        effectivePeriod = "16-FEB-2025 - 16-FEB-2025",
                        dateTimes = listOf("Su 02:00PM - 05:20PM"),
                        venue = listOf("Rm 2001, LSK Bldg"),
                        instructors = listOf("SHIEH, Tony"),
                        teachingAssistants = listOf()
                    ),
                    SectionSchedule(
                        effectivePeriod = "02-MAR-2025 - 02-MAR-2025",
                        dateTimes = listOf("Su 09:00AM - 12:20PM"),
                        venue = listOf("Rm 2001, LSK Bldg"),
                        instructors = listOf("SHIEH, Tony"),
                        teachingAssistants = listOf()
                    ),
                    SectionSchedule(
                        effectivePeriod = "02-MAR-2025 - 02-MAR-2025",
                        dateTimes = listOf("Su 02:00PM - 05:20PM"),
                        venue = listOf("Rm 2001, LSK Bldg"),
                        instructors = listOf("SHIEH, Tony"),
                        teachingAssistants = listOf()
                    ),
                    SectionSchedule(
                        effectivePeriod = "16-MAR-2025 - 16-MAR-2025",
                        dateTimes = listOf("Su 09:00AM - 12:20PM"),
                        venue = listOf("Rm 2001, LSK Bldg"),
                        instructors = listOf("SHIEH, Tony"),
                        teachingAssistants = listOf()
                    ),
                    SectionSchedule(
                        effectivePeriod = "16-MAR-2025 - 16-MAR-2025",
                        dateTimes = listOf("Su 02:00PM - 05:20PM"),
                        venue = listOf("Rm 2001, LSK Bldg"),
                        instructors = listOf("SHIEH, Tony"),
                        teachingAssistants = listOf()
                    ),
                    SectionSchedule(
                        effectivePeriod = "30-MAR-2025 - 30-MAR-2025",
                        dateTimes = listOf("Su 09:00AM - 12:20PM"),
                        venue = listOf("Rm 2001, LSK Bldg"),
                        instructors = listOf("SHIEH, Tony"),
                        teachingAssistants = listOf()
                    ),
                    SectionSchedule(
                        effectivePeriod = "30-MAR-2025 - 30-MAR-2025",
                        dateTimes = listOf("Su 02:00PM - 05:20PM"),
                        venue = listOf("Rm 2001, LSK Bldg"),
                        instructors = listOf("SHIEH, Tony"),
                        teachingAssistants = listOf()
                    ),
                ),
                totalQuota = Quota(
                    quota = 30,
                    enrol = 16,
                    avail = 14,
                    wait = 0
                ),
                reservedQuotas = listOf(
                    ReservedQuota(
                        dept = "MBA",
                        quota = 15,
                        enrol = 13,
                        avail = 2,
                    ),
                    ReservedQuota(
                        dept = "MBA Bi-weekly",
                        quota = 13,
                        enrol = 1,
                        avail = 12,
                    ),
                    ReservedQuota(
                        dept = "Digital MBA",
                        quota = 2,
                        enrol = 2,
                        avail = 0,
                    ),
                ),
                remarks = mapOf(
                    "Mode" to "The class is delivered in a conventional face-to-face mode",
                    "Program" to "For FT-MBA; PT-MBA; MBA (Bi-weekly); Digital MBA and MBA Exchange students only",
                    "Deadline" to "Add/Drop Deadline : 17-Feb-2025"
                )
            )
        )
    )
)