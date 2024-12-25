$(document).ready(function() {
	$(".link-delete").on("click", function(e) {
		e.preventDefault();
		link = $(this);
		$("#yesButton").attr("href", link.attr("href"));
		$("#modalBody").text("Are you sure do you want to delete this ?");
		$("#confirmDialog").modal();
	});
});