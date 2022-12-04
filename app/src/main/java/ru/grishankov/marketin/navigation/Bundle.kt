package ru.grishankov.marketin.navigation

/**
 * Пакет с данными
 */
class Bundle {
    private val bundle: HashMap<String, Any> = hashMapOf()

    /**
     * Получить String
     */
    fun getString(key: String, defaultValue: String): String {
        return bundle[key] as? String ?: defaultValue
    }

    /**
     * Получить Boolean
     */
    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return bundle[key] as? Boolean ?: defaultValue
    }

    /**
     * Получить Int
     */
    fun getInt(key: String, defaultValue: Int): Int {
        return bundle[key] as? Int ?: defaultValue
    }

    /**
     * Получить IBundled
     */
    fun getBundled(key: String, defaultValue: IBundled): IBundled {
        return bundle[key] as? IBundled ?: defaultValue
    }

    /**
     * Добавить любое значение
     */
    fun putAny(key: String, value: Any) {
        bundle[key] = value
    }
}

/**
 * Пакетная модель
 */
interface IBundled

/**
 * Добавить пакет с даными
 */
fun bundle(vararg bundles: Pair<String, Any>): Bundle {
    val bundle = Bundle()
    bundles.forEach {
        bundle.putAny(it.first, it.second)
    }
    return bundle
}