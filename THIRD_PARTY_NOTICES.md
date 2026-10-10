# Third-party notices

PCBCalc's original application code and the new public Word report templates are licensed under MIT by Alexander Baranov. This does not relicense external libraries, platform APIs or dependencies.

- Word export uses [poi-on-android](https://github.com/centic9/poi-on-android), Apache-2.0, by Dominik Stadler and contributors. The official [0.6 release](https://github.com/centic9/poi-on-android/releases/tag/0.6) is downloaded separately, unmodified, with a pinned checksum. It includes Apache POI 3.17 and Aalto XML 1.1.0 plus their dependencies; see the upstream build and notices for their licenses. No custom historical shaded JAR is included in Git.
- Android support libraries and Room: Apache-2.0, Android Open Source Project and contributors; resolved through Gradle.
- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart), copyright Philipp Jahoda and contributors, Apache-2.0; resolved through Gradle.
- The verification harness uses JUnit 4.13.2 under EPL-1.0 and Gradle under Apache-2.0.
- `tools/generate_reports.py` uses python-docx under its MIT license, when installed separately.

The original college-provided DOCX content is excluded from this public repository. New templates and their exporter were written for this release.
