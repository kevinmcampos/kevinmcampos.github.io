package br.app.kevin.portfolio

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Asserts the pure `mailto:` builder — the machine-verifiable core of the Contact screen.
 * Exercises URL-encoding via `encodeURIComponent` (available in the browser test runtime).
 */
class ContactInfoTest {

    @Test
    fun mailto_withNoArguments_isBarePlainMailto() {
        assertEquals("mailto:${ContactInfo.EMAIL}", ContactInfo.mailto())
    }

    @Test
    fun mailto_alwaysStartsWithMailtoScheme() {
        assertTrue(ContactInfo.mailto(subject = "Hi").startsWith("mailto:${ContactInfo.EMAIL}"))
    }

    @Test
    fun mailto_encodesAndJoinsSubjectAndBody() {
        val url = ContactInfo.mailto(subject = "Hello world", body = "a & b")
        // spaces -> %20, ampersand in the value -> %26, params joined by a literal &
        assertTrue(url.contains("subject=Hello%20world"), url)
        assertTrue(url.contains("body=a%20%26%20b"), url)
        assertTrue(url.contains("subject=Hello%20world&body="), url)
    }

    @Test
    fun mailto_omitsBlankSubjectAndBody() {
        val url = ContactInfo.mailto(subject = "   ", body = "")
        assertEquals("mailto:${ContactInfo.EMAIL}", url)
        assertFalse(url.contains("?"), url)
    }

    @Test
    fun mailto_withOnlyBody_startsTheQueryWithAQuestionMark() {
        val url = ContactInfo.mailto(body = "just body")
        assertTrue(url.startsWith("mailto:${ContactInfo.EMAIL}?body="), url)
        assertFalse(url.contains("subject="), url)
    }

    @Test
    fun publicLinks_areHttpsAndEmailLooksValid() {
        assertTrue(ContactInfo.GITHUB.startsWith("https://"), ContactInfo.GITHUB)
        assertTrue(ContactInfo.LINKEDIN.startsWith("https://"), ContactInfo.LINKEDIN)
        assertTrue(ContactInfo.EMAIL.contains("@"), ContactInfo.EMAIL)
    }
}
