AbstractSelectModel = Class.extend({
	construct : function() {
		this.selectListeners = new Array();
	},
	addSelectListener : function(listener) {
		this.selectListeners.push(listener);
	},
	itemSelected : function(index) {
		for ( var i in this.selectListeners) {
			this.selectListeners[i].onItemSelected(index);
		}
	},
	itemUnselected : function(index) {
		for ( var i in this.selectListeners) {
			this.selectListeners[i].onItemUnselected(index);
		}
	}
});

MultiSelectModel = AbstractSelectModel.extend({
	construct : function() {
		arguments.callee.$.construct.call(this);
		this.selected = new Array();
	},
	select : function(index) {
		if (this.selected.indexOf(index) != -1) {
			// Already Selected, unselect it
			this.selected.splice(this.selected.indexOf(index), 1);
			this.itemUnselected(index);
		} else {
			this.selected.push(index);
			this.itemSelected(index);
		}
	},
	remove : function(index) {
		var i = this.selected.indexOf(index);
		if (i != -1)
			this.selected.splice(i, 1);
	},
	getSelection : function() {
		var a = new Array();
		a = a.concat(this.selected);
		return a;
	}
});

SingleSelectModel = AbstractSelectModel.extend({
	construct : function() {
		arguments.callee.$.construct.call(this);
		this.selected = -1;
	},
	select : function(index) {
		var old = this.selected;
		this.selected = index;
		this.itemUnselected(old);
		if (old !== this.selected) {
			this.itemSelected(this.selected);
		}
	},
	remove : function(index) {
		if (this.selected == index)
			this.selected = -1;
	},
	getSelection : function() {
		var a = new Array();
		if (this.selected != -1)
			a.push(this.selected);
		return a;
	}
});

DataTableRenderer = Class.extend({
	construct : function(holder, controller) {
		this.holder = holder;
		this.controller = controller;
		this.items = new Array();
	},
	onItemSelected : function(index) {
		this.items[index].className = 'data_table_item_selected';
	},
	onItemUnselected : function(index) {
		this.items[index].className = 'data_table_item';
	},
	addItem : function(pos, newdata) {
		var item = this.createItem(newdata);
		if (pos == this.items.length) {
			this.items.push(item);
			this.holder.appendChild(item);
		} else {
			this.items.splice(pos, 0, item);
			this.holder.insertBefore(item, items[pos]);
		}
	},
	removeItem : function(pos) {
		var item = this.items[pos];
		this.items.splice(pos, 1);
		item.parentNode.removeChild(item);
		item.datatable = undefined;
	},
	clickItem : function(item) {
		this.controller.selectItem(this.items.indexOf(item));
	},
	createItem : function(data) {
		var newitem = document.createElement('div');
		newitem.setAttribute("class", "data_table_item");
		if (this.itemRenderer !== undefined) {
			this.itemRenderer.render(newitem, data);
		} else {
			newitem.innerHTML = data;
		}
		newitem.setAttribute("onclick", "this.datatable.clickItem(this)");
		newitem.datatable = this;
		return newitem;
	}
});

DataTable = Class.extend({
	construct : function(holder) {
		this.datas = new Array();
		this.selectModel = new MultiSelectModel();
		this.renderer = new DataTableRenderer(holder, this);
		this.selectModel.addSelectListener(this.renderer);
	},
	addItem : function(data) {
		this.datas.push(data);
		this.itemAdded(this.datas.length - 1);
	},
	removeItem : function(index) {
		this.datas.splice(index, 1);
		this.itemRemoved(index);
	},
	selectItem : function(index) {
		this.selectModel.select(index);
	},
	itemAdded : function(pos) {
		this.renderer.addItem(pos, this.datas[pos]);
	},
	itemRemoved : function(pos) {
		this.renderer.removeItem(pos);
		this.selectModel.remove(pos);
	}
});
