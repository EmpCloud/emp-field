"""Run with python3 iOS/EmpMonitor/Tests/check_api_logging.py on macOS."""

import json
from pathlib import Path
import subprocess
import tempfile


source = (Path(__file__).parents[1] / "EmpMonitor/Helpers/Constants/Constants.swift").read_text()
logger = source[source.index("enum AppLog {"):source.index("enum DeviceStatusDebug {")]
harness = r'''
import Foundation
'''
harness += logger + r'''
var request = URLRequest(url: URL(string: "https://example.test/api?token=query-secret#fragment")!)
request.httpMethod = "POST"
request.setValue("header-secret", forHTTPHeaderField: "x-access-token")
let response = HTTPURLResponse(url: request.url!, statusCode: 422, httpVersion: nil, headerFields: nil)!
let payload = #"{"statusCode":422,"body":{"message":"Invalid date","accessToken":"token-secret","items":[{"password":"password-secret","api_key":"key-secret","otp":"otp-secret","amount":1.5}],"value":null}}"#
AppLog.apiResponse(Data(payload.utf8), response: response, request: request)
AppLog.apiResponse(Data("Service unavailable".utf8), response: response, request: request)
AppLog.apiResponse(Data([0xff]), response: response, request: request)
AppLog.apiResponse(Data(), response: response, request: request)
AppLog.apiResponse(Data("[1,true,null]".utf8), response: response, request: request)
AppLog.apiResponse(Data("42".utf8), response: response, request: request)
var evaluated = false
AppLog.debug({ evaluated = true; return "debug output" }())
#if DEBUG
assert(evaluated)
#else
assert(!evaluated)
#endif
'''

with tempfile.TemporaryDirectory(prefix="emp-api-log-check-") as scratch:
    directory = Path(scratch)
    swift = directory / "main.swift"
    swift.write_text(harness)
    for flags in ([], ["-D", "DEBUG"]):
        binary = directory / "checks"
        subprocess.run(["xcrun", "swiftc", "-O", "-module-cache-path", str(directory / "cache"), *flags, str(swift), "-o", str(binary)], check=True, capture_output=True, text=True)
        output = subprocess.run([str(binary)], check=True, capture_output=True, text=True).stdout
        assert all(secret not in output for secret in ("token-secret", "password-secret", "key-secret", "otp-secret", "query-secret", "header-secret", "#fragment"))
        entries = output.split("[API] ")[1:]
        assert len(entries) == 6
        assert all(entry.startswith("POST https://example.test/api — Response (422)\n") for entry in entries)
        body = json.loads(entries[0].split("\n", 1)[1])["body"]
        assert body["message"] == "Invalid date" and body["value"] is None
        assert body["accessToken"] == "<redacted>"
        assert body["items"] == [{"password": "<redacted>", "api_key": "<redacted>", "otp": "<redacted>", "amount": 1.5}]
        assert entries[1].split("\n", 1)[1].strip() == "Service unavailable"
        assert entries[2].split("\n", 1)[1].strip() == "<non-utf8 data>"
        assert entries[3].split("\n", 1)[1].strip() == ""
        assert json.loads(entries[4].split("\n", 1)[1]) == [1, True, None]
        assert entries[5].split("\n", 1)[1].splitlines()[0] == "42"
        assert ("debug output" in output) == bool(flags)

print("API logging checks passed in Debug and optimized Release.")
