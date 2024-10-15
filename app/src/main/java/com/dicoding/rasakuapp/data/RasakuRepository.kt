package com.dicoding.rasakuapp.data

import com.dicoding.rasakuapp.model.FakeRasakuDataSource
import com.dicoding.rasakuapp.model.OrderRasaku
import com.dicoding.rasakuapp.model.Rasaku
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class RasakuRepository {

    private val orderRasaku = mutableListOf<OrderRasaku>()

    init {
        if (orderRasaku.isEmpty()) {
            FakeRasakuDataSource.dummyRasaku.forEach {
                orderRasaku.add(OrderRasaku(it, 0))
            }
        }
    }

    fun getAllRasaku(): Flow<List<OrderRasaku>> {
        return flowOf(orderRasaku)
    }

    fun getOrderRasakuById(rasakuId: Long): OrderRasaku {
        return orderRasaku.first {
            it.rasaku.id == rasakuId
        }
    }

    fun getAddedOrderRasaku(): Flow<List<OrderRasaku>> {
        return getAllRasaku()
            .map { orderRasaku ->
                orderRasaku.filter { orderRasaku ->
                    orderRasaku.count != 0
                }
            }
    }

    fun updateOrderRasaku(rasakuId: Long, newCountValue: Int): Flow<Boolean> {
        val index = orderRasaku.indexOfFirst { it.rasaku.id == rasakuId }
        val result = if (index >= 0) {
            val Rasaku = orderRasaku[index].copy(count = newCountValue)
            orderRasaku[index] = Rasaku // Memperbarui nilai elemen list
            true
        } else {
            false
        }
        return flowOf(result)
    }


    fun getFavoriteRasaku(): Flow<List<OrderRasaku>> {
        return flowOf(orderRasaku.filter { it.isFavorite })
    }


    fun updateFavoriteRasaku(rasakuId: Long, newState: Boolean): Flow<Boolean> {
        val index = orderRasaku.indexOfFirst { it.rasaku.id == rasakuId }
        val result = if (index >= 0) {
            val updatedRasaku = orderRasaku[index].copy(isFavorite = newState)
            orderRasaku[index] = updatedRasaku // Update status favorit
            true
        } else {
            false
        }
        return flowOf(result)
    }

    companion object {
        @Volatile
        private var instance: RasakuRepository? = null
        fun getInstance(): RasakuRepository =
            instance ?: synchronized(this) {
                RasakuRepository().apply {
                    instance = this
                }
            }
    }
}