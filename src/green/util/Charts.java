package green.util;

import green.core.Constant;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

public class Charts extends ApplicationFrame {
	public Charts(final String title, List<Integer> slotNewTaskList,
			List<Integer> slotFinishedTaskList, List<Float> slotEnergy,
			List<Float> slotGreenEnergy, List<Float> slotPrice, List<Float> priceList) {
		super(title);
		JPanel panel = new JPanel(new GridLayout(2, 2));
		XYDataset loadDataset = createLoadDataset(slotNewTaskList,
				slotFinishedTaskList);
		JFreeChart loadChart = createLoadChart(loadDataset);
		ChartPanel loadChartPanel = new ChartPanel(loadChart);

		XYDataset energyDataset = createEnergyDataset(slotEnergy, slotGreenEnergy);
		JFreeChart energyChart = createEnergyChart(energyDataset);
		ChartPanel energyChartPanel = new ChartPanel(energyChart);
		
		XYDataset priceDataset = createPriceDataset(slotPrice,priceList);
		JFreeChart priceChart = createPriceChart(priceDataset);
		ChartPanel priceChartPanel = new ChartPanel(priceChart);
		
		XYDataset relativeDataset = createRelativeDataset(slotFinishedTaskList,slotGreenEnergy,priceList);
		JFreeChart relativeChart = createRelativeChart(relativeDataset);
		ChartPanel relativeChartPanel = new ChartPanel(relativeChart);
		
		panel.add(priceChartPanel);
		panel.add(energyChartPanel);
		panel.add(loadChartPanel);
		panel.add(relativeChartPanel);
		setContentPane(panel);
	}

	private XYDataset createEnergyDataset(List<Float> slotEnergy,
			List<Float> slotGreenEnergy) {
		// TODO Auto-generated method stub
		final TimeSeries series = new TimeSeries("slotEnergyConsume");
		final TimeSeries series1 = new TimeSeries("slotGreenEnergy");
		Minute current = new Minute();
		for (int i = 0; i < slotEnergy.size(); i++) {
			try {
				/*
				 * int core = (int)(-7 + Math.random()*15); value = (int) (value
				 * + Math.random() * core);
				 */
				series.add(current, slotEnergy.get(i));
				series1.add(current, slotGreenEnergy.get(i));
				for (int j = 0; j < (Constant.SCHEDULE_INTERNAL / 60); j++) {
					current = (Minute) current.next();
				}
			} catch (SeriesException e) {
				System.err.println("Error adding to series");
			}
		}
		TimeSeriesCollection tc = new TimeSeriesCollection(series);
		tc.addSeries(series1);
		return tc;
	}

	private XYDataset createLoadDataset(List<Integer> slotNewTaskList,
			List<Integer> slotFinishedTaskList) {
		final TimeSeries series = new TimeSeries("slotNewTask");
		final TimeSeries series1 = new TimeSeries("slotFinishedTask");
		Minute current = new Minute();
		for (int i = 0; i < slotNewTaskList.size(); i++) {
			try {
				/*
				 * int core = (int)(-7 + Math.random()*15); value = (int) (value
				 * + Math.random() * core);
				 */
				series.add(current, slotNewTaskList.get(i));
				series1.add(current, slotFinishedTaskList.get(i));
				for (int j = 0; j < (Constant.SCHEDULE_INTERNAL / 60); j++) {
					current = (Minute) current.next();
				}
			} catch (SeriesException e) {
				System.err.println("Error adding to series");
			}
		}
		TimeSeriesCollection tc = new TimeSeriesCollection(series);
		tc.addSeries(series1);
		return tc;
	}

	private XYDataset createPriceDataset(List<Float> slotPrice, List<Float> priceList) {
		final TimeSeries series = new TimeSeries("slotPrice");
		final TimeSeries series1 = new TimeSeries("Price");
		Minute current = new Minute();
		for (int i = 0; i < slotPrice.size(); i++) {
			try {
				/*
				 * int core = (int)(-7 + Math.random()*15); value = (int) (value
				 * + Math.random() * core);
				 */
				series.add(current, slotPrice.get(i));
				series1.add(current,priceList.get(i));
				for (int j = 0; j < (Constant.SCHEDULE_INTERNAL / 60); j++) {
					current = (Minute) current.next();
				}
			} catch (SeriesException e) {
				System.err.println("Error adding to series");
			}
		}
		TimeSeriesCollection tc = new TimeSeriesCollection(series);
		tc.addSeries(series1);
		return tc;
	}
	private XYDataset createRelativeDataset(List<Integer> slotFinishedTaskList,List<Float> slotGreenEnergy, List<Float> priceList) {
		final TimeSeries series = new TimeSeries("slotFinishedTask");
		final TimeSeries series1 = new TimeSeries("slotGreenEnergy");
		final TimeSeries series2 = new TimeSeries("Price"); 
		Minute current = new Minute();
		for (int i = 0; i < slotFinishedTaskList.size(); i++) {
			try {
				/*
				 * int core = (int)(-7 + Math.random()*15); value = (int) (value
				 * + Math.random() * core);
				 */
				series.add(current, slotFinishedTaskList.get(i) / 100.0); //为了反映相关性，进行一定缩放
				series1.add(current,slotGreenEnergy.get(i));
				series2.add(current,priceList.get(i) / 10.0);
				for (int j = 0; j < (Constant.SCHEDULE_INTERNAL / 60); j++) {
					current = (Minute) current.next();
				}
			} catch (SeriesException e) {
				System.err.println("Error adding to series");
			}
		}
		TimeSeriesCollection tc = new TimeSeriesCollection(series);
		tc.addSeries(series1);
		tc.addSeries(series2);
		return tc;
	}
	
	private JFreeChart createLoadChart(final XYDataset dataset) {
		return ChartFactory.createTimeSeriesChart("Load View", "Time(h)",
				"TaskNum", dataset, true, true, false);
	}
	private JFreeChart createEnergyChart(final XYDataset dataset) {
		return ChartFactory.createTimeSeriesChart("Energy View", "Time(h)",
				"Energy(kWh)", dataset, true, true, false);
	}
	private JFreeChart createPriceChart(final XYDataset dataset) {
		return ChartFactory.createTimeSeriesChart("Price View", "Time(h)",
				"Price(cents)", dataset, true, true, false);
	}
	private JFreeChart createRelativeChart(final XYDataset dataset) {
		return ChartFactory.createTimeSeriesChart("Relative View", "Time(h)",
				"", dataset, true, true, false);
	}
}