// Run wasmJsTest under a headless Chrome that also works on CI runners (containers / root),
// where the default ChromeHeadless sandbox fails. Applied on top of the config Kotlin generates.
config.set({
    browsers: ["ChromeHeadlessNoSandbox"],
    customLaunchers: {
        ChromeHeadlessNoSandbox: {
            base: "ChromeHeadless",
            flags: ["--no-sandbox", "--disable-gpu"],
        },
    },
});
