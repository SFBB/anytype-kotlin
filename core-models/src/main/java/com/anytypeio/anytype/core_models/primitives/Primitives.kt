package com.anytypeio.anytype.core_models.primitives

@JvmInline
value class Id(val id: String)

@JvmInline
value class Key(val key: String)

@JvmInline
value class SpaceId(val id: String)

@JvmInline
value class TypeId(val id: String)

@JvmInline
value class TypeKey(val key: String)

@JvmInline
value class RelationId(val id: String)

@JvmInline
value class RelationKey(val key: String)

@JvmInline
value class TimestampInSeconds(val time: Long) {
    val inMillis get() = time * 1000
}

typealias Space = SpaceId
