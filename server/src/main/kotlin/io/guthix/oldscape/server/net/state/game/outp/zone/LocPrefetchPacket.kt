package io.guthix.oldscape.server.net.state.game.outp.zone

import io.guthix.buffer.writeByteADD
import io.guthix.buffer.writeByteNEG
import io.guthix.buffer.writeByteSUB
import io.guthix.buffer.writeShortLEADD
import io.guthix.oldscape.server.net.state.game.FixedSize
import io.guthix.oldscape.server.net.state.game.ZoneOutGameEvent
import io.guthix.oldscape.server.world.mapsquare.zone.tile.TileUnit
import io.guthix.oldscape.server.world.mapsquare.zone.tile.TileUnitRange
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext

class LocPrefetchPacket(
    val playerId: Int,
    val locId: Int,
    val locType: Int,
    val locRotation: Int,
    val xRange: TileUnitRange,
    val yRange: TileUnitRange,
    val cycleRange: IntRange,
    localX: TileUnit,
    localY: TileUnit
) : ZoneOutGameEvent(localX, localY) {
    override val opcode = 76

    override val enclOpcode = 5

    override val size = FixedSize(STATIC_SIZE)

    override fun encode(ctx: ChannelHandlerContext): ByteBuf {
        val buf = ctx.alloc().buffer(STATIC_SIZE)
        buf.writeShortLEADD(cycleRange.first)
        buf.writeShort(locId)
        buf.writeByteNEG((locType shl 2) or (locRotation and 0x3))
        buf.writeShort(cycleRange.last)
        buf.writeByteSUB(xRange.last.value)
        buf.writeByteADD(posBitPack)
        buf.writeShortLE(playerId)
        buf.writeByteADD(yRange.last.value)
        buf.writeByte(xRange.first.value)
        buf.writeByteNEG(yRange.first.value)
        return buf
    }

    companion object {
        const val STATIC_SIZE = Short.SIZE_BYTES + Short.SIZE_BYTES + Byte.SIZE_BYTES + Short.SIZE_BYTES +
            Byte.SIZE_BYTES + Byte.SIZE_BYTES + Short.SIZE_BYTES + Byte.SIZE_BYTES + Byte.SIZE_BYTES + Byte.SIZE_BYTES
    }
}