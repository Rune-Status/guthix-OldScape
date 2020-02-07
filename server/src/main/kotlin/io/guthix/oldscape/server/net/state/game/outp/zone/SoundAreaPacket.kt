package io.guthix.oldscape.server.net.state.game.outp.zone

import io.guthix.buffer.writeByteADD
import io.guthix.buffer.writeByteNEG
import io.guthix.oldscape.server.net.state.game.FixedSize
import io.guthix.oldscape.server.net.state.game.ZoneOutGameEvent
import io.guthix.oldscape.server.world.mapsquare.FloorUnit
import io.guthix.oldscape.server.world.mapsquare.zone.tile.TileUnit
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import kotlinx.coroutines.delay

class SoundAreaPacket(
    private val id: Int,
    private val delay: Int,
    private val loopCount: Int,
    private val floor: FloorUnit,
    localX: TileUnit,
    localY: TileUnit
) : ZoneOutGameEvent(localX, localY) {
    override val opcode = 33

    override val enclOpcode = 4

    override val size = FixedSize(STATIC_SIZE)

    override fun encode(ctx: ChannelHandlerContext): ByteBuf {
        val buf = ctx.alloc().buffer(STATIC_SIZE)
        buf.writeByteNEG(delay)
        buf.writeByteADD((floor.value shr 4) or loopCount)
        buf.writeByteADD(posBitPack)
        buf.writeShortLE(id)
        return buf
    }

    companion object {
        const val STATIC_SIZE = Byte.SIZE_BYTES + Byte.SIZE_BYTES + Byte.SIZE_BYTES + Short.SIZE_BYTES
    }
}