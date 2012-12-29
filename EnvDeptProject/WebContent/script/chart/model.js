function Range(min, max) {
	this.min = min;
	this.max = max;
	this.step = 1;
};

Range.prototype.steps = function() {
	return (this.max - this.min) / this.step;
};

Range.prototype.shrink = function() {
	var minimal = 0;
	var maximal = 0;
	var tenpower = 1;
	while (Math.pow(10, tenpower) <= this.max) {
		tenpower++;
	}
	maximal = Math.pow(10, tenpower);
	this.step = maximal / 10;
	while (minimal + this.step < this.min)
		minimal += this.step;
	while (maximal - this.step > this.max)
		maximal -= this.step;
	this.max = maximal;
	this.min = minimal;

};

Range.prototype.label = function(index) {
	return this.min + this.step * index;
};

function RecordData(max, min, val75, val25, median) {
	this.max = max;
	this.min = min;
	this.val75 = val75;
	this.val25 = val25;
	this.median = median;
	this.heightScale = 0;
};