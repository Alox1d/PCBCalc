"""Fetch the pinned, unmodified poi-on-android 0.6 release. Python 3 standard library only."""
from pathlib import Path
import hashlib, urllib.request

URL = "https://github.com/centic9/poi-on-android/releases/download/0.6/poishadow-all.jar"
SHA256 = "b0713288e2dc5d7318fd413f8f8ac611d2a4a4a3396061d34ff97a6a262ed3ad"
data = urllib.request.urlopen(URL, timeout=120).read()
if hashlib.sha256(data).hexdigest() != SHA256:
    raise SystemExit("POI checksum mismatch; no file was written")
target = Path(__file__).resolve().parents[1] / "app/libs/poishadow-all.jar"
target.parent.mkdir(parents=True, exist_ok=True)
target.write_bytes(data)
print("Verified poi-on-android 0.6 downloaded")
