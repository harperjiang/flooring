var scenes = new Array(10);
var stats = new Array(10);
var index = 0;
var sceneno = 8; 
var scene = {};
var stat = {};
function init() {
        for (var x = 0; x < sceneno;x++) {
            scenes[x] = new Array(9);
            scenes[x][0] = 0;
            stats[x] = new Array(9);
            stats[x][0] = 0;
        }
        scenelist();
    }
    window.onload = init;

    //for display button
    function check() {


    	if (index >= sceneno) {
    		document.getElementById("parameter").innerHTML = "Only support " + sceneno + " scenes<br />" 
    		                                               + "Please remove some scenes before adding more";
    		return;
    	}

    	document.getElementById("parameter").innerHTML = "";
    	if (document.getElementById("scheme").value=="")
    	{
    		document.getElementById("parameter").innerHTML = "scheme should be selected";
        	alert("Am I here?");
        	return;
    	}
    	if (document.getElementById("house").value=="")
    	{
    		document.getElementById("parameter").innerHTML = "house type should be selected";
    		return;
    	}
    	if (document.getElementById("floor").value=="")
    	{
    		document.getElementById("parameter").innerHTML = "floor type should be selected";
    		return;
    	}
    	if (document.getElementById("loading").value=="")
    	{
    		document.getElementById("parameter").innerHTML = "floor loading should be selected";
    		return;
    	}
    	if (document.getElementById("ventilation").value=="")
    	{
    		document.getElementById("parameter").innerHTML = "ventilation rate should be selected";
    		return;
    	}
    	if (document.getElementById("particle").value=="")
    	{
    		document.getElementById("parameter").innerHTML = "particle granularity should be selected";
    		return;
    	}
    	if (document.getElementById("resuspension").value=="")
    	{
    		document.getElementById("parameter").innerHTML = "probability of suspension granularity should be selected";
    		return;
    	}
    	var i = 0;
    	while (scenes[i][0]== 1) {
    		i++;
    	}


    	if (i < scenes.length) {

    		scenes[i][0] = 0;
    		scenes[i][1] = 0;
    		scenes[i][2] = document.getElementById("scheme").value;
    		scenes[i][3] = document.getElementById("house").value;
    		scenes[i][4] = document.getElementById("floor").value;
    		scenes[i][5] = document.getElementById("loading").value;
    		scenes[i][6] = document.getElementById("ventilation").value;
    		scenes[i][7] = document.getElementById("particle").value;
    		scenes[i][8] = document.getElementById("resuspension").value;

    		scene.scheme = document.getElementById("scheme").value;
    		scene.house= document.getElementById("house").value;
    		scene.floor= document.getElementById("floor").value;
    		scene.loading= document.getElementById("loading").value;
    		scene.ventilation = document.getElementById("ventilation").value;
    		scene.particle= document.getElementById("particle").value;
    		scene.resuspension = document.getElementById("resuspension").value;
    		//for debugging scene array+`
    		alert(JSON.stringify(scene));
    		var s = "Scene: ";
    		
    		for (var x=2; x< scenes[i].length; x++) {
    			s += scenes[i][x] + "; ";
    		}
    		s += "is added<br />";

    		document.getElementById("parameter").innerHTML = "<p>" + s + "</p>";
            ajaxsend(i);

    	}
    	else {
    		document.getElementById("parameter").innerHTML =   "Scenes are selected more than "+ sceneno+"<br />"
    		+ "Please remove some scenes before adding more";
    	}
    }
    
    function scenelist() {

        var s = new String();
        s += "<form><select id='scenelist' style='width:800px;'>";
        s += "<option value='-1' selected>"+ index + " scenes have been displayed";
        if (index > 0) {
            for (x in scenes) {
                if (scenes[x][0] == 1) {
                    s += "<option value="+x+">";
                    s+= "scene " + x + " : ";
                    for (var y=2; y< scenes[x].length;y++) {
                        s += scenes[x][y] + ";";
                    }
                }
            }
            s += "</select></form>";
        }
        s += "<input type='button' id='remove' value='Remove' onClick='sceneremove();'/>";
        s += "<input type='button' id='download' value='Download' onClick='download();'/>";
        s += "<br />";
        document.getElementById("scenes").innerHTML = s;
        if (index == 0) {
            drawaxis();
        } else {
        	//alert("It should go here");
            drawplot();
        }

    }

    function sceneremove() {
        var i = document.getElementById("scenelist").value;
        if (i != "-1") {
            scenes[i][0] = 0;
            scenes[i][1] = 0;
            stats[i].valid = 0;
            index--;
            var s = new String();

            for (var x=0; x<scenes[i].length; x++) {
                s += scenes[i][x] + "; ";
            }
            s += "is removed<br />";

            document.getElementById("parameter").innerHTML = "<p>" + s + "</p>";
        }
        else {
            document.getElementById("parameter").innerHTML = "No scene is selected to remove. Please click the droplist";
        }
        scenelist();
}
    
    function clearplot(){
    	for (var i=0; i<sceneno;i++) {
    		scenes[i][0] = 0;
    		scenes[i][1] = 0;
    		stats[i].valid = 0;
    	}
    	index=0;
    	//should reset scene and stat too
    	scenelist();
    }
    
    function ajaxsend(idx){
    	var xmlhttp;
    	xmlhttp = new XMLHttpRequest();
    	xmlhttp.onreadystatechange=function()
    	{
    	    if(xmlhttp.readyState==4 && xmlhttp.status==200)
    	    {
    	    	
    	    	document.getElementById("parameter").innerHTML="";
    	    	//document.getElementById("parameter").innerHTML=xmlhttp.responseText;
    	    	var stat = eval("("+xmlhttp.responseText+")");
    	    	if(stat.valid==1){
    	    		document.getElementById("parameter").innerHTML +=  "Scene " + idx + " stats as below:<br />";	    		
    	    		document.getElementById("parameter").innerHTML +=  "--------record min ------> " + stat.min +"<br />";
    	    		document.getElementById("parameter").innerHTML +=  "--------record 25% ------> " + stat.p25 +"<br />";
    	    		document.getElementById("parameter").innerHTML +=  "--------record medium ---> " + stat.medium +"<br />";
    	    		document.getElementById("parameter").innerHTML +=  "--------record 75% ------> " + stat.p75 +"<br />";
    	    		document.getElementById("parameter").innerHTML +=  "--------record max ------> " + stat.max +"<br />";

    	    		stats[idx]= stat;
    	    		scenes[idx][0]=1;
    	    		index++;
    	    	}
    	    	else{
    	    		alert("SORRY! Database doesn't have the specified data!");
    	    	}	
    	    	scenelist();
    			//document.getElementById("result").innerHTML=xmlhttp.responseText;
    	    }
    	    /*else{
    	    	document.getElementById("result").innerHTML="readystate is " + xmlhttp.readyState + "  and status is " + xmlhttp.status + " CANNOT PROCEED";
    	    }*/
    	};
    	
    	xmlhttp.open("POST", "Query", true);
    	//var param = "scene=haha";	
    	var param = JSON.stringify(scene); //has to be serialized
    	xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    	//xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    	//xmlhttp.setRequestHeader("Content-length", param.length);
    	//xmlhttp.setRequestHeader("Connection", "close");
    	xmlhttp.send(param);

    }
    

    function drawplot() {
    	var scale = 5;
        var canvas = document.getElementById("plot");
        var context = canvas.getContext('2d');
        context.clearRect ( 0 , 0 , canvas.width , canvas.height );
        context.beginPath();
        context.moveTo(100, 600);
        context.lineTo(100, 0);
        context.stroke();
        context.moveTo(100, 600);
        context.lineTo(100 + 800, 600);
        context.stroke();
        
        for(var i=0;i<(sceneno-1); i++){
    		context.moveTo(100+100+100*i, 605);
    		context.lineTo(100+100+100*i, 600);
    		context.stroke();
        }
		context.moveTo(100+100+100*(sceneno-1), 595);
		context.lineTo(100+100+100*(sceneno-1), 600);
		context.stroke();
        
		
		
        var j=0;
        for(var i=0;i<sceneno; i++){
        	if( (scenes[i][0]==1) && (stats[i].valid==1)){
        		//lengend for scenes, adjust the scene 
        		context.fillText("scence"+j, 100+80+100*j-20, 620);
        		context.moveTo(100+100+100*j, 605);
        		context.lineTo(100+100+100*j, 600);
        		context.stroke();
        		//graph  20 is the offset, box width=20 
        		context.fillText("Medium:"+stats[i].medium, 100+100+100*j+20, 600 - stats[i].medium*scale);
        		context.moveTo(100+100+100*j - 10, 600 - stats[i].medium*scale);
        		context.lineTo(100+100+100*j + 10, 600 - stats[i].medium*scale);
        		context.stroke();
        		//up-x, up-y, width, height
        		context.strokeRect(100+100+100*j - 10, 600 - stats[i].p75*scale, 10*2, (stats[i].p75*scale-stats[i].p25*scale));
        		//box top and bottom, offset the text in case overlapped
        		context.fillText("75%:"+stats[i].p75, 100+100+100*j+20, 600 - stats[i].p75*scale-5);
        		context.fillText("25%:"+stats[i].p25, 100+100+100*j+20, 600 - stats[i].p25*scale+5);
        		//line top and bottom
        		context.fillText("max:"+stats[i].max, 100+100+100*j  + 6, 600 - stats[i].max*scale -5);
        		context.moveTo(100+100+100*j - 4, 600 - stats[i].max*scale);
        		context.lineTo(100+100+100*j + 4, 600 - stats[i].max*scale);
        		context.stroke();
        		context.fillText("min:"+stats[i].min, 100+100+100*j + 6, 600 - stats[i].min*scale + 5);
        		context.moveTo(100+100+100*j - 4, 600 - stats[i].min*scale);
        		context.lineTo(100+100+100*j + 4, 600 - stats[i].min*scale);
        		context.stroke();
        		//vertical line
        		context.moveTo(100+100+100*j, 600 - stats[i].max*scale);        		
        		context.lineTo(100+100+100*j, 600 - stats[i].p75*scale);
        		context.stroke();
        		context.moveTo(100+100+100*j, 600 - stats[i].p25*scale);        		
        		context.lineTo(100+100+100*j, 600 - stats[i].min*scale);
        		context.stroke();
         		j++;
        	}

        }

    }

    function drawaxis() {
        var canvas = document.getElementById("plot");
        var context = canvas.getContext('2d');
        context.clearRect(0, 0, canvas.width, canvas.height);
        context.beginPath();
        context.moveTo(100, 600);
        context.lineTo(100, 0);
        context.stroke();
        context.moveTo(100, 600);
        context.lineTo(100 + 800, 600);
        context.stroke();
        
		context.fillText("scence axis", 100+100*sceneno/2, 650);   
        for(var i=0;i<(sceneno-1); i++){
    		context.moveTo(100+100+100*i, 605);
    		context.lineTo(100+100+100*i, 600);
    		context.stroke();
        }
		context.moveTo(100+100+100*(sceneno-1), 595);
		context.lineTo(100+100+100*(sceneno-1), 600);
		context.stroke();
    }