<div align="center">
   <img width="160" src="./doc/img/ic_launcher.png" alt="logo">
   <h1>FixRedMagicWindow</h1>
   <p>An Xposed module that removes the small-window (split-window) app limits on RedMagic OS 9 (Android 14).</p>
</div>

---

## Requirements

- RedMagic OS 9 (Android 14) — other versions/ROMs are not supported
- A root/Xposed framework (e.g. LSPosed) to activate the module

## What it does

Patches system-level restrictions so any app can be opened in small/split-window mode, and removes the cap on how many windows can be open at once.

## Building

```bash
./gradlew assembleRelease
```

By default the release build falls back to a debug test key. To sign with your own key, copy `sign.properties.template` to `sign.properties` and fill in your keystore details.

## Downloads

See the [Releases](https://github.com/Gio470/FixRedMagicWindow/releases) page for prebuilt APKs.

## License

[GNU General Public License v3.0](./LICENSE)
