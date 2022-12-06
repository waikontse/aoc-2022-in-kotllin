package week1

import shared.Puzzle
import java.lang.IllegalArgumentException

class CommunicationDevice : Puzzle(6) {
    override fun solveFirstPart(): Any {
        return puzzleInput.map { it.toCharArray().toList() }
            .map { findStartOfPacket(it, 4) }
    }

    override fun solveSecondPart(): Any {
        return puzzleInput.map { it.toCharArray().toList() }
            .map { findStartOfPacket(it, 14) }
    }

    private fun findStartOfPacket(packets: List<Char>, headerLength: Int) =
        findStartOfPacketHelper(packets, headerLength, headerLength)

    private tailrec fun findStartOfPacketHelper(
        packets: List<Char>,
        headerLength: Int,
        currentPacketPos: Int
    ): Int {
        if (packets.isEmpty()) {
            throw IllegalArgumentException("Could not find starting header position.")
        } else if (isPacketUnique(packets.take(headerLength))) {
            return currentPacketPos
        }

        return findStartOfPacketHelper(packets.drop(1), headerLength, currentPacketPos.inc())
    }

    private fun isPacketUnique(packet: List<Char>) =
        packet.toSet().size == packet.size
}
