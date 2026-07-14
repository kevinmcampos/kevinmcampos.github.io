# Agent Readiness — kevin-portfolio-wasm-kmp

> Audit against the 5 autonomy pillars. Autonomy is limited by the feedback loop,
> not intelligence: an agent can only work alone where it can (a) verify its own
> work and (b) run without causing damage. This file is the hardening plan.

- **Assessed commit:** `5806615b5406bff72fa4f804eeed78d6d77851ad` ("Work in progress")
- **Branch:** `work-in-progress` (in sync with `origin/work-in-progress`: 0 ahead / 0 behind)
- **Working tree:** DIRTY — 4 staged new files, 14 modified, 1 deleted, plus untracked
  `PhoneFrame.kt`, `ScreenshotCarousel.kt`, and the whole `ui/theme/` dir. **No clean revert point.**
- **Date:** 2026-07-14
- **Stack:** Kotlin Multiplatform / Compose Multiplatform, single `wasmJs` target
  (Kotlin 2.2.0, Compose MP 1.9.0-rc01). All source lives in `wasmJsMain` — there is
  no `commonMain` or `commonTest` despite the template README implying otherwise.
- **Current Definition of Done in the repo:** **NONE.** No `CLAUDE.md`, no `AGENTS.md`.
  `README.md` is the unedited JetBrains KMP template (references `commonMain`, `iosMain`,
  `jvmMain` folders that do not exist).

## Scorecard — 5 / 10

| Pillar | Score | Evidence |
|--------|:---:|----------|
| **1. Oracle** (can it verify itself?) | **1** | One *real* gate: `./gradlew compileKotlinWasmJs` compiles the current dirty tree — BUILD SUCCESSFUL in 39s. But **zero tests exist**: `./gradlew check` is a **false green** — it passes in 1m1s with `compileTestKotlinWasmJs NO-SOURCE`, `wasmJsTest NO-SOURCE`, `allTests NO-SOURCE`, `wasmJsBrowserTest SKIPPED`. No CI mirror of any verification gate (see below). No lint/format gate at all. |
| **2. Safe solo** (can it run without damage?) | **2** | Build/run is hermetic and offline: no paid keys, no live services, no DB, no money. Only external dependency is the Gradle/Maven/npm(yarn) fetch. The single side-effect zone is **`git push origin main` → GitHub Pages auto-deploy** (`.github/workflows/deploy.yml`), which requires an explicit push to `main` and never fires from local verification or from the `work-in-progress` branch. |
| **3. Verifiable done** (is "done" machine-checkable?) | **1** | ~1,600 LOC, 16 files, 12 carry `@Composable`. "Done" for this repo is overwhelmingly **visual taste** (phone frame, screenshot carousel, avatar hover, status bar, theme). A thin *pure* core exists but is unasserted: `ContactInfo.mailto()` (pure string builder), `PortfolioDestination` enum, and the `ProjectItem` / `experiences` data list in `ExperienceScreen.kt`. Mixed: testable core + taste shell, no oracle over either. |
| **4. Safety net** (clean baseline + hooks?) | **1** | `.git` exists and is synced to remote, but the **working tree is dirty with no baseline commit** — an agent has no clean point to revert to. **No git hooks** (`.git/hooks/` holds only samples), so no gate runs on commit. |
| **5. Self-contained** (spec + DoD in-repo?) | **0** | No `CLAUDE.md`/`AGENTS.md`. No DoD anywhere. Spec for the active WIP (new theme, phone frame, carousel, footer) lives outside the repo. `README.md` is stale template boilerplate. An agent has nothing in-repo telling it what "done" means or which command proves it. |

**Verdict:** **5–7 band → an agent may work only the verifiable trail.** Today that trail
is nearly empty (compile-only). Close the loop first; do not point an agent at UI work
until a gate and a DoD exist.

## Verifiable trail vs fenced zones

**Verifiable trail (agent may work here).** The machine-checkable surface is small but
real: `ContactInfo.mailto()` (URL-encoding, subject/body assembly), the
`PortfolioDestination` enum (labels/icons), and the `ProjectItem`/`experiences` data in
`ExperienceScreen.kt` (every project should have a non-blank title, ≥1 technology, ≥1
screenshot, and valid-looking URLs). Plus the compile gate itself, which already guards
every file. This is where tests can assert "done" without a human eye. It does not exist
as a separate module yet — Task 3 carves it out.

**Fenced zones (no machine certifies these — keep a human in the loop).** Everything
`@Composable`: layout, spacing, the fake-Android phone frame, the screenshot carousel,
avatar hover animation, `AppTheme` colors/typography, `WavingEmoji`. These are taste
islands — an agent can *build* them but cannot *verify* them; sign-off is visual. The one
**side-effect zone** is deployment: pushing to `main` publishes the live site via GitHub
Actions. No secrets are committed (`local.properties` is untracked and gitignored; the
only `token`-ish match in tracked files is `id-token: write` in the workflow — a GitHub
OIDC permission, not a credential). `ContactInfo` holds public contact details by design,
not secrets.

## Tasks — ordered by leverage (loop-closing first)

### Task 1 — Add `CLAUDE.md` with an explicit Definition of Done  *(cheapest pillar-5 point)*
- **Goal:** Give an agent an in-repo, copy-pasteable DoD and the repo's footguns.
- **Files:** create `/CLAUDE.md` (repo root).
- **Content must state, at minimum:**
  - **Done =** `./gradlew compileKotlinWasmJs` passes **and** `./gradlew check` passes
    (once Task 2 makes `check` meaningful). Copy the literal commands.
  - **Footguns:** all source is under `composeApp/src/wasmJsMain/` — there is **no**
    `commonMain`/`commonTest`; tests must go in `composeApp/src/wasmJsTest/kotlin/`.
    `check` is currently a false green (no tests) — do not trust it until Task 2/3 land.
    Active work happens on `work-in-progress`; **CI only runs on `main`** so local
    verification is the only signal on this branch.
  - **Where the spec lives:** point to the current source of truth for WIP scope.
  - **Publish is a side effect:** never `git push origin main` without explicit ask —
    it auto-deploys to GitHub Pages.
