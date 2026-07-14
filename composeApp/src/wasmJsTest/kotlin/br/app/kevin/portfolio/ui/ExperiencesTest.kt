package br.app.kevin.portfolio.ui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Data invariants for the portfolio's experience list. This is the pure core extracted from
 * `ExperienceScreen.kt` — no renderer needed, so the content itself becomes machine-verifiable:
 * every project must be presentable (title, role, period, description, technologies, screenshots)
 * and any link must look like a real URL.
 */
class ExperiencesTest {

    @Test
    fun experiences_listIsNotEmpty() {
        assertTrue(experiences.isNotEmpty(), "portfolio must list at least one project")
    }

    @Test
    fun experiences_haveUniqueIds() {
        val ids = experiences.map { it.id }
        assertEquals(ids.size, ids.toSet().size, "project ids must be unique: $ids")
    }

    @Test
    fun everyProject_hasNonBlankCoreFields() {
        experiences.forEach { p ->
            assertTrue(p.id.isNotBlank(), "blank id")
            assertTrue(p.title.isNotBlank(), "blank title for id=${p.id}")
            assertTrue(p.role.isNotBlank(), "blank role for id=${p.id}")
            assertTrue(p.period.isNotBlank(), "blank period for id=${p.id}")
            assertTrue(p.description.isNotBlank(), "blank description for id=${p.id}")
        }
    }

    @Test
    fun everyProject_hasAtLeastOneNonBlankTechnology() {
        experiences.forEach { p ->
            assertTrue(p.technologies.isNotEmpty(), "no technologies for id=${p.id}")
            assertTrue(p.technologies.all { it.isNotBlank() }, "blank technology in id=${p.id}")
        }
    }

    @Test
    fun everyProject_hasAtLeastOneNonBlankScreenshot() {
        experiences.forEach { p ->
            assertTrue(p.screenshots.isNotEmpty(), "no screenshots for id=${p.id}")
            assertTrue(p.screenshots.all { it.label.isNotBlank() }, "blank screenshot label in id=${p.id}")
        }
    }

    @Test
    fun everyProjectLink_isEitherNullOrLooksLikeAUrl() {
        experiences.forEach { p ->
            val link = p.link ?: return@forEach
            assertTrue(link.isNotBlank(), "blank link for id=${p.id}")
            assertTrue(
                link.startsWith("http://") || link.startsWith("https://"),
                "link for id=${p.id} does not look like a URL: $link",
            )
        }
    }
}
