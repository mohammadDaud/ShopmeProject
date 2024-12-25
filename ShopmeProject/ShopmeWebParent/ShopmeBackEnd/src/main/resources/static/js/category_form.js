function checkNameUnique(form) {
			catId = $("#id").val();
			catName = $("#name").val();
			catAlias = $("#alias").val();
			csrfValue = $("input[name='_csrf']").val();
			url = ModalUrl;
			param = {
				id : catId,
				name : catName,
				alias : catAlias,
				_csrf : csrfValue
			};
			$.post(url, param, function(response) {
				if(response=="OK"){
					form.submit();
				} else if(response=="DuplicateName"){
					showWarningModal("there is another category having same name."+catName);
				}else if(response=="DuplicateAlias"){
					showWarningModal("there is another category having same alias."+catAlias);
				}else{
					showErrorModal("Unknown response from the server.");
				}
			}).fail(function() {
				showErrorModal("could not connect to the server.");
			});
			return false;
		}
		
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