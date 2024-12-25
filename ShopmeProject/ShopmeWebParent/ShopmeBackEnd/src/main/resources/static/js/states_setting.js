var buttonLoadCountries4States;
var dropDownCountries4States;
var dropDownState;
var buttonAddState;
var buttonUpdateState;
var buttonDeleteState;
var labelStateName;
var fieldStateName;


$(document).ready(function() {
	buttonLoadCountries4States = $("#buttonLoadCountriesForStates");
	dropDownCountries4States = $("#dropDownCountriesForStates");
	dropDownState = $("#dropDownStates");

	buttonAddState = $("#addState");
	buttonUpdateState = $("#updateState");
	buttonDeleteState = $("#deleteState");

	labelStateName = $("#labelStateName");
	fieldStateName = $("#fieldStateName");


	buttonLoadCountries4States.click(function() {
		getAllCountryListForState();
	});
	dropDownCountries4States.on("change", function() {
		loadStateForCountry();
	});
	
	dropDownState.on("change", function() {
		changeFormStateToselectedCountry();
	});
	
	buttonAddState.click(function() {
		if (buttonAddState.val() == "add") {
			addState();
		} else {
			changeFormStateToNew();
		}
	});
	buttonUpdateState.click(function() {
		updateState();
	});
	buttonDeleteState.click(function() {
		deleteState();
	});
});

function deleteState() {
	stateId = dropDownState.val();
	url = contextPath + "states/delete/" + stateId;
	$.get(url, function() {
		$("#dropDownStates option[value='" + stateId + "']").remove();
		changeFormStateToNew();
	}).done(function() {
		showToastMessage("The State has been deleted.");
	}).fail(function() {
		showToastMessage("ERROR : could not connect to server or server encountered an error");
	});
}

function updateState() {
	if(!validateFormState()) return;
	url = contextPath + "states/save";
	stateId = dropDownState.val();
	stateName = fieldStateName.val();
	selectCountry = $("#dropDownCountriesForStates option:selected");
	countryId = selectCountry.val();
	countryName = selectCountry.text();
	jsonData = { id: stateId, name: stateName, country: { id: countryId, name: countryName } };
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(stateId) {
		$("#dropDownStates option:selected").val(stateId).text(stateName);
		showToastMessage("The State has been updated : " + stateId);
		changeFormStateToNew();
	}).fail(function() {
		showToastMessage("ERROR : could not connect to server or server encountered an error");
	});
}

function validateFormState(){
	formState = document.getElementById("stateForm");
	if (!formState.checkValidity()) {
		formState.reportValidity();
		return false;
	}
	return true;
}
function addState() {
	if(!validateFormState()) return;
	url = contextPath + "states/save";
	stateName = fieldStateName.val();
	selectCountry = $("#dropDownCountriesForStates option:selected");
	countryId = selectCountry.val();
	countryName = selectCountry.text();
	jsonData = { name: stateName, country: { id: countryId, name: countryName } };
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(stateId) {
		selectedNewlyState(stateId, stateName);
		showToastMessage("new State has been added stateID is : " + stateId);
	}).fail(function() {
		showToastMessage("ERROR : could not connect to server or server encountered an error");
	});
}

function selectedNewlyState(id, name) {
	optionValue = id;
	$("<option>").val(optionValue).text(name).appendTo(dropDownState);
	$("#dropDownStates option[value='" + optionValue + "']").prop("selected", true);
	fieldStateName.val("").focus();
}

function getAllCountryListForState() {
	url = contextPath + "countries/list";
	$.get(url, function(responseJSON) {
		dropDownCountries4States.empty();
		$.each(responseJSON, function(index, country) {
			$("<option>").val(country.id).text(country.name).appendTo(dropDownCountries4States);
		});
	}).done(function() {
		buttonLoadCountries4States.val("Refresh Country List");
		showToastMessage("All Countries have been Loaded");
	}).fail(function() {
		showToastMessage("ERROR : could not connect to server or server encountered an error");
	});
}

function loadStateForCountry() {
	selectCountry = $("#dropDownCountriesForStates option:selected");
	countryId = selectCountry.val();
	url = contextPath + "states/list_by_country/" + countryId;
	$.get(url, function(responseJSON) {
		dropDownState.empty();
		$.each(responseJSON, function(index, state) {
			$("<option>").val(state.id).text(state.name).appendTo(dropDownState);
		});
	}).done(function() {
		changeFormStateToNew();
		showToastMessage("All States have been Loaded For Country");
	}).fail(function() {
		showToastMessage("ERROR : could not connect to server or server encountered an error");
	});
}


function showToastMessage(message) {
	$("#toastMessage").text(message);
	$(".toast").toast("show");
}

function changeFormStateToNew() {
	buttonAdd.prop("value", "add");
	buttonUpdate.prop("disabled", true);
	buttonDelete.prop("disabled", true);
	fieldStateName.val("").focus();
}
function changeFormStateToselectedCountry() {
	buttonAddState.prop("value", "add");
	buttonUpdateState.prop("disabled", false);
	buttonDeleteState.prop("disabled", false);
	labelStateName.text("Selected State")
	selectedStateName = $("#dropDownStates option:selected").text();
	fieldStateName.val(selectedStateName);
}