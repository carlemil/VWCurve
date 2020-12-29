package se.kjellstrand.variablewidthline

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.GeneralPath
import kotlin.math.*

fun Graphics2D.drawVariableWidthCurve(    line: List<LinePoint>) {
    drawPolygon(buildHullFromPolygon(line), this)
}

// Add a extension for the android canvas somehow.

private fun buildHullFromPolygon(    ppList: List<LinePoint>): MutableList<LinePoint> {
    val leftHull = mutableListOf<LinePoint>()
    val rightHull = mutableListOf<LinePoint>()

    for (i in 1 until ppList.size) {
        val p0 = ppList[i - 1]
        val p1 = ppList[i]

        val (a, b, c) = calculateSidesOfTriangle(p0, p1)

        val alfaPlus90 = calculateAlfa(a, c, b, (PI / 2.0))
        val alfaMinus90 = calculateAlfa(a, c, b, -(PI / 2.0))

        leftHull.add(calculatePerpendicularPolyPoint(p0, p1,  alfaPlus90))
        rightHull.add(calculatePerpendicularPolyPoint(p0, p1,  alfaMinus90))
    }

    val hull = mutableListOf<LinePoint>()
    hull.addAll(leftHull)
    hull.addAll(rightHull.reversed())
    return hull
}

private fun drawPolygon(hull: MutableList<LinePoint>, g2: Graphics2D) {
    val path = GeneralPath()
    val polygonInitialPP = LinePoint.getMidPoint(hull[hull.size - 1], hull[hull.size - 2])
    path.moveTo(polygonInitialPP.x, polygonInitialPP.y)

    for (i in 0 until hull.size) {
        val quadStartPP = hull[(if (i == 0) hull.size else i) - 1]
        val nextQuadStartPP = hull[i]
        val quadEndPP = LinePoint.getMidPoint(quadStartPP, nextQuadStartPP)
        path.quadTo(quadStartPP.x, quadStartPP.y, quadEndPP.x, quadEndPP.y)
    }
    path.closePath()

    g2.paint = Color.BLACK

    g2.fill(path)
}

private fun calculatePerpendicularPolyPoint(
    p0: LinePoint,
    p1: LinePoint,
    alfaPlus90: Double
): LinePoint {
    val width = ((p0.w + p1.w) / 2.0)
    val x = (p0.x + p1.x) / 2.0 + width * sin(alfaPlus90)
    val y = (p0.y + p1.y) / 2.0 + width * cos(alfaPlus90)
    return LinePoint(x, y)
}

private fun calculateSidesOfTriangle(p0: LinePoint, p1: LinePoint): Triple<Double, Double, Double> {
    val a = p0.x - p1.x
    val b = p0.y - p1.y
    val c = sqrt(a.pow(2.0) + b.pow(2.0))
    return Triple(a, b, c)
}

private fun calculateAlfa(a: Double, c: Double, b: Double, angle: Double): Double {
    val alfa = asin(a / c)
    var alfaPlus90 = alfa + angle
    // Take care of special case when adjacent side is negative
    if (b < 0) alfaPlus90 = -alfaPlus90
    return alfaPlus90
}
