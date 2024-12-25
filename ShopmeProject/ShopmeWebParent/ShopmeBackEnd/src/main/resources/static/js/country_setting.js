var buttonLoad;
var dropDownCountry;
var buttonAdd;
var buttonUpdate;
var buttonDelete;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;

$(document).ready(function() {
	buttonLoad = $("#buttonLoadCountries");
	dropDownCountry = $("#dropDownCountries");

	buttonAdd = $("#add");
	buttonUpdate = $("#update");
	buttonDelete = $("#delete");

	labelCountryName = $("#labelCountryName");
	fieldCountryName = $("#fieldCountryName");
	fieldCountryCode = $("#fieldCountryCode");

	buttonLoad.click(function() {
		getAllCountryList();
	});
	dropDownCountry.on("change", function() {
		changeFormStateToselectedCountry();
	});
	buttonAdd.click(function() {
		if (buttonAdd.val() == "add") {
			addCountry();
		} else {
			changeFormStateToNew();
		}
	});
	buttonUpdate.click(function() {
		updateCountry();
	});
	buttonDelete.click(function() {
		deleteCountry();
	});
});

function deleteCountry() {
	countryId = dropDownCountry.val().split("-")[0];
	url = contextPath + "countries/delete/" + countryId;
	$.get(url, function() {
		$("#dropDownCountries option[value='" + dropDownCountry.val() + "']").remove();
		changeFormStateToNew();
	}).done(function() {
		showToastMessage("The Country has been deleted.");
	}).fail(function() {
		showToastMessage("ERROR : could not connect to server or server encountered an error");
	});
}
function validateFormCountry(){
	formCountry = document.getElementById("countryForm");
	if (!formCountry.checkValidity()) {
		formCountry.reportValidity();
		return false;
	}
	return true;
}
function updateCountry() {
	if (!validateFormCountry()) return;
	url = contextPath + "countries/save";
	countryId = dropDownCountry.val().split("-")[0];
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	jsonData = { id: countryId, name: countryName, code: countryCode };
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId) {
		$("#dropDownCountries option:selected").val(countryId + "-" + countryCode);
		$("#dropDownCountries option:selected").text(countryName);
		showToastMessage("The country has been updated : " + countryId);
		changeFormStateToNew();
	}).fail(function() {
		showToastMessage("ERROR : could not connect to server or server encountered an error");
	});
}

function addCountry() {
	if (!validateFormCountry()) return;
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	jsonData = { name: countryName, code: countryCode };
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId) {
		selectedNewlyCountry(countryId, countryName, countryCode);
		showToastMessage("new Country has been added countryID is : " + countryId);
	}).fail(function() {
		showToastMessage("ERROR : could not connect to server or server encountered an error");
	});
}

function selectedNewlyCountry(id, name, code) {
	optionValue = id + "-" + code;
	$("<option>").val(optionValue).text(countryName).appendTo(dropDownCountry);
	$("#dropDownCountries option[value='" + optionValue + "']").prop("selected", true);
	fieldCountryName.val("").focus();
	fieldCountryCode.val("");
}

function changeFormStateToNew() {
	buttonAdd.prop("value", "add");
	labelCountryName.text("Country Name");
	buttonUpdate.prop("disabled", true);
	buttonDelete.prop("disabled", true);
	fieldCountryName.val("").focus();
	fieldCountryCode.val("")
}
function changeFormStateToselectedCountry() {
	buttonAdd.prop("value", "add");
	buttonUpdate.prop("disabled", false);
	buttonDelete.prop("disabled", false);
	labelCountryName.text("Selected Country")
	selectedCountryName = $("#dropDownCountries option:selected").text();
	fieldCountryName.val(selectedCountryName);
	countryCode = dropDownCountry.val().split("-")[1];
	fieldCountryCode.val(countryCode);
}

function getAllCountryList() {
	url = contextPath + "countries/list";
	$.get(url, function(responseJSON) {
		dropDownCountry.empty();
		$.each(responseJSON, function(index, country) {
			optionValue = country.id + "-" + country.code;
			$("<option>").val(optionValue).text(country.name).appendTo(dropDownCountry);
		});
	}).done(function() {
		buttonLoad.val("Refresh Country List");
		showToastMessage("All Countries have been Loaded");
	}).fail(function() {
		showToastMessage("ERROR : could not connect to server or server encountered an error");
	});
}

function showToastMessage(message) {
	$("#toastMessage").text(message);
	$(".toast").toast("show");
}