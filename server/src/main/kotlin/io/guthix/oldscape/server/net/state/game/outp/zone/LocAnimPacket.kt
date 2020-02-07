package io.guthix.oldscape.server.net.state.game.outp.zone

import io.guthix.buffer.writeByteADD
import io.guthix.oldscape.server.net.state.game.FixedSize
import io.guthix.oldscape.server.net.state.game.ZoneOutGameEvent
import io.guthix.oldscape.server.world.mapsquare.zone.tile.TileUnit
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext

class LocAnimPacket(
    private val animId: Int,
    private val locType: Int,
    private val locOrientation: Int,
    localX: TileUnit,
    localY: TileUnit
) : ZoneOutGameEvent(localX, localY) {
    override val opcode = 48

    override val enclOpcode = 9

    override val size = FixedSize(STATIC_SIZE)

    override fun encode(ctx: ChannelHandlerContext): ByteBuf {
        val buf = ctx.alloc().buffer(STATIC_SIZE)
        buf.writeShort(animId)
        buf.writeByte(posBitPack)
        buf.writeByteADD((locType shl 2) or (locOrientation and 0x3))
        return buf
    }

    companion object {
        const val STATIC_SIZE = Short.SIZE_BYTES + Byte.SIZE_BYTES + Byte.SIZE_BYTES
    }
}