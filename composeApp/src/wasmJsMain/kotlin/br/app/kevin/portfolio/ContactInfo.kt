package br.app.kevin.portfolio

/**
 * My links, in one place.
 *
 * TODO: drop a real cv.pdf in resources/ so the Download CV button works (career-ops can build one).
 */
object ContactInfo {
    const val NAME = "Kevin Campos"
    const val ROLE = "Senior Mobile Engineer"
    const val LOCATION = "Porto, Portugal"
    const val EMAIL = "kevinmcampos@gmail.com"
    const val GITHUB = "https://github.com/kevinmcampos"
    const val LINKEDIN = "https://www.linkedin.com/in/kevinmcampos"
    const val CV_URL = "cv.pdf"

    fun mailto(subject: String = "", body: String = ""): String {
        val query = buildList {
            if (subject.isNotBlank()) add("subject=" + encodeUriComponent(subject))
            if (body.isNotBlank()) add("body=" + encodeUriComponent(body))
        }.joinToString("&")
        return "mailto:$EMAIL" + if (query.isEmpty()) "" else "?$query"
    }
}

/** url-encode a mailto param. has to be top-level for wasm's js(). */
private fun encodeUriComponent(value: String): String = js("encodeURIComponent(value)")
