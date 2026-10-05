# QLauncher
- Gradle8.0
- JDK-17

## APK releases

Run **Build and publish release APK** from the GitHub Actions page, or push a
`v*` tag matching `versionName` in `app/build.gradle` (for example, `v1.0.1`).
The workflow publishes a signed `MyLauncher-v<version>.apk` and its SHA-256
checksum to GitHub Releases.

Signing uses repository secrets: `ANDROID_KEYSTORE_BASE64`,
`ANDROID_KEYSTORE_PASSWORD`, `ANDROID_KEY_ALIAS`, and `ANDROID_KEY_PASSWORD`.
Keep the same signing key for future versions so installed apps can be updated.
