package plotting;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelSizeFrequencyGraph extends JFrame {

    public ModelSizeFrequencyGraph(String title, List<Integer> numbers) {
        super(title);

        CategoryDataset dataset = createDataset(numbers);

        JFreeChart chart = ChartFactory.createBarChart(
                "",
                "Model Size",
                "Frequency",
                dataset
        );

        chart.removeLegend();

        // Customize colors of the bars
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setShadowVisible(false);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        setContentPane(chartPanel);

        saveChartAsPNG(chart, "modelSizeFrequencyChart.png");
    }

    private void saveChartAsPNG(JFreeChart chart, String fileName) {
        try {
            ChartUtils.saveChartAsPNG(new File(fileName), chart, 800, 600);
            System.out.println("Chart saved as " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CategoryDataset createDataset(List<Integer> numbers) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Count the frequency of each number
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int number : numbers) {
            frequencyMap.put(number, frequencyMap.getOrDefault(number, 0) + 1);
        }

        // Add data to the dataset
        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            dataset.addValue(entry.getValue(), "Frequency", String.valueOf(entry.getKey()));
        }

        return dataset;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            FindingAverageRuntime findingAverageRuntime = new FindingAverageRuntime();
            List<Integer> modelSizes = findingAverageRuntime.getListOfModelSizes();

            ModelSizeFrequencyGraph example = new ModelSizeFrequencyGraph("Number Frequency Chart", modelSizes);
            example.setSize(800, 600);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
    }
}
