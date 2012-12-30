var scenes = new Array(10);
var scene;
var index = 0;
var sceneno = 10; 
function init() {
        for (var x = 0; x < sceneno;x++) {
            scenes[x] = new Array(9);
            scenes[x][0] = 0;
        }
        scene["scheme"]="";
        scene["house"]="";
        scene["floor"]="";
        scene["loading"]="";
        scene["ventilation"]="";
        scene["particle"]="";
        scene["resuspension"]="";
        scenelist();
	//getstats();
    }
    window.onload = init;
    
    function choose(form)
    {
    	var radios = document[form][form];
    	for(var i=0; i<radios.length;i++){
    		if( radios[i].checked) {
    	    	scene[form] = radios[i].value;
    		}
    	}
    	document.getElementById("parameter").innerHTML = form + " is selected to be " + scene[form];
    }
    
    //for display button
    function select() {

        if (index >= sceneno) {
            document.getElementById("parameter").innerHTML = "Only support " + sceneno + " scenes<br />"
                                                            + "Please remove some scenes before adding more";
            return;
        }


        document.getElementById("parameter").innerHTML = "";
        if (scene["scheme"]=="")
        {
            document.getElementById("parameter").innerHTML = "scheme should be selected";
            return;
        }
        if (scene["house"]=="")
        {
            document.getElementById("parameter").innerHTML = "house type should be selected";
            return;
        }
        if (scene["floor"]=="")
        {
            document.getElementById("parameter").innerHTML = "floor type should be selected";
            return;
        }
        if (scene["loading"]=="")
        {
            document.getElementById("parameter").innerHTML = "floor loading should be selected";
            return;
        }
        if (scene["ventilation"]=="")
        {
            document.getElementById("parameter").innerHTML = "ventilation rate should be selected";
            return;
        }
        if (scene["particle"]=="")
        {
            document.getElementById("parameter").innerHTML = "particle granularity should be selected";
            return;
        }
        if (scene["resuspension"]=="")
        {
            document.getElementById("parameter").innerHTML = "probability of suspension granularity should be selected";
            return;
        }
        
    }
    
    function scenelist() {

        var s = new String();
        s += "<form><select id='scenes' style='width:800px;'>";
        s += "<option value='-1' selected>"+ index + " scenes have been displayed";
        if (index > 0) {
            for (x in scenes) {
                if (scenes[x][0] == 1) {
                    s += "<option value="+x+">";
                    for (y in scenes[x]) {
                        s += scenes[x][y] + ";";
                    }
                }
            }
            s += "</select></form>";
        }
        s += "<input type='button' id='remove' value='Remove' onClick='sceneremove();'/>";
        s += "<br />";
        document.getElementById("scenes").innerHTML = s;

        if (index == 0) {
            drawaxis();
        } else {
            drawplot(index);
        }

    }

    function sceneremove() {
        var i = document.getElementById("scenes").value;
        if (i != "-1") {
            scenes[i][0] = 0;
            index--;
            var s = new String();

            for (x in scenes[i]) {
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

    function drawplot(index) {
        var canvas = document.getElementById("plot");
        var context = canvas.getContext('2d');
        context.beginPath();
        context.moveTo(100, 600);
        context.lineTo(100, 0);
        context.stroke();
        context.moveTo(100, 600);
        context.lineTo(100 + 800, 600);
        context.stroke();
        context.moveTo(200, 590);
        context.lineTo(200, 600);
        context.stroke();
        context.fillText("scence1", 100 + 80, 610);
        context.fillText("Mean", 200 + 10, 610 - 300 + 5);
        context.moveTo(200 - 10, 610 - 300);
        context.lineTo(200 + 10, 610 - 300);
        context.stroke();
        context.strokeRect(200 - 5, 610 - 300 - 20 / 2, 10, 20);
        context.fillText("25%", 200 + 6, 610 - 300 - 30 + 5);
        context.moveTo(200 - 6, 610 - 300 - 30);
        context.lineTo(200 + 6, 610 - 300 - 30);
        context.stroke();
        context.fillText("75%", 200 + 4, 610 - 300 + 40 + 5);
        context.moveTo(200 - 4, 610 - 300 + 40);
        context.lineTo(200 + 4, 610 - 300 + 40);
        context.stroke();
        context.moveTo(200, 610 - 300 - 30);
        context.lineTo(200, 610 - 300 + 40);
        context.stroke();
        context.moveTo(300, 590);
        context.lineTo(300, 600);
        context.stroke();
        context.fillText("scence2", 100 + 180, 610);
        context.fillText(index, 100 + 80+ index*100, 650);

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
    }