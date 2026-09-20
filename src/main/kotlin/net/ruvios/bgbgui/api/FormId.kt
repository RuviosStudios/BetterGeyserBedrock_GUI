package net.ruvios.bgbgui.api

internal fun resolveFormId(id: String?, title: String): String {
    val trimmed = id?.trim().orEmpty()
    if (trimmed.isEmpty()) {
        return title
    }
    return trimmed
}
