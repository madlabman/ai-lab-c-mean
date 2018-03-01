package sample

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.layout.VBox

import org.jfree.chart.fx.ChartViewer
import org.jfree.chart.block.BlockBorder
import org.jfree.chart.ChartFactory
import org.jfree.chart.JFreeChart
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.chart.StandardChartTheme
import org.jfree.data.xy.XYDataset
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import java.awt.Font
import java.util.*
import kotlin.collections.ArrayList


class Controller {

    @FXML
    lateinit var mainBtn: Button

    @FXML
    lateinit var centerPane: VBox


    fun initialize() {
        mainBtn.setOnAction { mainBtnClicked() }
    }

    private fun mainBtnClicked() {
        val dataset = createDataset(FCM().run())
        val chart = createChart(dataset)
        val viewer = ChartViewer(chart)

        centerPane.children.clear()
        centerPane.children.add(viewer)
    }

    private fun createChart(dataset: XYDataset): JFreeChart {
        val chart_xy = ChartFactory.createScatterPlot(
                "",
                "Признак X",
                "Признак Y",
                dataset)

        // Font styling
        val fontFamily = "Arial"
        val chartTheme = StandardChartTheme.createJFreeTheme() as StandardChartTheme

        val oldExtraLargeFont = chartTheme.extraLargeFont
        val oldLargeFont = chartTheme.largeFont
        val oldRegularFont = chartTheme.regularFont
        val oldSmallFont = chartTheme.smallFont

        val extraLargeFont = Font(fontFamily, oldExtraLargeFont.style, oldExtraLargeFont.size)
        val largeFont = Font(fontFamily, oldLargeFont.style, oldLargeFont.size)
        val regularFont = Font(fontFamily, oldRegularFont.style, oldRegularFont.size)
        val smallFont = Font(fontFamily, oldSmallFont.style, oldSmallFont.size)

        chartTheme.extraLargeFont = extraLargeFont
        chartTheme.largeFont = largeFont
        chartTheme.regularFont = regularFont
        chartTheme.smallFont = smallFont
        /*
        chartTheme.legendBackgroundPaint = darculaGREY
        chartTheme.legendItemPaint = darculaLIGHT
        chartTheme.chartBackgroundPaint = darculaGREY
        chartTheme.axisLabelPaint = darculaLIGHT
        chartTheme.tickLabelPaint = darculaLIGHT
        */
        chartTheme.apply(chart_xy)
        chart_xy.antiAlias = true
        chart_xy.legend.frame = BlockBorder.NONE

        // Plot styling
        val plot = chart_xy.xyPlot
        val renderer = XYLineAndShapeRenderer()
        // Remove lines visibility
        for (i in 0 until dataset.seriesCount) {
            renderer.setSeriesLinesVisible(i, false)
        }

        // Background
        plot.backgroundPaint = java.awt.Color(204, 204,204)
//        plot.rangeGridlinePaint = java.awt.Color.WHITE
//        plot.domainGridlinePaint = java.awt.Color.WHITE

        plot.renderer = renderer

        // Set range
        val yAxis = plot.rangeAxis
        yAxis.isAutoRange = false
        yAxis.setRange(0.0, 100.0)
        // Set domain
        val xAxis = plot.domainAxis
        xAxis.isAutoRange = false
        xAxis.setRange(0.0, 100.0)

        return chart_xy
    }

    private fun createDataset(data: ArrayList<ArrayList<Point>>): XYDataset {
        val dataset = XYSeriesCollection()

        var index = 1
        for (a in data) {
            if (a.isNotEmpty()) {
                val xySeries = XYSeries("Кластер $index")
                for (p in a) {
                    xySeries.add(p.props[0], p.props[1])
                }
                dataset.addSeries(xySeries)
                index++
            }
        }

        return dataset;
    }
}