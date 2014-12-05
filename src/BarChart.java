import java.util.ArrayList;
import java.util.Collections;

import controlP5.ControlP5;
import processing.core.PFont;
import processing.data.*;
import processing.core.PApplet;


public class BarChart {

	Table table;
	PFont font;
	PApplet parent;
	// screen size
	int screen_width;
	int screen_height;
	// margins
	int left_margin;
	int bottom_margin;
	int right_margin;
	int top_margin;
	// number of ticks on y axis
	int y_num_ticks;
	// space between tick labels and axis
	int tick_padding;
	// chart position and size
	int chart_x;
	int chart_y;
	int chart_width;
	int chart_height;

	// unit width and height of bars
	float w;
	float h;
	// ticks on the axes
	float num_x_ticks;
	int x_tick_interval;
	int y_tick_val;
	int y_tick_h;

	//float[] bars;
	ArrayList<Float>bars;
	ControlP5 cp5;
	 
	
	BarChart(PApplet p, ControlP5 cp5) {
		this.parent = p;
		this.screen_width = 900;
		this.screen_height = 700;
		// margins
		this.left_margin = 90;
		this.bottom_margin = 20;
		this.right_margin = 10;
		this.top_margin = 30;
		// number of ticks on y axis
		this.y_num_ticks = 4;
		// space between tick labels and axis
		this.tick_padding = 5;
		this.chart_x = this.left_margin;
		this.chart_y = this.screen_height - this.bottom_margin;
		this.chart_width = this.screen_width - this.left_margin - this.right_margin;
		this.chart_height = this.screen_height - this.top_margin - this.bottom_margin;
		this.cp5 = cp5;	 
		this.bars = new ArrayList<Float>();
	}

	

	void display() {


		//bars = new float[table.getRowCount()];

		for (TableRow row : table.rows()) {

			float hits = row.getFloat(0);
			if(hits>=this.cp5.getValue(table.getColumnTitle(0))){
			bars.add(hits);
			}
		}
		
		drawAxes();
		drawBars();
		drawLabel();
		 
	}

	@SuppressWarnings("static-access")
	void drawAxes() {
		
		
		float max_number = 0;
		if(bars.size()>0){
		max_number = Collections.max(bars);
		this.w = (float) (this.chart_width) / bars.size();
		}
		this.h = 0;
		 
		if(max_number>0){
		this.h = (float) (this.chart_height) / max_number;
		}
		// ticks on y axis
		this.y_tick_val = (int)max_number / (this.y_num_ticks - 1);
		this.y_tick_h = (int) (this.y_tick_val * this.h);
		// ticks on x axis
		this.num_x_ticks = this.chart_width
				/ parent.textWidth(Integer.toString(table.getRowCount()));
		this.x_tick_interval = parent.ceil(table.getRowCount()
				/ this.num_x_ticks);
		
		// draw the axes
		parent.stroke(180, 29, 36);
		parent.line(chart_x, chart_y, chart_x + chart_width, chart_y);
		parent.line(chart_x, chart_y, chart_x, chart_y - chart_height);
		font = parent.createFont("Arial", 20);
		parent.text(table.getColumnTitle(0), chart_x, chart_y - chart_height-15);
		font = parent.createFont("Arial", 14);
		// draw y axis ticks and labels
		parent.textAlign(parent.RIGHT, parent.CENTER);
		for (int i = 0; i < y_num_ticks; i++) {
			parent.line(chart_x, chart_y - i * y_tick_h, chart_x + chart_width,
					chart_y - i * y_tick_h);
			parent.text(i * y_tick_val, chart_x - tick_padding, chart_y - i
					* y_tick_h);
		}

	}

	@SuppressWarnings("static-access")
	void drawBars() {
		// draw bars
		parent.fill(255, 255, 255);
		parent.textAlign(parent.CENTER, parent.TOP);
		for (int i = 0; i < bars.size(); i++) {
			parent.rect(this.chart_x + i * this.w, this.chart_y, this.w, -this.h * bars.get(i));
			// draw labels along x axis
			if (i % this.x_tick_interval == 0)
				parent.text(i,this.chart_x + i * this.w + this.w / 2, this.chart_y + this.tick_padding);
		}

	}

	@SuppressWarnings("static-access")
	void drawLabel() {
		// draw label
		if (parent.mouseX > this.chart_x && parent.mouseX < this.chart_x + this.chart_width) {
			 
			// find the bar that mouse pointed to
			int selectedBarIndex = (int) ((parent.mouseX - this.chart_x) / this.w);
			parent.textAlign(parent.CENTER, parent.BOTTOM);
			parent.line(parent.mouseX, this.top_margin - 5, parent.mouseX, this.chart_y
					- this.h * bars.get(selectedBarIndex) - 5);
			parent.text(bars.get(selectedBarIndex), parent.mouseX,
					this.top_margin - 10);
			parent.fill(0,161,228);
			parent.rect(this.chart_x + selectedBarIndex * this.w, this.chart_y, this.w, -this.h * bars.get(selectedBarIndex));
			parent.fill(255,255,255);
		}
	}

	public void setData(Table table) {

		this.table = table;
	}

}
