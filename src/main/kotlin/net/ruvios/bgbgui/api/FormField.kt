package net.ruvios.bgbgui.api

sealed interface FormField {

    data class Label(
        val text: String,
    ) : FormField

    data class Input @JvmOverloads constructor(
        val id: String,
        val label: String,
        val placeholder: String = "",
        val default: String = "",
    ) : FormField

    data class Dropdown @JvmOverloads constructor(
        val id: String,
        val label: String,
        val options: List<String>,
        val defaultIndex: Int = 0,
    ) : FormField

    data class Toggle @JvmOverloads constructor(
        val id: String,
        val label: String,
        val default: Boolean = false,
    ) : FormField

    data class Slider @JvmOverloads constructor(
        val id: String,
        val label: String,
        val min: Float,
        val max: Float,
        val step: Float = 1f,
        val default: Float = min,
    ) : FormField

    companion object {
        @JvmStatic
        fun label(text: String): FormField {
            return Label(text)
        }

        @JvmStatic
        @JvmOverloads
        fun input(
            id: String,
            label: String,
            placeholder: String = "",
            default: String = "",
        ): FormField {
            return Input(id, label, placeholder, default)
        }

        @JvmStatic
        @JvmOverloads
        fun dropdown(
            id: String,
            label: String,
            options: List<String>,
            defaultIndex: Int = 0,
        ): FormField {
            return Dropdown(id, label, options, defaultIndex)
        }

        @JvmStatic
        fun dropdown(
            id: String,
            label: String,
            vararg options: String,
        ): FormField {
            return Dropdown(id, label, options.toList())
        }

        @JvmStatic
        @JvmOverloads
        fun toggle(
            id: String,
            label: String,
            default: Boolean = false,
        ): FormField {
            return Toggle(id, label, default)
        }

        @JvmStatic
        @JvmOverloads
        fun slider(
            id: String,
            label: String,
            min: Float,
            max: Float,
            step: Float = 1f,
            default: Float = min,
        ): FormField {
            return Slider(id, label, min, max, step, default)
        }
    }
}

class FormValues internal constructor(
    private val values: Map<String, Any?>,
) {

    fun getString(id: String): String {
        return values[id]?.toString() ?: ""
    }

    fun getInt(id: String): Int {
        return when (val value = values[id]) {
            is Int -> value
            is Number -> value.toInt()
            is String -> value.toIntOrNull() ?: 0
            else -> 0
        }
    }

    fun getBoolean(id: String): Boolean {
        return values[id] as? Boolean ?: false
    }

    fun getFloat(id: String): Float {
        return when (val value = values[id]) {
            is Float -> value
            is Number -> value.toFloat()
            is String -> value.toFloatOrNull() ?: 0f
            else -> 0f
        }
    }

    fun string(id: String): String = getString(id)

    fun int(id: String): Int = getInt(id)

    fun bool(id: String): Boolean = getBoolean(id)

    fun float(id: String): Float = getFloat(id)
}
