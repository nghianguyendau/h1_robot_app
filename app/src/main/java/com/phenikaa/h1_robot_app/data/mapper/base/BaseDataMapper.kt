package com.phenikaa.h1_robot_app.data.mapper.base

interface BaseDataMapper<R, E> {
    fun mapToEntity(data: R?): E?
    fun mapToListEntity(listData: List<R?>?): List<E> {
        return listData?.mapNotNull { mapToEntity(it) } ?: emptyList()
    }
}

interface DataMapper<R, E> : BaseDataMapper<R, E> {
    fun mapToData(entity: E): R
    fun mapToNullableData(entity: E?): R? = entity?.let { mapToData(it) }
    fun mapToListData(listEntity: List<E>?): List<R> {
        return listEntity?.map { mapToData(it) } ?: emptyList()
    }
}
