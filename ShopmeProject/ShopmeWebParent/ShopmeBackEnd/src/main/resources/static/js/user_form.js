$(document).ready(function() {
	$("#btnCancel").on("click", function() {
		window.location = "/ShopmeAdmin/users";
	});
	$("#fileImage").change(function() {
		fileSize = this.files[0].size;
		if (fileSize > 1048576) {
			this.setCustomValidity("you must choose image less than 1 MB")
			this.reportValidity();
		} else {
			this.setCustomValidity("");
			showImageThumbnail(this);
		}
	});
});

function showImageThumbnail(fileInput) {
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e) { $("#thumbnail").attr("src", e.target.result); };
	reader.readAsDataURL(file);
};
function checkEmailUnique(form) {
	url = "/ShopmeAdmin/users/check_email";
	userEmail = $("#email").val();
	userId = $("#id").val();
	csrf = $("input[name='_csrf']").val();
	params = {
		id: userId,
		email: userEmail,
		_csrf: csrf
	};
	$.post(
		url,
		params,
		function(response) {
			if (response == "Ok") {
				form.submit();
			} else if (response == "Duplicated") {
				showWarningModal(
					"This Email is already used by other user. Please change your email."
					+ userEmail);
			} else {
				showErrorModal("Unknown response from the server.");
			}
		}).fail(function() {
			showErrorModal("could not connect to the server.");
		});
	return false;
};
function showModal(title, message) {
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal();
}
function showErrorModal(message){
	showModal("Error", message);
}
function showWarningModal(message){
	showModal("Warning", message);
}