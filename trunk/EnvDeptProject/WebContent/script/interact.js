function extract_select_value(id) {
	var selectObject = document.getElementById(id);
	return selectObject.options[selectObject.selectedIndex].value;
}

function show_distribution_done(summary) {
	if (summary != null && typeof summary == 'object') {
		var record = new RecordData(summary.max, summary.min, summary.val75,
				summary.val25, summary.median);
		// Add Record to chart
		thechart.addRecord(record);
		thechart.redraw();
		// Add Record to data table
		thedatatable.addItem(summary);
		// Record existed criteria
		recordCriteria(summary.criteria);
	} else {
		show_dialog_content('Error', 'No record found for selected criteria');
	}
	unmask();
};

function mask() {
	document.getElementById('progress_mask').style.display = 'block';
};

function unmask() {
	document.getElementById('progress_mask').style.display = 'none';
};

var existedCriteria = new Array();

function criteriaToString(criteria) {
	return "" + criteria.ventScheme + criteria.houseType + criteria.floorType
			+ criteria.floorLoading + criteria.ventLevel + criteria.partGran
			+ criteria.probabilitySuspension;
}

function removeCriteria(criteria) {
	var index = existedCriteria.indexOf(criteriaToString(criteria));
	if (index != -1)
		existedCriteria.splice(index, 1);
}

function recordCriteria(criteria) {
	existedCriteria.push(criteriaToString(criteria));
}

function checkCriteria(criteria) {
	return existedCriteria.indexOf(criteriaToString(criteria));
}

function show_distribution() {
	mask();
	if (thedatatable.datas.length >= 10) {
		unmask();
		show_dialog_content('Exceed Limit',
				'You are only allowed to display no more than 10 items');
		return;
	}

	var vent_scheme = extract_select_value('vent_scheme');
	var house_type = extract_select_value('house_type');
	var floor_type = extract_select_value('floor_type');
	var floor_loading = extract_select_value('floor_loading');
	var vent_rate = extract_select_value('vent_rate');
	var part_gran = extract_select_value('part_gran');
	var prob_sus = extract_select_value('prob_sus');

	var criteria = {
		"ventScheme" : vent_scheme,
		"houseType" : house_type,
		"floorType" : floor_type,
		"floorLoading" : floor_loading,
		"ventLevel" : vent_rate,
		"partGran" : part_gran,
		"probabilitySuspension" : prob_sus
	};
	if (checkCriteria(criteria) != -1) {
		unmask();
		show_dialog_content('Duplicate', 'The selected criteria already exists');
		return;
	}

	summaryService.read(criteria, show_distribution_done);
};

function remove_distribution() {
	var selection = thedatatable.selectModel.getSelection();
	selection.sort();
	for ( var i = selection.length - 1; i >= 0; i--) {
		var criteria = thedatatable.datas[selection[i]].criteria;
		removeCriteria(criteria);
		thedatatable.removeItem(selection[i]);
		thechart.removeRecord(selection[i]);
	}
	thechart.redraw();
};

var ventSchemeText = [ 'Natural', 'Air Conditioned' ];
var houseTypeText = [ 'Single Detached', 'Apartment Building' ];
var floorTypeText = [ 'Hardwood', 'Carpeting', 'HD Carpeting' ];
var floorLoadingText = [ 'Low', 'Medium', 'High' ];
var ventLevelText = [ 'Low', 'Medium', 'High' ];
var partGranText = [ 'Fine', 'Coarse' ];
var probSusText = [ 'CR=0', 'CR≠0', 'CR=∅' ];
SummaryRenderer = Class.extend({
	render : function(item, summary) {
		item.innerHTML = "Criteria: "
				+ ventSchemeText[summary.criteria.ventScheme - 1] + ","
				+ houseTypeText[summary.criteria.houseType - 1] + ","
				+ floorTypeText[summary.criteria.floorType - 1] + ","
				+ floorLoadingText[summary.criteria.floorLoading - 1] + ","
				+ ventLevelText[summary.criteria.ventLevel - 1] + ","
				+ partGranText[summary.criteria.partGran - 1] + ","
				+ probSusText[summary.criteria.probabilitySuspension - 1];
		var data = "Max:\t" + summary.max + "\n75% Value:\t" + summary.val75
				+ "\nMedian:\t" + summary.median + "\n25% Value:\t" + summary.val25
				+ "\nMin:\t" + summary.min;
		item.setAttribute('title', data);
	}
});