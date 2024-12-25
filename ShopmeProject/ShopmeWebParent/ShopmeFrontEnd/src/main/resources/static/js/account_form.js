$(document).ready(function() {
	$("#btnCancel").on("click", function() {
		window.location = "/ShopmeAdmin/";
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
	reader.onload = function(e) {
		$("#thumbnail").attr("src", e.target.result);
	};
	reader.readAsDataURL(file);
};
function checkPasswordMatch(confirmPassword) {
	if (confirmPassword.value != $("#password").val()) {
		confirmPassword.setCustomValidity("Passwords doesn't match");
	}
	else {
		confirmPassword.setCustomValidity("");
	}
}














