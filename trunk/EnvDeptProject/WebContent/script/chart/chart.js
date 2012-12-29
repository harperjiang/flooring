function Margin(top, left, bottom, right) {
	this.top = top;
	this.left = left;
	this.bottom = bottom;
	this.right = right;
};

function MouseEvent(x, y) {
	this.point = new Point(x, y);
};

NamespaceUtils.set("chart.selected", undefined);

function switchSelected(newsel) {
	if (chart.selected !== undefined) {
		chart.selected.selected = false;
		chart.selected.redraw();
	}
	if (newsel !== undefined) {
		chart.selected = newsel;
		newsel.selected = true;
		newsel.redraw();
	}
}

Component = Class.extend({
	construct : function(origin, size) {
		this.origin = origin;
		this.size = size;

		this.eventlisteners = new Array();

		this.selected = false;
	},
	draw : function(context) {
		context.save();
		context.rect(this.origin.x, this.origin.y, this.size.width,
				this.size.height);
		context.clip();
		// Translate Coordinate
		context.translate(this.origin.x, this.origin.y);
		this.paint(context);
		context.restore();
	},
	paint : function(context) {
	},
	addEventListener : function(eventlistener) {
		this.eventlisteners.push(eventlistener);
	},
	redraw : function() {
		if (this.parent !== undefined)
			this.parent.redraw2(this.origin, this.size);
	},
	redraw2 : function(origin, size) {
	},
	layoutData : null,
	onMouseClick : function(event) {

	},
	onMouseMove : function(event) {

	},
	onMouseIn : function(event) {
		switchSelected(this);
	},
	onMouseOut : function(event) {
		switchSelected(undefined);
	}
});

Container = Component.extend({
	construct : function(origin, size) {
		arguments.callee.$.construct.call(this, origin, size);
		this.children = new Array();
		this.mouseIn = undefined;
	},
	addChild : function(child) {
		child.parent = this;
		child.seq = this.children.length;
		this.children.push(child);
	},
	removeChild : function(index) {
		this.children[index].parent = undefined;
		this.children.splice(index, 1);
	},
	paint : function(context) {
		this.doLayout();
		context.save();
		this.paintBackground(context);
		context.restore();
		for ( var i = 0; i < this.children.length; i++) {
			this.children[i].draw(context);
		}
	},
	paintBackground : function(context) {

	},
	doLayout : function() {
		if (this.layout != null)
			this.layout.doLayout(this);
	},
	redraw2 : function(origin, size) {
		this.parent.redraw2(new Point(this.origin.x + origin.x, this.origin.y
				+ origin.y), size);
	},
	onMouseClick : function(event) {
		var child = this.getChild(event);
		if (child) {
			child.onMouseClick(new Point(event.x - child.origin.x, event.y
					- child.origin.y));
		}
	},
	onMouseMove : function(event) {
		var newChild = this.getChild(event);
		if (newChild !== this.mouseIn) {
			if (this.mouseIn !== undefined) {
				this.mouseIn.onMouseOut(event);
			}
			this.mouseIn = newChild;
			if (newChild !== undefined) {
				newChild.onMouseIn(event);
			}
		}
	},
	onMouseIn : function(event) {
		var child = this.getChild(event);
		if (child !== undefined)
			child.onMouseIn(event);
	},
	onMouseOut : function(event) {
		var child = this.mouseIn;
		if (child !== undefined)
			child.onMouseOut(event);
	},
	getChild : function(point) {
		for ( var i = 0; i < this.children.length; i++) {
			if (this.children[i].origin.isin(this.children[i].size, point)) {
				return this.children[i];
			}
		}
		return undefined;
	}
});

