function breakout() {
	if (top.location != location) {
		top.location.href = document.location.href;
	}
}

function impedirCargaIndividual(welcomePage) {
	if (top.location == location) {
		top.location.href = welcomePage;
	}
}

function refreshCabezal() {
	window.parent.document.getElementById('formCabezal:hiddenLink').click();
}
