package io.guthix.oldscape.server.net.state.game.outp.zone

import io.guthix.buffer.writeShortADD
import io.guthix.buffer.writeShortLEADD
import io.guthix.oldscape.server.net.state.game.FixedSize
import io.guthix.oldscape.server.net.state.game.ZoneOutGameEvent
import io.guthix.oldscape.server.world.mapsquare.zone.tile.TileUnit
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext

class ObjCountPacket(
    private val id: Int,
    private val oldCount: Int,
    private val newCount: Int,
    localX: TileUnit,
    localY: TileUnit
) : ZoneOutGameEvent(localX, localY) {
    override val opcode = 68

    override val enclOpcode = 7

    override val size = FixedSize(STATIC_SIZE)

    override fun encode(ctx: ChannelHandlerContext): ByteBuf {
        val buf = ctx.alloc().buffer(STATIC_SIZE)
        buf.writeShortADD(id)
        buf.writeShortLEADD(newCount)
        buf.writeByte(posBitPack)
        buf.writeShortADD(oldCount)
        return buf
    }

    companion object {
        const val STATIC_SIZE = Short.SIZE_BYTES + Short.SIZE_BYTES + Byte.SIZE_BYTES + Short.SIZE_BYTES
    }
}