XYCoordinate = Container
		.extend({
			construct : function(dimension) {
				arguments.callee.$.construct.call(this, new Point(0, 0),
						dimension);
				this.margin = new Margin(20, 40, 40, 20);
				this.area = new Dimension(this.size.width - this.margin.left
						- this.margin.right, this.size.height - this.margin.top
						- this.margin.bottom);
				this.title = "Distribution";
				this.rangex = null;
				this.rangey = null;
			},
			paintBackground : function(context) {
				context.lineWidth = "2";
				context.strokeStyle = "red";
				// Leave some space for legends and text
				var arrowlen = 8;
				context.beginPath();
				context.moveTo(this.margin.left, this.margin.top - arrowlen);
				context.lineTo(this.margin.left, this.size.height
						- this.margin.bottom);
				context.lineTo(this.size.width - this.margin.right + arrowlen,
						this.size.height - this.margin.bottom);

				context.stroke();

				// Draw Arrows
				context.beginPath();

				var yendpoint = new Point(this.margin.left, this.margin.top
						- arrowlen);
				yendpoint.x -= 4;
				yendpoint.y += 4;
				context.moveTo(yendpoint.x, yendpoint.y);
				context.lineTo(yendpoint.x + 4, yendpoint.y - 4);
				context.lineTo(yendpoint.x + 8, yendpoint.y);

				var endpoint = new Point(this.size.width - this.margin.right
						+ arrowlen, this.size.height - this.margin.bottom);
				endpoint.x = endpoint.x - 4;
				endpoint.y = endpoint.y - 4;
				context.moveTo(endpoint.x, endpoint.y);
				context.lineTo(endpoint.x + 4, endpoint.y + 4);
				context.lineTo(endpoint.x, endpoint.y + 8);
				context.stroke();

				// Draw the grids and texts
				context.lineWidth = "1";
				context.strokeStyle = "#dddddd";

				// Decide the step
				var stepx = (this.size.width - this.margin.left - this.margin.right)
						/ this.rangex.steps();
				var stepy = (this.size.height - this.margin.top - this.margin.bottom)
						/ this.rangey.steps();
				var gridheight = stepy * (this.rangey.steps());
				var gridwidth = stepx * (this.rangex.steps());
				for ( var i = 0; i <= this.rangex.steps(); i++) {
					if (i > 0) {
						context.beginPath();
						context.moveTo(this.margin.left + stepx * i,
								this.size.height - this.margin.bottom
										- gridheight);
						context.lineTo(this.margin.left + stepx * i,
								this.size.height - this.margin.bottom);
						context.stroke();
					}
					context.save();
					// Rect for text
					context.beginPath();
					context.font = "10px Sans-Serif";
					context.rect(this.margin.left + stepx * i, this.size.height
							- this.margin.bottom, 34, 15);
					context.clip();
					context.fillText(this.rangex.label(i), this.margin.left
							+ stepx * i, this.size.height - this.margin.bottom
							+ 15);
					context.closePath();
					context.restore();
				}
				for ( var i = 0; i <= this.rangey.steps(); i++) {
					var y = this.size.height - this.margin.bottom - i * stepy;
					if (i != 0) {
						context.beginPath();
						context.moveTo(this.margin.left, y);
						context.lineTo(this.margin.left + gridwidth, y);
						context.stroke();
					}
					context.save();
					// Rect for text
					context.beginPath();
					context.font = "10px Sans-Serif";
					context.rect(this.margin.left - 35, y - 15, 34, 15);
					context.clip();
					context.fillText(this.rangey.label(i),
							this.margin.left - 35, y);
					context.closePath();
					context.restore();
				}

				// Text Title
				context.font = "16px Sans-Serif";
				context.fillText(this.title, this.size.width / 2,
						this.size.height - this.margin.bottom / 2 + 16);
			},
			redraw2 : function(origin, size) {
				this.chart.redrawRegion(origin, size);
			}
		});

function translen(scale, height, min, value) {
	return parseInt(height - scale * (value - min));
}

Record = Component.extend({

	construct : function(origin, size) {
		arguments.callee.$.construct.call(this, origin, size);
	},
	paint : function(context) {
		var recdata = this.layoutData;
		var scale = recdata.heightScale;
		var pos75 = translen(scale, this.size.height, recdata.min,
				recdata.val75);
		var pos25 = translen(scale, this.size.height, recdata.min,
				recdata.val25);
		var posmid = translen(scale, this.size.height, recdata.min,
				recdata.median);
		var ycenter = this.size.width / 4;
		// if (this.selected) {
		// context.fillStyle = "#dddddd";
		// } else {
		// context.fillStyle = "red";
		// }
		// context.fillRect(0, 0, this.size.width, this.size.height);

		context.fillStyle = "#00ffdd";
		context.fillRect(0, pos75, this.size.width / 2, pos25 - pos75);
		context.strokeStyle = "black";
		context.lineWidth = 2;
		context.beginPath();

		context.moveTo(ycenter - 6, 0);
		context.lineTo(ycenter + 6, 0);
		context.moveTo(ycenter, 0);
		context.lineTo(ycenter, this.size.height);
		context.moveTo(ycenter - 6, this.size.height);
		context.lineTo(ycenter + 6, this.size.height);

		context.moveTo(0, posmid);
		context.lineTo(this.size.width / 2, posmid);

		context.stroke();

		// Draw text
		if (this.selected) {
			context.fillStyle = "black";
			context.font = "8px Sans-Serif";
			context.fillText(recdata.max, ycenter * 2, 10);
			context.fillText(recdata.val75, ycenter * 2, pos75 + 10);
			context.fillText(recdata.val25, ycenter * 2, pos25 + 10);
			context.fillText(recdata.median, ycenter * 2, posmid + 10);
			context.fillText(recdata.min, ycenter * 2, this.size.height);
		}
	}
});

