package com.example.rcslocationtracking.contacts

import android.content.Context

object TrustedContactsManager {

    private const val PREF =
        "trusted_contacts"

    private const val KEY =
        "numbers"

    fun isTrusted(
        context: Context,
        number: String
    ): Boolean {

        val contacts = getAll(
            context
        )

        return contacts.contains(
            normalize(
                number
            )
        )
    }

    fun removeNumber(
        context: Context,
        number: String
    ) {

        val prefs =
            context.getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )

        val set =
            prefs.getStringSet(
                KEY,
                mutableSetOf()
            )!!.toMutableSet()

        set.remove(
            normalize(number)
        )

        prefs.edit()

            .putStringSet(
                KEY,
                set
            )

            .commit()
    }

    fun addNumber(
        context: Context,
        number: String
    ) {

        val prefs =
            context.getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )

        val existing =

            prefs.getStringSet(
                KEY,
                HashSet()
            ) ?: HashSet()

        val updated =
            HashSet(existing)

        updated.add(

            normalize(
                number
            )
        )

        prefs.edit()

            .putStringSet(
                KEY,
                updated
            )

            .commit()
    }

    fun getAll(
        context: Context
    ): Set<String> {

        val prefs =
            context.getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )

        return prefs.getStringSet(
            KEY,
            emptySet()
        ) ?: emptySet()
    }

    private fun normalize(
        num: String
    ): String {

        return num

            .replace(
                "\\s".toRegex(),
                ""
            )

            .replace(
                "+91",
                ""
            )

            .takeLast(10)
    }
}