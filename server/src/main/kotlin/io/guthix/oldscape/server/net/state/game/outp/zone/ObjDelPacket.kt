package io.guthix.oldscape.server.net.state.game.outp.zone

import io.guthix.buffer.writeByteNEG
import io.guthix.oldscape.server.net.state.game.FixedSize
import io.guthix.oldscape.server.net.state.game.ZoneOutGameEvent
import io.guthix.oldscape.server.world.mapsquare.zone.tile.TileUnit
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext

class ObjDelPacket(
    private val id: Int,
    localX: TileUnit,
    localY: TileUnit
) : ZoneOutGameEvent(localX, localY) {
    override val opcode = 10

    override val enclOpcode = 3

    override val size = FixedSize(STATIC_SIZE)

    override fun encode(ctx: ChannelHandlerContext): ByteBuf {
        val buf = ctx.alloc().buffer(STATIC_SIZE)
        buf.writeShort(id)
        buf.writeByteNEG(posBitPack)
        return buf
    }

    companion object {
        const val STATIC_SIZE = Short.SIZE_BYTES + Byte.SIZE_BYTES
    }
}