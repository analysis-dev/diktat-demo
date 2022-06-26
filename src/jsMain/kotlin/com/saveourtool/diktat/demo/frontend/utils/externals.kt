/**
 * External JS declarations
 */

@file:Suppress(
    "MISSING_KDOC_TOP_LEVEL",
    "MISSING_KDOC_ON_FUNCTION",
    "MISSING_KDOC_CLASS_ELEMENTS",
    "KDOC_WITHOUT_PARAM_TAG",
    "KDOC_WITHOUT_RETURN_TAG",
)

package com.saveourtool.diktat.demo.frontend.utils

@Suppress("BACKTICKS_PROHIBITED")
external var `$`: dynamic = definedExternally

@JsModule("ace-builds")
@JsNonModule
@JsName("ace")
external object Ace {
    @JsName("edit")
    fun edit(editorName: String): Editor
}

@JsModule("ace-code-editor")
@JsNonModule
external class Editor {
    /**
     * @param path path to ace theme
     */
    fun setTheme(path: String)

    fun getSession(): Session

    fun setReadOnly(readOnly: Boolean)

    class Session {
        fun setMode(mode: String)

        fun getValue(): String

        fun setValue(text: String)

        fun on(eventName: String, handler: () -> Unit)
    }
}
