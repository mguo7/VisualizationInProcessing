import java.io.File;
import java.util.ArrayList;

import processing.core.*;
import processing.data.*;
import controlP5.*;

@SuppressWarnings("serial")
public class MyProcessingSketch extends PApplet {

	Table table;
	int sliderValue = 100;
	Slider abc;
	ControlP5 cp5;

	public void setup() {
		size(1200, 800);
		table = new Table();
		selectInput("Select a file to process:", "fileSelected");
		cp5 = new ControlP5(this);

	}

	@SuppressWarnings("static-access")
	public void fileSelected(File selection) {
		if (selection == null) {
			println("Window was closed or the user hit cancel.");
		} else {
			table = loadTable(selection.getAbsolutePath(), "header");
			if (table.getColumnCount() == 1) {
				float[] bars = new float[table.getRowCount()];
				int index = 0;
				for (TableRow row : table.rows()) {

					float hits = row.getFloat(0);
					bars[index] = hits;
					index++;
				}
				float max = this.max(bars);
				float min = this.min(bars);
				cp5.addSlider(table.getColumnTitle(0)).setPosition(950, 150)
						.setRange(min, max)
						.setSize(200,20)
				        .setValue(min);
				 
			} else if (table.getColumnCount() == 2){
				
				float[]xpoints = new float[table.getRowCount()];
				float[]ypoints = new float[table.getRowCount()];

				int index = 0;
				for (TableRow row : table.rows()) {

					float x = row.getFloat(0);
					xpoints[index] = x;
					float y = row.getFloat(1);
					ypoints[index] = y;
					index++;
				}
				
				float xmax = this.max(xpoints);
				float xmin = this.min(xpoints);
				cp5.addSlider(table.getColumnTitle(0)).setPosition(950, 150)
				.setRange(xmin, xmax)
				.setSize(120,20)
		        .setValue(xmin);
				
				float ymax = this.max(ypoints);
				float ymin = this.min(ypoints);
				cp5.addSlider(table.getColumnTitle(1)).setPosition(950, 180)
				.setRange(ymin, ymax)
				.setSize(120,20)
		        .setValue(ymin);
			} else {
				
				ArrayList<String>featureVals = new ArrayList<String>();
				ArrayList<String>features = new ArrayList<String>();
				ArrayList<ColumnVals> values = new ArrayList<ColumnVals>();
				
				for (int col = 0; col < table.getColumnCount(); col++) {
					ColumnVals colvals = new ColumnVals(table.getRowCount());
					int index = 0;
					for (TableRow row : table.rows()) {

						if (col == 0) {
							if (!featureVals.contains(row.getString(0))) {
								featureVals.add(row.getString(0));
							}
							features.add(row.getString(0));
						} else {

							colvals.addElement(row.getFloat(col), index);
						}
						index++;
					}
					if (col > 0) {
						values.add(colvals);
					}

				} 
				 
				for(int col = 0; col < table.getColumnCount()-1; col++){
					float max = max(values.get(col).vals);
					float min = min(values.get(col).vals);
					cp5.addSlider(table.getColumnTitle(col+1)).setPosition(950, 90+30*(col-1))
					.setRange(min, max)
					.setSize(120,20)
			        .setValue(min);
					
				}
				
			}
		}
	}

	public void draw() {
		background(130, 130, 130);
		
		// A bar chart for one-dimensional data

		if (table.getColumnCount() == 1) {
			 
			BarChart bar = new BarChart(this,this.cp5);
			bar.setData(table);
			bar.display();

			// Scatter Plot for two-dimensional data
		} else if (table.getColumnCount() == 2) {

			ScatterPlot splot = new ScatterPlot(this,this.cp5);
			splot.setData(table);
			splot.display();
			 

			// Scatter Plot Matrix for 3-dimensional or above data
		} else if (table.getColumnCount() > 2) {
			
			ScatterPlotMatrix matrix = new ScatterPlotMatrix(this,this.cp5);
			matrix.setData(table);
			matrix.display();
			

		}

	}
	public ControlP5 getController(){
		
		return this.cp5;
	}

}
