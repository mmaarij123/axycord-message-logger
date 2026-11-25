# Message Logger for Axycord

Maintained by **Maarij**. Native Axycord port of the Microcord plugin.

## Features and use

Keeps messages this session sees deleted in chat and shows earlier text for edits.

- Deleted messages are marked red; edit history includes the previous text and time.
- Choose whether to log deletes/edits, ignore muted channels/servers, or ignore bots.
- Message menu: **Remove From Logs**. Server/channel menus: **Stop Logging** / **Resume Logging**.
- Settings: **Clear logs** removes retained deleted messages locally; **Log everywhere again** resets ignored targets.

Logs are held in memory, capped to 2,000 message records. They are cleared when the
plugin stops or Axycord restarts. Kept deleted messages can remain in the local chat
until their channel reloads. It cannot recover messages the client never observed.
No message history is uploaded to a third-party server.

## Install

Download **Axycord-MessageLogger-v1.0.0.apk** from
[Releases](https://github.com/mmaarij123/axycord-message-logger/releases/latest), or install it through
**Axycord Settings → Plugins → Browse Plugins**. Approve installation and the
plugin signing certificate when prompted, then enable **Message Logger**.

Requires Android 6.0+ and Axycord plugin API level 3, available in Axycord 1.0.
Plugin ID: `axycord.messagelogger`. Android package: `mmaarij.axycord.messagelogger`.
Plugin version: **1.0.0**. It uses the Axycord Plugin SDK **1.0.6**; these are separate
version numbers. No companion launcher activity is needed.

## Typography

The extra history view explicitly resolves Axycord's `font_primary_normal`.
Host bundled Whitney fonts are the fallback, never Android's system typeface.
Message decorations retain the native chat font; settings, menus and confirmations
use Axycord's existing native components.

## Axycord port fixes

History reads return stable snapshots so new edits on the store thread cannot
mutate a history list while the UI is drawing it.

## Build and verify

Use JDK 21, Android SDK `platforms;android-37.0` and Build Tools `36.0.0`.
Set `ANDROID_HOME` or configure `sdk.dir` in an ignored `local.properties` file.
The SDK and `mmaarij.axycord.plugin` Gradle plugin resolve from Axycord's public
Maven branch without a GitHub login.

```bash
./gradlew lintDebug testDebugUnitTest assembleDebug
```

Windows: `.\gradlew.bat lintDebug testDebugUnitTest assembleDebug`.
GitHub Actions runs the same checks and saves a debug APK as an artifact. Debug
artifacts use the runner's key and are intended for verification; published
release APKs use the maintainer's stable plugin key.

For a signed release, copy `keystore.properties.example` to `keystore.properties`,
fill in the original signing-key details and run `assembleRelease`. Release output
is under `build/outputs/apk/release/`. Keep the key private and reuse it for updates.
The signing properties and key files are ignored by Git.

To compile against a local host checkout, generate `pluginSdkJar` there and pass
`-PaxycordCheckout=/path/to/axycord`. Normal builds use the public SDK.

## Catalog updates

The [Axycord Plugins collection](https://github.com/Axycord/Axycord-Plugins) contains
the corresponding source module and a pinned release entry. For updates, increase
`versionCode` and `versionName`, release with the same key, synchronize that module,
and update the catalog URL, size and SHA-256. Catalog CI verifies the APK package,
plugin metadata and signing fingerprint before publishing.

## Verification boundary

Build/lint and offline regression checks cover the port. Live account actions,
third-party service behavior and visual rendering on a device require separate
runtime verification. The plugin never needs your Discord token in its settings.

## License and credits

GPL-3.0; see [LICENSE](LICENSE) and [NOTICE.md](NOTICE.md). Original credits
are preserved there. The Axycord adaptation is maintained by Maarij.
