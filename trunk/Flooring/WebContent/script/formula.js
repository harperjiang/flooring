function calculate(){
	document.getElementById("Cin").value = (  document.getElementById("S").value/document.getElementById("v").value 
											+ document.getElementById("p").value*document.getElementById("a1").value*document.getElementById("Cout").value
											)
											/
											(document.getElementById("a2").value*1
											+document.getElementById("k").value*1
											);
	//document.getElementById("Cin").value = document.getElementById("S").value/document.getElementById("v").value + document.getElementById("p").value*document.getElementById("a").value*document.getElementById("Cout").value;
	/*
	var i = document.getElementById("a2").value*1+document.getElementById("k").value*1;
	document.getElementById("expression").innerHTML = "("+document.getElementById("S").value+"/"+document.getElementById("v").value + "+"
													+ document.getElementById("p").value+"*"+document.getElementById("a1").value+"*"+document.getElementById("Cout").value + ")/"
													+ "("+document.getElementById("a2").value + "+"
													+    document.getElementById("k").value+")"
													+ "=" + document.getElementById("S").value/document.getElementById("v").value + "+"
													+ document.getElementById("p").value*document.getElementById("a1").value*document.getElementById("Cout").value
													+ "/" + i;
	*/
}

function calculatea(id){
	if(id=="a1"){
		document.getElementById("a2").value=document.getElementById("a1").value;
	}
	else if(id == "a2"){
		document.getElementById("a1").value=document.getElementById("a2").value;
	}
	calculate();
	
}