function Chart(canvas) {
	this.canvas = canvas;
	this.coordinate = new XYCoordinate(new Dimension(this.canvas.width,
			this.canvas.height));
	this.coordinate.layout = this;
	this.coordinate.chart = this;
	this.canvas["chart_Chart"] = this;
	this.canvas.addEventListener("mousemove", Chart.prototype.onMouseMove,
			false);
	this.canvas.addEventListener("click", Chart.prototype.onMouseClick, false);
};

Chart.prototype.addRecord = function(record_data) {
	var record = new Record();
	record.layoutData = record_data;
	this.coordinate.addChild(record);
};

Chart.prototype.removeRecord = function(index) {
	this.coordinate.removeChild(index);
};

Chart.prototype.redraw = function() {
	// Do some drawing
	var ctx = this.canvas.getContext("2d");
	ctx.save();
	ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
	this.coordinate.draw(ctx);
	ctx.restore();
};

Chart.prototype.redrawRegion = function(origin, size) {
	// Redraw the region
	var ctx = this.canvas.getContext("2d");
	ctx.save();
	ctx.clearRect(origin.x, origin.y, size.width, size.height);
	ctx.rect(origin.x, origin.y, size.width, size.height);
	ctx.clip();
	this.coordinate.draw(ctx);
	ctx.restore();
};

Chart.prototype.doLayout = function(coordinate) {
	// Update Coordinate Info
	this.coordinate.rangex = new Range(0, 0);
	this.coordinate.rangey = new Range(0, 20);

	var xcount = coordinate.children.length;
	if (xcount < 10)
		xcount = 10;
	this.coordinate.rangex.max = xcount;

	var range = new Range(Number.MAX_VALUE, -1);
	for ( var i = 0; i < coordinate.children.length; i++) {
		var record = coordinate.children[i];
		data = record.layoutData;
		range.min = Math.min(range.min, data.min);
		range.max = Math.max(range.max, data.max);
	}
	if (range.max != -1) {
		range.shrink();
		coordinate.rangey = range;
	}

	// Record Width is constant according to step
	var stepx = (coordinate.size.width - coordinate.margin.left - coordinate.margin.right)
			/ coordinate.rangex.steps();
	var stepy = (coordinate.size.height - coordinate.margin.top - coordinate.margin.bottom)
			/ coordinate.rangey.steps();

	for ( var i = 0; i < this.coordinate.children.length; i++) {
		var record = coordinate.children[i];
		var recwidth = stepx * 0.6;
		var recleftwidth = stepx * 0.3 / 2;
		var heightScale = stepy / coordinate.rangey.step;

		var top = translen(heightScale, this.canvas.height
				- coordinate.margin.bottom, coordinate.rangey.min,
				record.layoutData.max);
		var bottom = translen(heightScale, this.canvas.height
				- coordinate.margin.bottom, coordinate.rangey.min,
				record.layoutData.min);
		record.origin = new Point(coordinate.margin.left + (i + 1) * stepx
				- recleftwidth, top);
		record.size = new Dimension(recwidth, bottom - top);
		record.layoutData.heightScale = heightScale;
	}
};

Chart.prototype.onMouseMove = function(event) {
	var eventPoint = PosUtils.translateEventPos(event);
	this["chart_Chart"].coordinate.onMouseMove(eventPoint);
};

Chart.prototype.onMouseClick = function(event) {
	var eventPoint = PosUtils.translateEventPos(event);
	this["chart_Chart"].coordinate.onMouseClick(eventPoint);
};