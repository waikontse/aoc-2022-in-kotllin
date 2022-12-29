package week3


import shared.Puzzle
import week2.Point
import java.math.BigInteger
import kotlin.math.abs

data class SensorBeacon(
    val sensorX: Int,
    val sensorY: Int,
    val beaconX: Int,
    val beaconY: Int
) {
    val manhattanDistance = abs(sensorX - beaconX) + abs(sensorY - beaconY)

    // Return set of X positions for sweeps on Y
    fun getSweepsOnY(y: Int): Set<Int> {
        // return empty set when sweep does not reach Y
        val distanceToY = abs(sensorY - y)

        if (manhattanDistance < distanceToY) {
            return setOf()
        }
        // Generate the list of X coordinates for the Y coordinate sweep
        val manhattanDistanceDiff = abs(manhattanDistance - distanceToY)
        val numberOfXs = manhattanDistanceDiff * 2 + 1

        return (sensorX-numberOfXs/2..sensorX+numberOfXs/2).toSet()
    }

    fun pointIsInRange(point: Point): Boolean {
        return (abs(sensorX-point.first) + abs(sensorY-point.second)) <= manhattanDistance
    }

    fun generatePerimeter(manhattanDistance: Int): Set<Point> {
        // Generate the
        val perimeterPoints = mutableSetOf<Point>()

        for (i in 0..manhattanDistance) {
            // generate upper part
            // left
            addPositivePoint(perimeterPoints, Point(sensorX - (manhattanDistance - i), sensorY-i))
            // right
            addPositivePoint(perimeterPoints, Point(sensorX + (manhattanDistance - i), sensorY-i))

            // generate lower part
            // right
            addPositivePoint(perimeterPoints, Point(sensorX - (manhattanDistance - i), sensorY+i))
            // left
            addPositivePoint(perimeterPoints, Point(sensorX + (manhattanDistance - i), sensorY+i))
        }

        return perimeterPoints
    }

    private fun addPositivePoint(perimeterPoints: MutableSet<Point>, elem: Point) {
        if (elem.first < 0 || elem.second < 0) {
            return
        }

        val upperLimit = 4000000
        if (elem.first > upperLimit) {
            return
        }

        if (elem.second > upperLimit) {
            return
        }

        perimeterPoints.add(elem)
    }
}
class BeaconExclusion : Puzzle(15) {
    override fun solveFirstPart(): Any {
        val ySweep = 2000000
        val parsedSensorBeacon = puzzleInput
            .map { parseSensorLine(it) }

        val sweepOnY = parsedSensorBeacon
            .map { it.getSweepsOnY(ySweep) }
            .reduce { acc, ints -> acc + ints }

        val beaconsOnY = parsedSensorBeacon.filter { it.beaconY == ySweep }
            .map { it.beaconX }
            .toSet()

        return (sweepOnY - beaconsOnY).count()
    }

    override fun solveSecondPart(): Any {
        val parsedSensorBeacon = puzzleInput
            .map { parseSensorLine(it) }

        val allPerimetersPoints = parsedSensorBeacon
            .map { it.generatePerimeter(it.manhattanDistance + 1) }
            .sortedByDescending { it.size }

        var pointOfDistressSignal: Point = Point(0,0)
        var hasFoundDistressSignal = 0
        loopOuter@ for (points in allPerimetersPoints) {
            for (p in points) {
                for (sensor in parsedSensorBeacon) {
                    if (!sensor.pointIsInRange(p)) {
                        hasFoundDistressSignal += 1
                    }
                }

                if(hasFoundDistressSignal == parsedSensorBeacon.size) {
                    pointOfDistressSignal = p
                    break@loopOuter
                }

                hasFoundDistressSignal = 0
            }
        }


        val multiplier = BigInteger.valueOf(4000000L)

        return pointOfDistressSignal.first.toBigInteger().multiply(multiplier).plus(pointOfDistressSignal.second.toBigInteger())
    }


    val sensorLineRegex = """Sensor at x=(\p{Graph}+), y=(\p{Graph}+): closest beacon is at x=(\p{Graph}+), y=(\p{Graph}+)""".toRegex()
    private fun parseSensorLine(rawPosition: String): SensorBeacon {
        val (sensorX, sensorY, beaconX, beaconY) = sensorLineRegex.matchEntire(rawPosition)
            ?.destructured
            ?: throw IllegalArgumentException(rawPosition)

        return SensorBeacon(sensorX.toInt(), sensorY.toInt(), beaconX.toInt(), beaconY.toInt())
    }
}