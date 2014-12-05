import java.util.ArrayList;
import java.util.Collections;

import controlP5.ControlP5;
import processing.core.PApplet;
import processing.core.PFont;
import processing.data.Table;
import processing.data.TableRow;

public class ScatterPlot {

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

	// space between tick labels and axis
	int tick_padding;
	// chart position and size
	int chart_x;
	int chart_y;
	int chart_width;
	int chart_height;

	// unit width and height of points
	float w;
	float h;
	// ticks on the axes
	int x_num_ticks;
	int y_num_ticks;
	int x_tick_val;
	int x_tick_h;
	int y_tick_val;
	int y_tick_h;

	 
	ArrayList<Float> xpoints;
	ArrayList<Float> ypoints;
	ControlP5 cp5;

	ScatterPlot(PApplet p, ControlP5 cp5) {
		this.parent = p;
		this.screen_width = 900;
		this.screen_height = 700;
		// margins
		this.left_margin = 90;
		this.bottom_margin = 20;
		this.right_margin = 10;
		this.top_margin = 30;
		// number of ticks on y axis
		this.y_num_ticks = 6;
		// number of ticks on x axis
		this.x_num_ticks = 7;
		// space between tick labels and axis
		this.tick_padding = 5;
		this.chart_x = this.left_margin;
		this.chart_y = this.screen_height - this.bottom_margin;
		this.chart_width = this.screen_width - this.left_margin
				- this.right_margin;
		this.chart_height = this.screen_height - this.top_margin
				- this.bottom_margin;

		this.cp5 = cp5;
		
		xpoints = new ArrayList<Float>();
		ypoints = new ArrayList<Float>();

	}

	void display() {

		font = parent.createFont("Arial", 14);
		
		for (TableRow row : table.rows()) {

			float x = row.getFloat(0);
			float y = row.getFloat(1);
			if (x >= this.cp5.getValue(table.getColumnTitle(0))) {
				
				if( y >= this.cp5.getValue(table.getColumnTitle(1))){
				xpoints.add(x);
			    ypoints.add(y);
			    
				}
			}	 
		}

		drawAxes();
		drawPoints();
		drawLabel();
	}

	@SuppressWarnings("static-access")
	void drawAxes() {
		float ymax = 0;
		float xmax = 0;
		if(xpoints.size()>0){
		ymax = Collections.max(ypoints);
		xmax = Collections.max(xpoints);
		} 
		this.h = 0;
		this.w = 0;
		if(xmax>0&&ymax>0){
		this.h = (float) (this.chart_height) / ymax;
		this.w = (float) (this.chart_width) / xmax;
		}
		// ticks on y axis
		this.y_tick_val = (int) ymax / (this.y_num_ticks - 1);
		this.y_tick_h = (int) (this.y_tick_val * this.h);
		// ticks on x axis
		this.x_tick_val = (int) xmax / (this.x_num_ticks - 1);
		this.x_tick_h = (int) (this.x_tick_val * this.w);

		// draw the axes
		parent.stroke(180, 29, 36);
		parent.line(chart_x, chart_y, chart_x + chart_width, chart_y);
		parent.text(table.getColumnTitle(0), chart_x + chart_width,
				chart_y + 15);

		parent.line(chart_x, chart_y, chart_x, chart_y - chart_height);
		parent.text(table.getColumnTitle(1), chart_x + 100, chart_y
				- chart_height - 15);

		// draw y axis ticks and labels
		parent.textAlign(parent.RIGHT, parent.CENTER);
		for (int i = 1; i < y_num_ticks; i++) {
			parent.line(chart_x, chart_y - i * y_tick_h, chart_x + 10, chart_y
					- i * y_tick_h);
			parent.text(i * y_tick_val, chart_x - tick_padding, chart_y - i
					* y_tick_h);
		}

		// draw x axis ticks and labels
		for (int i = 0; i < x_num_ticks; i++) {
			parent.line(chart_x + i * x_tick_h, chart_y,
					chart_x + i * x_tick_h, chart_y - 10);
			parent.text(i * x_tick_val, chart_x + i * x_tick_h + 5,
					chart_y + 10);
		}

	}

	@SuppressWarnings("static-access")
	void drawPoints() {
		// draw points
		parent.fill(255, 255, 255);
		parent.textAlign(parent.CENTER, parent.TOP);
		if(xpoints.size()>0){
		for (int i = 0; i < xpoints.size(); i++) {
			parent.ellipse((this.w * xpoints.get(i) + this.left_margin), chart_y
					- this.h * ypoints.get(i), 10, 10);

		}
		}

	}

	void drawLabel() {
		// draw label

		for (int i = 0; i < xpoints.size(); i++) {
			parent.ellipse((this.w * xpoints.get(i) + this.left_margin), chart_y
					- this.h * ypoints.get(i), 10, 10);
			float xdist = this.w * xpoints.get(i) + this.left_margin
					- parent.mouseX;
			float ydist = (chart_y - this.h * ypoints.get(i)) - parent.mouseY;
			if (xdist * xdist + ydist * ydist <= 100) {
				parent.fill(0, 161, 228);
				parent.ellipse((this.w * xpoints.get(i) + this.left_margin),
						chart_y - this.h * ypoints.get(i), 10, 10);
				parent.fill(255, 255, 255);
				parent.text("(" + xpoints.get(i) + ", ", parent.mouseX + 50,
						parent.mouseY - 30);
				parent.text(ypoints.get(i) + ")", parent.mouseX + 85,
						parent.mouseY - 30);
				// draw x label
				parent.line(this.left_margin, chart_y - this.h * ypoints.get(i),
						this.w * xpoints.get(i) + this.left_margin, chart_y
								- this.h * ypoints.get(i));
				parent.line(this.w * xpoints.get(i) + this.left_margin, chart_y
						- this.h * ypoints.get(i), this.w * xpoints.get(i)
						+ this.left_margin, (float) chart_y);
				break;
			}

		}

	}

	public void setData(Table table) {

		this.table = table;

	}

}
