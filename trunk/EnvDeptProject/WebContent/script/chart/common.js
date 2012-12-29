function Point(x, y) {
	this.x = parseInt(x);
	this.y = parseInt(y);
};

Point.prototype.isin = function(dimension, point) {
	return this.x < point.x && this.x + dimension.width > point.x
			&& this.y < point.y && this.y + dimension.height > point.y;
};

function Dimension(width, height) {
	this.width = parseInt(width);
	this.height = parseInt(height);
};

PosUtils = Class.extend({
	position : function(element) {
		if (element == window.document)
			return new Point(0, 0);
		var parentPoint = this.position(element.parentNode);
		return new Point(parentPoint.x + element.offsetLeft, parentPoint.y
				+ element.offsetTop);
	},
	translateEventPos : function(event) {
		var rect = event.target.getBoundingClientRect();
		return new Point(event.clientX - rect.left, event.clientY - rect.top);
	}
});

window.PosUtils = new PosUtils();