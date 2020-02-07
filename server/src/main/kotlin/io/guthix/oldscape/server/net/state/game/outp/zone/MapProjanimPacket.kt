package io.guthix.oldscape.server.net.state.game.outp.zone

import io.guthix.oldscape.server.net.state.game.FixedSize
import io.guthix.oldscape.server.net.state.game.ZoneOutGameEvent
import io.guthix.oldscape.server.world.mapsquare.zone.tile.TileUnit
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext

class MapProjanimPacket(
    localX: TileUnit,
    localY: TileUnit
) : ZoneOutGameEvent(localX, localY) {
    override val opcode = 65

    override val enclOpcode = 6

    override val size = FixedSize(STATIC_SIZE)

    override fun encode(ctx: ChannelHandlerContext): ByteBuf {
        TODO("not implemented")
    }

    companion object {
        const val STATIC_SIZE = 15
    }
}