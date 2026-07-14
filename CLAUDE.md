# CLAUDE.md — kevin-portfolio-wasm-kmp

Kevin Campos' personal portfolio: a Kotlin Multiplatform / Compose Multiplatform app with a
single `wasmJs` target, deployed as a static site to GitHub Pages. All the code lives in one
module, `:composeApp`.

## Definition of Done

A change is done only when **both** of these pass locally, reporting real exit codes (do not
pipe to `tail`/`head` — a pipe can mask `BUILD FAILED`):

```bash
./gradlew compileKotlinWasmJs   # the Wasm build compiles
./gradlew check                 # compiles + RUNS the tests (wasmJsTest, in headless Chrome)
```

`./gradlew check` is the real gate. It compiles every source file and executes the tests in
`composeApp/src/wasmJsTest/`. If either command fails, the change is not done.

## Footguns (read before touching this repo)

- **There is no `commonMain` / `commonTest`.** Despite what the (stale) `README.md` says, this
  project has a single target. All production code is under
  `composeApp/src/wasmJsMain/kotlin/br/app/kevin/portfolio/`. **Tests go in
  `composeApp/src/wasmJsTest/kotlin/br/app/kevin/portfolio/`** — NOT `commonTest`, which has no
  source root here. (`kotlin.test` is wired via `commonTest.dependencies` in
  `composeApp/build.gradle.kts` and is inherited by `wasmJsTest`.)
- **`check` used to be a false green.** Before the pure core was extracted there were zero tests,
  so `check` passed with `wasmJsTest NO-SOURCE` — it verified nothing. It is now a real oracle;
  keep it that way (every new bit of pure logic gets a test). Don't trust a green `check` on a
  tree with no tests.
- **Tests run in a headless browser.** `wasmJsTest` compiles to Wasm and runs under Karma +
  headless Chrome, so a Chrome/Chromium install is required locally and in CI. The no-sandbox
  launcher lives in `composeApp/karma.config.d/karma.conf.js`. Pure-logic tests are fine; there
  is no Compose UI-rendering test harness — assert **data and pure functions**, not pixels.
- **Active work happens on the `work-in-progress` branch**, often with a dirty tree and no clean
  baseline commit. Don't revert or "tidy" the WIP UI work (theme, phone frame, screenshot
  carousel, avatar, footer). Add tests/config *around* it.

## What is verifiable vs what needs a human

- **Verifiable (an agent may close the loop here):** the pure core — `ContactInfo.mailto()` and
  the `ProjectItem` / `experiences` data in `ui/Experiences.kt`. Assert it in `wasmJsTest`.
- **Fenced / taste (human visual sign-off required):** everything `@Composable` — layout,
  spacing, `AppTheme`, `PhoneFrame`, `ScreenshotCarousel`, `ProfileAvatar`, `WavingEmoji`. Tests
  certify logic and data, not the look. Do not claim UI work is "done" from a green build alone.

## Where the spec lives

The WIP visual scope (new theme, phone frame, carousel, footer) is defined in Kevin's own notes
**outside this repo** — ask before assuming scope. The autonomy audit and hardening plan for this
repo is `AGENT-READINESS.md` at the root.

## CI

`.github/workflows/ci.yml` runs the **same** gate an agent runs locally — `./gradlew check
--no-daemon` — on every push (any branch) and every pull request. That is the CI signal for the
`work-in-progress` branch. `.github/workflows/deploy.yml` builds and publishes to GitHub Pages
and is **gated to `main` only**.

## Publishing is a side effect — never do it unasked

Pushing to `main` triggers `deploy.yml`, which publishes the live site to GitHub Pages. **Never
`git push origin main` (or merge to `main`) without an explicit request.** Local verification and
CI on `work-in-progress` never deploy. Do not commit unless asked.
