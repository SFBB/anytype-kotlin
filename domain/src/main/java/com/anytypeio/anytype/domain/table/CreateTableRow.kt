package com.anytypeio.anytype.domain.table

import com.anytypeio.anytype.core_models.Id
import com.anytypeio.anytype.core_models.Payload
import com.anytypeio.anytype.core_models.Position
import com.anytypeio.anytype.domain.base.BaseUseCase
import com.anytypeio.anytype.domain.base.Either
import com.anytypeio.anytype.domain.block.repo.BlockRepository

class CreateTableRow(
    private val repo: BlockRepository
) : BaseUseCase<Payload, CreateTableRow.Params>() {

    override suspend fun run(params: Params): Either<Throwable, Payload> = safe {
        repo.createTableRow(
            ctx = params.ctx,
            targetId = params.target,
            position = params.position
        )
    }

    /**
     * @param ctx - id of the context object
     * @param target - id of the closest row
     * @param position - position of the new row, relative to [target]
     */
    data class Params(
        val ctx: Id,
        val target: Id,
        val position: Position
    )
}