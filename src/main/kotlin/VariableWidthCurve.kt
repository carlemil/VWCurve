package se.kjellstrand.variablewidthline

import kotlin.math.*

fun buildHullFromPolygon(ppList: List<Triple<Float, Float, Float>>): MutableList<Triple<Float, Float, Float>> {
    val leftHull = mutableListOf<Triple<Float, Float, Float>>()
    val rightHull = mutableListOf<Triple<Float, Float, Float>>()

    for (i in 1 until ppList.size) {
        val p0 = ppList[i - 1]
        val p1 = ppList[i]

        val (a, b, c) = calculateSidesOfTriangle(p0, p1)

        val alfaPlus90 = calculateAlfa(a, c, b, (PI / 2.0).toFloat())
        val alfaMinus90 = calculateAlfa(a, c, b, -(PI / 2.0).toFloat())

        leftHull.add(calculatePerpendicularPolyPoint(p0, p1, alfaPlus90))
        rightHull.add(calculatePerpendicularPolyPoint(p0, p1, alfaMinus90))
    }

    val hull = mutableListOf<Triple<Float, Float, Float>>()
    hull.addAll(leftHull)
    hull.addAll(rightHull.reversed())
    return hull
}

private fun calculatePerpendicularPolyPoint(
    p0: Triple<Float, Float, Float>,
    p1: Triple<Float, Float, Float>,
    alfaPlus90: Float
): Triple<Float, Float, Float> {
    val width = ((p0.third + p1.third) / 2.0)
    val x = (p0.first + p1.first) / 2.0 + width * sin(alfaPlus90)
    val y = (p0.second + p1.second) / 2.0 + width * cos(alfaPlus90)
    return Triple(x.toFloat(), y.toFloat(), 1.0F)
}

private fun calculateSidesOfTriangle(
    p0: Triple<Float, Float, Float>,
    p1: Triple<Float, Float, Float>
): Triple<Float, Float, Float> {
    val a = p0.first - p1.first
    val b = p0.second - p1.second
    val c = sqrt(a.toDouble().pow(2.0) + b.toDouble().pow(2.0)).toFloat()
    return Triple(a, b, c)
}

private fun calculateAlfa(a: Float, c: Float, b: Float, angle: Float): Float {
    val alfa = asin(a / c)
    var alfaPlus90 = alfa + angle
    // Take care of special case when adjacent side is negative
    if (b < 0) alfaPlus90 = -alfaPlus90
    return alfaPlus90
}

fun getMidPoint(
    p0: Triple<Float, Float, Float>,
    p1: Triple<Float, Float, Float>
): Triple<Float, Float, Float> {
    return Triple((p0.first + p1.first) / 2.0F, (p0.second + p1.second) / 2.0F, (p0.third + p1.third) / 2.0F)
}