import java.util.ArrayList;

import controlP5.ControlP5;
import processing.core.PApplet;
import processing.core.PFont;
import processing.data.Table;
import processing.data.TableRow;

public class ScatterPlotMatrix {
	Table table;
	PFont font;
	PApplet parent;

	ArrayList<String> featureVals;
	ArrayList<ColumnVals> values;
	ArrayList<String> features;

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

	// unit width and height of points
	float w;
	float h;
	// ticks on the axes
	int x_num_ticks;
	int x_tick_interval;
	float x_tick_val;
	int x_tick_h;
	float y_tick_val;
	int y_tick_h;
	ControlP5 cp5;

	ScatterPlotMatrix(PApplet p, ControlP5 cp5) {
		this.parent = p;

		this.featureVals = new ArrayList<String>();
		this.features = new ArrayList<String>();
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
		this.x_num_ticks = 6;
		// space between tick labels and axis
		this.tick_padding = 5;
		this.chart_x = this.left_margin;
		this.chart_y = this.screen_height - this.bottom_margin;
		this.chart_width = this.screen_width - this.left_margin
				- this.right_margin;
		this.chart_height = this.screen_height - this.top_margin
				- this.bottom_margin;

		this.values = new ArrayList<ColumnVals>();
		this.cp5 = cp5;

	}

	void display() {

		font = parent.createFont("Arial", 14);

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

		drawAxes();
		drawPoints();
		drawLabel();
	}

	@SuppressWarnings("static-access")
	void drawAxes() {

		// for each of two columns, create a scatter plot chart
		// the first column is categories, so start at 2nd
		parent.stroke(180, 29, 36);
		int splitnum = table.getColumnCount() - 1;
		int round = 0;
		for (int m = 1; m < table.getColumnCount(); m++) {

			float xmax = parent.max(values.get(m - 1).vals);
			this.w = (float) (this.chart_width / splitnum) / xmax;

			for (int n = 1; n < table.getColumnCount(); n++) {

				float ymax = parent.max(values.get(n - 1).vals);
				this.h = (float) (this.chart_height / splitnum) / ymax;

				this.x_tick_val = xmax / (this.x_num_ticks - 1);
				this.x_tick_h = (int) (this.x_tick_val * this.w);

				this.y_tick_val = ymax / (this.y_num_ticks - 1);
				this.y_tick_h = (int) (this.y_tick_val * this.h);

				// draw x axes
				parent.line(chart_x + (chart_width / splitnum) * (m - 1),
						chart_y - chart_height * (n - 1) / splitnum, chart_x
								+ (chart_width / splitnum) * m, chart_y
								- chart_height * (n - 1) / splitnum);
				// draw y axes
				parent.line(chart_x + (chart_width / splitnum) * (m - 1),
						chart_y - chart_height * (n - 1) / splitnum, chart_x
								+ (chart_width / splitnum) * (m - 1), chart_y
								- chart_height * n / splitnum);
				parent.textAlign(parent.RIGHT, parent.CENTER);

				// draw x sticks (only bottom)
				if (n == 1) {
					if (round == 0) {

						for (int i = 0; i < x_num_ticks; i++) {
							parent.line(chart_x + (chart_width / splitnum)
									* (m - 1) + i * x_tick_h, chart_y, chart_x
									+ (chart_width / splitnum) * (m - 1) + i
									* x_tick_h, chart_y - 6);
							parent.textSize(8);
							parent.text(i * x_tick_val, chart_x
									+ (chart_width / splitnum) * (m - 1) + i
									* x_tick_h + 5, chart_y + 10);
							parent.textSize(14);
						}

					} else {

						for (int i = 1; i < x_num_ticks; i++) {
							parent.line(chart_x + (chart_width / splitnum)
									* (m - 1) + i * x_tick_h, chart_y, chart_x
									+ (chart_width / splitnum) * (m - 1) + i
									* x_tick_h, chart_y - 6);
							parent.textSize(8);
							parent.text(i * x_tick_val, chart_x
									+ (chart_width / splitnum) * (m - 1) + i
									* x_tick_h + 5, chart_y + 10);
							parent.textSize(14);
						}
					}

				}
				// draw y sticks (only left side)
				if (m == 1) {

					for (int i = 1; i < y_num_ticks; i++) {
						parent.line(chart_x, chart_y - chart_height * (n - 1)
								/ splitnum - i * y_tick_h, chart_x + 6, chart_y
								- chart_height * (n - 1) / splitnum - i
								* y_tick_h);
						parent.textSize(8);
						parent.text(i * y_tick_val, chart_x - tick_padding,
								chart_y - chart_height * (n - 1) / splitnum - i
										* y_tick_h + tick_padding);
						parent.textSize(14);
					}
				}

				if (m == n) {
					parent.textSize(8);
					parent.text(table.getColumnTitle(m), chart_x
							+ (chart_width / splitnum) * (m), chart_y
							- chart_height * n / splitnum + 10);
					parent.textSize(14);

				}

			}
			round++;
		}

	}

	@SuppressWarnings("static-access")
	void drawPoints() {
		// draw points

		parent.textAlign(parent.CENTER, parent.TOP);
		int splitnum = table.getColumnCount() - 1;

		for (int m = 1; m < table.getColumnCount(); m++) {
			float xmax = parent.max(values.get(m - 1).vals) / splitnum;
			this.w = (float) (this.chart_width / splitnum) / xmax;

			for (int n = 1; n < table.getColumnCount(); n++) {
				float ymax = parent.max(values.get(n - 1).vals) / splitnum;
				this.h = (float) (this.chart_height / splitnum) / ymax;
				for (int i = 0; i < this.values.get(m - 1).vals.length; i++) {

					int color = 0;

					if (this.featureVals.contains(this.features.get(i))) {

						color = this.featureVals.indexOf(this.features.get(i));

					}

					if (this.values.get(m - 1).vals[i] >= this.cp5
							.getValue(table.getColumnTitle(m))) {

						if (this.values.get(n - 1).vals[i] >= this.cp5
								.getValue(table.getColumnTitle(n))) {

							parent.fill(255 - color * 50,
									255 - 255 / ((color + 1) * 2),
									255 - 255 / (color + 1));
							parent.ellipse(
									(this.w
											* (this.values.get(m - 1).vals[i] / splitnum)
											+ this.left_margin + ((chart_width / splitnum) * (m - 1))),
									chart_y
											- this.h
											* (this.values.get(n - 1).vals[i] / splitnum)
											- chart_height * (n - 1) / splitnum,
									4, 4);
							parent.fill(255, 255, 255);
						}

					}

				}

			}

		}

	}

	void drawLabel() {

		for (String feature : this.featureVals) {

			int index = this.featureVals.indexOf(feature);
			parent.fill(255 - index * 50, 255 - 255 / ((index + 1) * 2),
					255 - 255 / (index + 1));
			parent.ellipse(this.screen_width + 100, (this.screen_height / 2)
					+ 100 + index * 30, 8, 8);
			parent.fill(255, 255, 255);
			parent.text(feature, this.screen_width + 150,
					(this.screen_height / 2) + 90 + index * 30);

		}

	}

	public void setData(Table table) {

		this.table = table;

	}
}