- **Verify:** `test -f CLAUDE.md && grep -q compileKotlinWasmJs CLAUDE.md`
- **Accept:** file exists and names the literal gate command(s). → Pillar 5: 0 → 1.

### Task 2 — Make CI mirror local verification, and run it on every branch/PR
- **Goal:** Green-local ⇒ green-CI, on the branch actually being worked on.
- **Files:** `.github/workflows/deploy.yml` (or a new `ci.yml` for verification).
- **Change:** the deploy job runs only `wasmJsBrowserDistribution` (a build, not a check)
  and triggers only on `main`. Add a verification step that runs the **same** command an
  agent runs locally — `./gradlew check --no-daemon` (which includes the Wasm compile and,
  after Task 3, the tests) — and trigger it on `push` to any branch and on all PRs, not
  just `main`. Keep deploy gated to `main`.
- **Verify:** `grep -q "gradlew check" .github/workflows/*.yml` and push the branch, then
  `gh run list --branch work-in-progress` shows the check job running.
- **Accept:** CI executes the identical gate string used locally, on the working branch. → Pillar 1 toward 2.

### Task 3 — Extract the pure core and add the first tests (kills the false green)
- **Goal:** Turn `check` from a no-op into a real oracle; separate assertable logic from UI.
- **Files:**
  - New `composeApp/src/wasmJsTest/kotlin/br/app/kevin/portfolio/` test dir (this is the
    correct test source set — **not** `commonTest`, which has no source root here).
  - Test `ContactInfo.mailto()` — encodes subject/body, omits empty query, prefixes `mailto:`.
  - Move `ProjectItem` + `experiences` out of `ExperienceScreen.kt` into a plain
    `Experiences.kt` (data only, no `@Composable`) and test invariants: every project has a
    non-blank title, ≥1 technology, ≥1 screenshot, non-blank URLs.
  - `kotlin.test` is already wired (`commonTest.dependencies` in `composeApp/build.gradle.kts`);
    confirm it resolves for the `wasmJsTest` source set.
- **Verify:** `./gradlew wasmJsTest` (expect it to compile and RUN tests, not `NO-SOURCE`).
- **Accept:** `wasmJsTest` reports ≥1 executed test and 0 skips; deleting an assertion
  makes it fail red. → Pillar 1: 1 → 2, Pillar 3: 1 → 2.

### Task 4 — Establish a clean baseline commit for the WIP
- **Goal:** Give the agent a clean revert point (pillar-4 requirement).
- **Files:** none edited — a git operation. Stage the coherent WIP (`git add -A`) and commit
  it as a baseline (**only when the user asks** — this audit must not commit), or record an
  explicit stash policy in `CLAUDE.md`. The dirty tree currently *does* compile (Task-0
  compile gate passed UP-TO-DATE), so it is safe to baseline.
- **Verify:** `git status --porcelain` prints nothing.
- **Accept:** clean working tree; `git stash` / revert returns to a known-good state. → Pillar 4 toward 2.

### Task 5 — Add a pre-commit hook that runs the gate
- **Goal:** Hooks run the same gate before each commit, so broken code can't baseline.
- **Files:** a committed hook (e.g. `.githooks/pre-commit` running
  `./gradlew compileKotlinWasmJs`, wired via `git config core.hooksPath .githooks`, documented
  in `CLAUDE.md`).
- **Verify:** stage a file with a deliberate compile error and attempt `git commit` — it is rejected.
- **Accept:** commit is blocked on compile failure. → Pillar 4: 1 → 2.

### Task 6 — Add a lint/format gate  *(lower leverage, do after the loop closes)*
- **Goal:** A cheap, deterministic style oracle beyond compile.
- **Files:** `gradle/libs.versions.toml`, `composeApp/build.gradle.kts` — add ktlint or detekt.
  `kotlin.code.style=official` in `gradle.properties` today is only an IDE hint, not a gate.
- **Verify:** `./gradlew ktlintCheck` (or `detekt`) runs and reports.
- **Accept:** a style violation fails the task; wire it into `check` and the DoD. → strengthens Pillar 1.

### Task 7 — Replace the stale template README
- **Goal:** Remove the misleading `commonMain`/`iosMain`/`jvmMain` references; describe the
  real single-`wasmJs` layout and the run/build/test commands.
- **Files:** `README.md`.
- **Verify:** `grep -c iosMain README.md` returns 0.
- **Accept:** README matches the actual source layout. → supports Pillar 5.

## Re-score target (after Tasks 1–5)

| Pillar | Now | After |
|--------|:--:|:--:|
| Oracle | 1 | **2** (real tests + CI mirror, zero skips) |
| Safe solo | 2 | 2 |
| Verifiable done | 1 | **2** (pure core extracted + asserted) |
| Safety net | 1 | **2** (clean baseline + pre-commit gate) |
| Self-contained | 0 | **1** (CLAUDE.md DoD) |
| **Total** | **5** | **9 → release the agent on the verifiable trail** |

UI/taste work (theme, phone frame, carousel, avatar, layout) stays a fenced zone requiring
human visual sign-off regardless of score — the tests certify logic and data, not pixels.
