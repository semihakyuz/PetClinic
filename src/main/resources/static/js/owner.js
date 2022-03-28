var deleteActionTableButton = document.querySelectorAll(
  ".process-button.button-delete.owner-update"
);
var deleteAlertModal = document.querySelector(
  ".container-delete-record-alert.owner-update"
);
var existingRecordId = document.querySelector(".existingAnimalId");
var deleteAlertForm = document.querySelector(
  ".container-delete-record-alert form"
);
var deleteAlertCancelButton = document.querySelector(".delete-cancel");
var deleteSubmitButton = document.querySelector(".delete-submit");
var paramField = document.querySelector(".param");
var requestAnimalId = document.querySelector(".requestAnimalId");

deleteAlertCancelButton.addEventListener("click", function () {
  deleteAlertModal.classList.remove("modal_active");

  // reset request animalId input value
  requestAnimalId.value = "";
});

deleteActionTableButton.forEach((e) => {
  e.addEventListener("click", function () {
    // change request animalId input value
    requestAnimalId.value = existingRecordId.value;

    // param value
    var urlParams = new URLSearchParams(window.location.search);
    var status = urlParams.get("status");
    paramField.value = status;

    //modal active
    deleteAlertModal.classList.add("modal_active");
  });
});
