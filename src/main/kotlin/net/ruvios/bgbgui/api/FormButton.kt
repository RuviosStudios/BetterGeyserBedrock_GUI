package net.ruvios.bgbgui.api

enum class FormImageType {
    URL,
    PATH,
}

data class FormButton @JvmOverloads constructor(
    val label: String,
    val image: String? = null,
    val imageType: FormImageType = FormImageType.URL,
) {

    companion object {
        @JvmStatic
        fun of(label: String): FormButton {
            return FormButton(label)
        }

        @JvmStatic
        fun url(label: String, url: String): FormButton {
            return FormButton(label, url, FormImageType.URL)
        }

        @JvmStatic
        fun path(label: String, path: String): FormButton {
            return FormButton(label, path, FormImageType.PATH)
        }

        /** URL bei http:// oder https://, sonst Resource-Pack-Pfad. */
        @JvmStatic
        fun auto(label: String, image: String?): FormButton {
            if (image.isNullOrBlank()) {
                return FormButton(label)
            }
            val type = if (image.startsWith("http://") || image.startsWith("https://")) {
                FormImageType.URL
            } else {
                FormImageType.PATH
            }
            return FormButton(label, image, type)
        }
    }
}
