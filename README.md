<p align="center"><img src="assets/poster.png" alt="PCBCalc — circuits meet mechanics" width="100%"></p>

# PCBCalc

My graduation project at **Ивановский энергетический колледж**, where I studied computer systems technology. An Android application for educational PCB vibration and reliability calculations, built in Java in 2018.

## What it does

- Calculates vibration stability and vibration strength from board geometry, material, mounting and load parameters.
- Estimates failure rate, average operating time and probability of failure-free operation.
- Displays calculation results and charts.
- Exports results to editable Word reports.

The public release fixes a unit-conversion bug in the bending-strength check: stress already expressed in MPa must not be divided by a million again. Six JVM tests verify conversions, thickness scaling and the strict strength boundary. A seventh test fills every bookmark in all three real report templates, saves each DOCX and reloads it to verify the values.

## Run the calculation and report tests

Requires Python 3 for the dependency download and JDK 17–21 for the standalone verification project:

```sh
python3 tools/fetch_poi.py
./verification/gradlew -p verification test
```

The verification build compiles the production calculation helper and report exporter. Its Android storage adapter is confined to the JVM test harness. It does not build or exercise the full Android UI.

## Historical Android build

The app retains Android Gradle Plugin 3.1.2, Gradle 4.4, Android SDK 27 and Build Tools 27.0.3. It needs an older compatible JDK such as JDK 8. Configure your SDK in local `local.properties`, fetch POI as above, then run `./gradlew :app:assembleDebug`.

The full APK build has not been verified for this public release. The app is a historical diploma project, not a current engineering certification tool or a ready-to-publish store build. Its UI, storage permissions and dependencies need migration for current Android versions.

## Word reports

The public source includes three original report templates created for this release. They preserve the application's replacement keys without redistributing college-provided template content. Reports list the computed parameters and results; they do not reproduce the old teaching material's derivations and illustrations.

To regenerate the templates, install `python-docx` and run `python3 tools/generate_reports.py`. Field identifiers are recorded in `tools/report-fields.json`. The templates were rendered and inspected; export and reload are covered by tests.

The old custom `poishadow-all.jar` is not distributed. `tools/fetch_poi.py` downloads the official poi-on-android 0.6 release and verifies its pinned SHA-256 before writing it locally. The report exporter uses that verified version.

## License and history

Original project code and new report templates: [MIT](LICENSE). External dependencies retain their licenses; see [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md).

This public repository starts from a prepared source snapshot. The original development history and college-provided resources are preserved in a private archive.
