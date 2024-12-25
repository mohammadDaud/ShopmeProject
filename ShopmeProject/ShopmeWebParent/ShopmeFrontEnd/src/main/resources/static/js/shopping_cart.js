$(document).ready(function() {
	$(".linkMinus").on("click", function(evt) {
		evt.preventDefault();
		decreaseQuantity($(this));
	});
	$(".linkPlus").on("click", function(evt) {
		evt.preventDefault();
		increaseQuantity($(this));
	});
	$(".linkRemove").on("click", function(evt) {
		evt.preventDefault();
		deleteProductFromCart($(this));
	});
});

function decreaseQuantity(link) {
	productId = link.attr("pid");
	quantityInput = $("#quantity" + productId);
	newQuantity = parseInt(quantityInput.val()) - 1;
	if (newQuantity > 0) {
		quantityInput.val(newQuantity);
		updateQuantity(newQuantity, productId);
	} else {
		showWarningModal("Minimum quantity 1");
	}
}
function increaseQuantity(link) {
	productId = link.attr("pid");
	quantityInput = $("#quantity" + productId);
	newQuantity = parseInt(quantityInput.val()) + 1;
	if (newQuantity <= 5) {
		quantityInput.val(newQuantity);
		updateQuantity(newQuantity, productId);
	} else {
		showWarningModal("Maximum quantity 5");
	}
}
function updateQuantity(quantity, productId) {
	url = contextPath + "cart/update/" + productId + "/" + quantity;
	$.ajax({
		type: "POST",
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue)
		}
	}).done(function(updatedSubtotal) {
		updateSubtotal(updatedSubtotal, productId);
		updateEstimateTotal();
	}).fail(function() {
		showErrorModal("Error while updating product quantity.");
	});
}
function updateSubtotal(updatedSubtotal, productId) {
	formatedSubtotal = $.number(updatedSubtotal, 2);
	$("#subtotal" + productId).text(formatedSubtotal);
}
function updateEstimateTotal() {
	total = 0.0;
	productCount = 0;
	$(".Subtotals").each(function(index, ele) {
		productCount++;
		total += parseFloat(ele.innerHTML.replace(",", ""));
	});
	if (productCount < 1) {
		showEmptyShoppingCart();
	} else {
		formattedTotal = $.number(total, 2);
		$("#estimatedTotal").text(formattedTotal);
	}
}
function deleteProductFromCart(link) {
	url = link.attr("href");
	$.ajax({
		type: "DELETE",
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue)
		}
	}).done(function(response) {
		showModal("Remove Product", response);
		rowNumber = link.attr("rowNumber");
		removeProductHtml(rowNumber);
		updateEstimateTotal();
		updateCountNumber();
	}).fail(function() {
		showErrorModal("Error while removing product.");
	});
}

function removeProductHtml(rowNumber) {
	$("#row" + rowNumber).remove();
	$("#blankLine" + rowNumber).remove();

}
function updateCountNumber() {
	$(".divCount").each(function(index, element) {
		element.innerHTML = ""   (index + 1);
	});
}
function showEmptyShoppingCart() {
	$("#sectionTotal").hide();
	$("#sectionEmpty").removeClass("d-none");
}