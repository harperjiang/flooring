function create_mask() {
	var m = document.createElement("div");
	m.setAttribute('id', "script_mask");
	m.setAttribute('class', "mask");
	return m;
};

function create_dialog() {
	var d = document.createElement("div");
	d.setAttribute("id", "script_dialog");
	d.setAttribute("class", "dialog");

	var title = document.createElement("div");
	title.setAttribute("id", "script_dialog_title");
	title.setAttribute("class", "dialog_title");

	d.appendChild(title);

	var content = document.createElement("div");
	content.setAttribute("id", "script_dialog_content");
	content.setAttribute("class", "dialog_content");
	d.appendChild(content);

	var span = document.createElement("span");
	span.setAttribute("id", "script_dialog_content_span");
	span.setAttribute("class", "error");
	content.appendChild(span);

	var button = document.createElement("button");
	button.setAttribute("class", "dialog_btn");
	button.setAttribute("onclick", "hide_dialog('script')");
	button.innerHTML = "OK";
	d.appendChild(button);

	return d;
};

function show_dialog_content(title, content) {
	if (document.getElementById('script_mask') == undefined) {
		var mask = create_mask();
		document.body.appendChild(mask);
	}
	if (document.getElementById('script_dialog') == undefined) {
		var dialog = create_dialog();
		document.body.appendChild(dialog);
		dialog.style.left = (document.body.clientWidth - 400) / 2 + "px";
		dialog.style.top = (document.body.clientHeight - 200) / 2 + "px";
	}

	document.getElementById("script_dialog_content_span").innerHTML = content;
	document.getElementById("script_dialog_title").innerHTML = title;
	show_dialog("script");
};

function show_dialog(name) {
	document.getElementById(name + "_mask").style.display = "block";
	document.getElementById(name + "_dialog").style.display = "block";
};

function hide_dialog(name) {
	document.getElementById(name + "_mask").style.display = "none";
	document.getElementById(name + "_dialog").style.display = "none";
};
