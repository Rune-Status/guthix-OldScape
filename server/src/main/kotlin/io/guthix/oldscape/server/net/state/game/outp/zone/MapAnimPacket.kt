package io.guthix.oldscape.server.net.state.game.outp.zone

import io.guthix.buffer.writeByteSUB
import io.guthix.buffer.writeShortLEADD
import io.guthix.oldscape.server.net.state.game.FixedSize
import io.guthix.oldscape.server.net.state.game.ZoneOutGameEvent
import io.guthix.oldscape.server.world.mapsquare.FloorUnit
import io.guthix.oldscape.server.world.mapsquare.zone.tile.TileUnit
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext

class MapAnimPacket(
    private val id: Int,
    private val delay: Int,
    private val floor: FloorUnit,
    localX: TileUnit,
    localY: TileUnit
) : ZoneOutGameEvent(localX, localY) {
    override val opcode = 26

    override val enclOpcode = 2

    override val size = FixedSize(STATIC_SIZE)

    override fun encode(ctx: ChannelHandlerContext): ByteBuf {
        val buf = ctx.alloc().buffer(STATIC_SIZE)
        buf.writeByteSUB(floor.value)
        buf.writeShortLEADD(delay)
        buf.writeShortLEADD(id)
        buf.writeByte(posBitPack)
        return buf
    }

    companion object {
        const val STATIC_SIZE = Byte.SIZE_BYTES + Short.SIZE_BYTES + Short.SIZE_BYTES + Byte.SIZE_BYTES
    }
}