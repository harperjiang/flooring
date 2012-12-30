function postdescription(item){
	if(item == 'scheme')
		document.getElementById("parameter").innerHTML = "Ventilation Scheme refers to method used to supply outdoor air to the indoor environment. These methods can broadly classified as either natural ventilation or mechanical ventilation.";
	if(item == 'house')
		document.getElementById("parameter").innerHTML = "Housing type describes the general floor plan for a residential dwelling. The four most common types are single detached, single attached such as townhouse complexes, apartment complexes and manufactured houses, which represent pre-fabricated, typically single detached housing.";
	if(item == 'floor')
		document.getElementById("parameter").innerHTML = "Flooring type for the purposes of this study can be broadly classified as either carpeting or hard floors. The hard floors evaluated within this study included hard woods and vinyl floorings. For carpeting three types were considered; level loop, high density cut pile, and low density cut pile.";
	if(item == 'loading')
		document.getElementById("parameter").innerHTML = "Floor loading refers to that total mass of dust particles per unit area of flooring. The primary sources of these particles include natural infiltration of air from the outdoors, tracked in on shoes by walking from the outdoors to the indoors, and sources within the home such as stoves, the burning of incense, smoking and cooking.";
	if(item == 'ventilation')
		document.getElementById("parameter").innerHTML = "Ventilation rate level or air change rate refers to how quickly indoor air is exchanged with outdoor air. It has units of air changes per hour. The national average is approximately 0.5 per hour. Air changes of less than 0.3 per hour would be considered low while that of approaching 1 per hour and above would be considered relatively high.";
	if(item == 'particle')
		document.getElementById("parameter").innerHTML = "Particle size for the purposes of this study has been classified as either fine or coarse. Fine particles are those having diameter of 2.5 microns or less. Coarse particles are those with diameters greater than 2.5 microns but less than 10 microns.";		
	if(item == 'resuspension')
		document.getElementById("parameter").innerHTML = "Exposure level is the estimated amount of coarse and fine particles in the air that a person would breathe. For this study, the model predicts the levels of fine and coarse particles in each of the rooms in the home and then estimates a person’s exposure by which rooms they are in over a given time period.";	
}