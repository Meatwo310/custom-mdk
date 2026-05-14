fun String.supportsGameTestServer(): Boolean {
    val parts = split(".").mapNotNull(String::toIntOrNull)
    val major = parts.getOrElse(0) { 0 }
    val minor = parts.getOrElse(1) { 0 }
    return major > 26 || (major == 26 && minor >= 1)
}
