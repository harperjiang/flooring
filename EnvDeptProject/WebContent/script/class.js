//Defines the top level Class
function Class() {
};
Class.prototype.construct = function() {
};
Class.extend = function(def) {
	var classDef = function() {
		if (arguments[0] !== Class) {
			this.construct.apply(this, arguments);
		}
	};

	var proto = new this(Class);
	var superClass = this.prototype;

	for ( var n in def) {
		var item = def[n];
		if (item instanceof Function)
			item.$ = superClass;
		proto[n] = item;
	}

	classDef.prototype = proto;

	// Give this new class the same static extend method
	classDef.extend = this.extend;
	return classDef;
};

NamespaceUtils = Class.extend({
	set : function(namespace, object) {
		var names = namespace.split(".");
		var currentloc = window;
		for ( var i = 0; i < names.length - 1; i++) {
			currentloc = this.setWithSource(currentloc, names[i]);
		}
		currentloc[names[length - 1]] = object;
	},
	setWithSource : function(source, name) {
		if (source[name] === undefined)
			source[name] = {};
		return source.name;
	}
});
window.NamespaceUtils = new NamespaceUtils();