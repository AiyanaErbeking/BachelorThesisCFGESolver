package plotting;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class TimeGraph extends JFrame {

    public TimeGraph(String title, List<Double> times) {
        super(title);

        XYSeries series = new XYSeries("Number of `Satisfiable' Answers");

        // Populate the series with data
        for (double currentTime = 0; currentTime <= 60 * 30; currentTime += 60) {
            int count = countItemsWithTimeSmallerThan(times, currentTime);
            series.add(currentTime / 60, count); // Convert seconds to minutes
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "",
                "Time (minutes)",
                "Number of 'Satisfiable' Answers",
                dataset
        );

        chart.removeLegend();

        XYPlot plot = (XYPlot) chart.getPlot();
        NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        xAxis.setTickUnit(new NumberTickUnit(5)); // Adjust tick unit as needed

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        setContentPane(chartPanel);

        // Save the chart as a PNG image locally
        saveChartAsPNG(chart, "local_chart.png");

        setContentPane(chartPanel);
    }

    private void saveChartAsPNG(JFreeChart chart, String fileName) {
        try {
            ChartUtils.saveChartAsPNG(new File(fileName), chart, 800, 600);
            System.out.println("Chart saved as " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int countItemsWithTimeSmallerThan(List<Double> times, double currentTime) {
        int count = 0;
        for (Double time : times) {
            if (time < currentTime) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            FindingAverageRuntime findingAverageRuntime = new FindingAverageRuntime();
            List<Double> times = findingAverageRuntime.getListOfAverageRuntimes();;

            TimeGraph numberOfSatAnswersVsTime = new TimeGraph("", times);
            numberOfSatAnswersVsTime.setSize(800, 600);
            numberOfSatAnswersVsTime.setLocationRelativeTo(null);
            numberOfSatAnswersVsTime.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            numberOfSatAnswersVsTime.setVisible(true);
        });
    }
}
