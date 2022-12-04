package ru.grishankov.marketin.util.extension

import kotlinx.coroutines.flow.FlowCollector

/**
 * Функция расширение, DSLK emit
 */
suspend fun <T> FlowCollector<T>.emitFlow(value: () -> T) {
    emit(value())
}