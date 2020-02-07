package io.guthix.oldscape.server.net.state.game.outp.zone

import io.guthix.buffer.writeByteNEG
import io.guthix.buffer.writeByteSUB
import io.guthix.buffer.writeShortLEADD
import io.guthix.oldscape.server.net.state.game.FixedSize
import io.guthix.oldscape.server.net.state.game.ZoneOutGameEvent
import io.guthix.oldscape.server.world.mapsquare.zone.tile.TileUnit
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext

class LocAddChangePacket(
    private val id: Int,
    private val type: Int,
    private val orientation: Int,
    localX: TileUnit,
    localY: TileUnit
) : ZoneOutGameEvent(localX, localY) {
    override val opcode = 16

    override val enclOpcode = 0

    override val size = FixedSize(STATIC_SIZE)

    override fun encode(ctx: ChannelHandlerContext): ByteBuf {
        val buf = ctx.alloc().buffer(STATIC_SIZE)
        buf.writeByteNEG(posBitPack)
        buf.writeShortLEADD(id)
        buf.writeByteSUB((type shl 2) or (orientation and 0x3))
        return buf
    }

    companion object {
        const val STATIC_SIZE = Short.SIZE_BYTES + Byte.SIZE_BYTES + Byte.SIZE_BYTES
    }
}