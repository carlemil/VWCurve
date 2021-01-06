import org.junit.jupiter.api.Test
import se.kjellstrand.variablewidthline.buildHullFromPolygon
import se.kjellstrand.variablewidthline.getMidPoint
import java.awt.*
import java.awt.geom.GeneralPath
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class CurveTest {
    @Test
    internal fun drawAVWCurve() {

        val line = mutableListOf(
            Triple(0.1810875895988005, 0.8504400374531756, 0.4921568393707275),
            Triple(0.2070683517123337, 0.8354400374531754, 0.2568393707275),
            Triple(0.2503696219015556, 0.8104400374531755, 0.0784313678741455),
            Triple(0.3109914001664663, 0.7754400374531756, 0.08235293626785278),
            Triple(0.3889336865070658, 0.7304400374531755, 0.8352941125631332),
            Triple(0.4668759728476653, 0.6954400374531757, 0.6235294044017792),
            Triple(0.5274977511125759, 0.6904400374531756, 0.4823529124259949),
            Triple(0.5707990213017979, 0.7154400374531756, 0.31372547149658203),
            Triple(0.5967797834153311, 0.7704400374531757, 0.15294116735458374),
            Triple(0.6054400374531754, 0.8554400374531755, 0.937254898250103),
            Triple(0.6141002914910199, 0.9404400374531757, 0.6588235199451447),
            Triple(0.6400810536045529, 0.9954400374531754, 0.831372544169426),
            Triple(0.6833823237937748, 1.0000000000000000, 1.0000000000000000),
            Triple(0.7440041020586856, 1.0000000000000000, 0.8509803861379623),
            Triple(0.8219463883992850, 0.9804400374531755, 0.7843137234449387),
            Triple(0.8912284207020403, 0.9304400374531757, 0.18823528289794922),
            Triple(1.0000000000000000, 1.0000000000000000, 1.0000000000000000),
            Triple(0.9258694368534177, 0.8304400374531755, 0.13333332538604736),
            Triple(0.8912284207020402, 0.7804400374531757, 1.0000000000000000),
            Triple(0.8219463883992851, 0.7304400374531757, 0.9176470562815666),
            Triple(0.7526643560965303, 0.6804400374531757, 0.8509803861379623),
            Triple(0.7180233399451526, 0.6304400374531756, 0.6156862676143646),
            Triple(0.7180233399451526, 0.5804400374531755, 0.45490193367004395),
            Triple(0.7526643560965302, 0.5304400374531756, 0.6196078360080719),
            Triple(0.8219463883992851, 0.4804400374531755, 0.8274509757757187),
            Triple(0.8912284207020404, 0.43044003745317555, 0.5725490152835846),
            Triple(0.9258694368534177, 0.3804400374531755, 0.0),
            Triple(0.9258694368534177, 0.33044003745317546, 0.0156862735748291),
            Triple(0.8912284207020402, 0.2804400374531755, 0.019607841968536377),
            Triple(0.8219463883992851, 0.23044003745317543, 0.003921568393707275),
            Triple(0.7440041020586858, 0.0019544003745317545, 0.011764705181121826),
            Triple(0.6833823237937751, 0.0004400374531754, 0.011764705181121826),
            Triple(0.6400810536045531, 0.21544003745317536, 0.003921568393707275),
            Triple(0.6141002914910201, 0.27044003745317535, 0.7450980246067047),
            Triple(0.6054400374531758, 0.35544003745317526, 0.803921565413475),
            Triple(0.5967797834153314, 0.4404400374531753, 0.11372548341751099),
            Triple(0.5707990213017983, 0.49544003745317516, 0.760784313082695),
            Triple(0.5274977511125765, 0.5204400374531752, 0.4627450704574585),
            Triple(0.4668759728476657, 0.5154400374531751, 0.4627450704574585),
            Triple(0.3889336865070663, 0.4804400374531751, 0.7843137234449387),
            Triple(0.3109914001664670, 0.4454400374531751, 0.0313725471496582),
            Triple(0.2503696219015562, 0.44044003745317506, 0.011764705181121826),
            Triple(0.2070683517123344, 0.465440037453175, 0.011764705181121826),
            Triple(0.1810875895988013, 0.520440037453175, 0.011764705181121826),
            Triple(0.1724273355609570, 0.605440037453175, 0.0156862735748291),
            Triple(0.1724273355609570, 0.6954400374531751, 0.00784313678741455),
            Triple(0.0172427335560950, 0.7654400374531749, 0.00784313678741455),
            Triple(0.000042733556091, 0.815440037453175, 0.003921568393707275),
            Triple(0.1724273355609571, 0.845440037453175, 0.003921568393707275),
        )

        val r = Rectangle(20, 20, 600, 600)
        val minWidth = 2.0
        val maxWidth = 60.0
        val scaledLine = line.map { p ->
            Triple(
                (r.getX() + p.first.times(r.width)).toFloat(),
                (r.getY() + p.second.times(r.height)).toFloat(),
                (p.third * (maxWidth - minWidth) / 2.0 + minWidth / 2.0).toFloat()
            )
        }

        val vwLine = buildHullFromPolygon(scaledLine)

        val (bufferedImage, g2) = setupGraphics(640)

        drawPolygon(vwLine, g2)

        tearDownGraphics(g2)

        writeImageToPngFile(bufferedImage, "LineTestOutput.png")
    }

    private fun drawPolygon(hull: List<Triple<Float,Float,Float>>, g2: Graphics2D) {
        val path = GeneralPath()
        val polygonInitialPP = getMidPoint(hull[hull.size - 1], hull[hull.size - 2])
        path.moveTo(polygonInitialPP.first, polygonInitialPP.second)

        for (i in hull.indices) {
            val quadStartPP = hull[(if (i == 0) hull.size else i) - 1]
            val nextQuadStartPP = hull[i]
            val quadEndPP = getMidPoint(quadStartPP, nextQuadStartPP)
            path.quadTo(quadStartPP.first, quadStartPP.second, quadEndPP.first, quadEndPP.second)
        }
        path.closePath()

        g2.paint = Color.BLACK

        g2.fill(path)
    }

    private fun tearDownGraphics(g2: Graphics2D) {
        g2.dispose()
    }

    private fun setupGraphics(size: Int): Pair<BufferedImage, Graphics2D> {
        val bufferedImage = BufferedImage(size, size, BufferedImage.TYPE_INT_RGB)

        val g2 = bufferedImage.createGraphics()
        val rh = mutableMapOf<RenderingHints.Key, Any>()
        rh[RenderingHints.KEY_ANTIALIASING] = RenderingHints.VALUE_ANTIALIAS_ON
        rh[RenderingHints.KEY_ALPHA_INTERPOLATION] = RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY
        rh[RenderingHints.KEY_COLOR_RENDERING] = RenderingHints.VALUE_COLOR_RENDER_QUALITY
        rh[RenderingHints.KEY_RENDERING] = RenderingHints.VALUE_RENDER_QUALITY
        rh[RenderingHints.KEY_STROKE_CONTROL] = RenderingHints.VALUE_STROKE_PURE
        g2.setRenderingHints(rh)

        g2.stroke = BasicStroke(2f)
        g2.color = Color.WHITE
        g2.fill(Rectangle(0, 0, bufferedImage.width, bufferedImage.height))
        return Pair(bufferedImage, g2)
    }

    private fun writeImageToPngFile(bufferedImage: BufferedImage, pngFileName: String) {
        val file = File(pngFileName)
        ImageIO.write(bufferedImage, "png", file)
    }
}