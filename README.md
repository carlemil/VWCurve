# VWCurve

A kotlin lib for drawing long splines with varying width.
![N|Solid](https://github.com/carlemil/VWCurve/blob/main/LineTestOutput.png?raw=true)

The above image was drawn using this library running the LineTest.kt test class and hopefully illustrates what this lib does in a simple way.

To draw on a awt Graphics2d object, this could be used:

```kotlin
fun drawPolygon(hull: MutableList<LinePoint>, g2: Graphics2D) {
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
```
Latest release is found here: https://jitpack.io/#carlemil/VWLine/
