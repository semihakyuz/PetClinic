//review modal
var reviewModalContainer = document.querySelector(".container-review");
var actionTableCompletedButton = document.querySelectorAll(
  ".process-button.button-completed.review"
);
var reviewModalClosedButton = document.querySelector(
  ".modal-closed-button.review"
);

actionTableCompletedButton.forEach((e) => {
  e.addEventListener("click", function () {
    reviewModalContainer.classList.add("modal_active");
  });
});
if (reviewModalClosedButton) {
  reviewModalClosedButton.addEventListener("click", function () {
    reviewModalContainer.classList.remove("modal_active");
  });
}

// detail closed button redirect
var detailClosedButton = document.querySelector(
  ".wrapper-details div[class=modal-closed-button] a"
);
if (detailClosedButton) {
  detailClosedButton.addEventListener("click", function () {
    history.back();
  });
}

var deleteActionTableButton = document.querySelectorAll(
  ".process-button.button-delete"
);
var deleteAlertModal = document.querySelector(".container-delete-record-alert");
var existingRecordId = document.querySelector(
  ".container-delete-record-alert form input"
);
var deleteAlertForm = document.querySelector(
  ".container-delete-record-alert form"
);
var deleteAlertCancelButton = document.querySelector(".delete-cancel");
var deleteSubmitButton = document.querySelector(".delete-submit");
var paramField = document.querySelector(".param");

deleteAlertCancelButton.addEventListener("click", function () {
  deleteAlertModal.classList.remove("modal_active");
});

deleteActionTableButton.forEach((e) => {
  e.addEventListener("click", function () {
    //animal id
    var recordId = e.nextElementSibling.textContent;

    // change form input value
    existingRecordId.value = recordId;

    if (e.classList.contains("all-animals")) {
      deleteAlertForm.setAttribute("action", `/pets/delete`);
    } else if (e.classList.contains("all-owners")) {
      deleteAlertForm.setAttribute("action", `/owner/delete`);
    } else if (e.classList.contains("all-users")) {
      deleteAlertForm.setAttribute("action", `/user/delete`);
    } else {
      deleteAlertForm.setAttribute("action", `/examination/delete`);
    }

    // param value
    var urlParams = new URLSearchParams(window.location.search);
    var status = urlParams.get("status");
    paramField.value = status;

    //modal active
    deleteAlertModal.classList.add("modal_active");
  });
});

var newPatientModal = document.querySelector(".container-modal-new-record");
var newUserModal = document.querySelector(
  ".container-review.user-registration"
);
var addIconPatient = document.querySelector(".icon_add_pets");
var addIconUser = document.querySelector(".icon_add_user");

addIconPatient.addEventListener("click", function () {
  newPatientModal.classList.add("modal_active");
});

addIconUser.addEventListener("click", function () {
  newUserModal.classList.add("modal_active");
});

// Animal option
var optionSelected = document.querySelector(".option-selected");
var animalId = document.querySelector(".animalId");
var optionsListContainer = document.querySelector(".container-animal-list");
var searchBox = document.querySelector(".option_animal-search input");
var optionList = document.querySelectorAll(".option-wrapper");
var animalFieldsInput = document.querySelectorAll(
  ".wrapper-modal.animal input"
);
var animalFieldsWrapper = document.querySelector(".wrapper-modal.animal");
var selectedAnimalId = document.querySelectorAll(".animal-option-value");
var clearButtonFields = document.querySelector(".animal-clear-fields span");

clearButtonFields
  ? clearButtonFields.addEventListener("click", () => {
      animalFieldsInput.forEach((item) => {
        item.value = "";
        animalFieldsWrapper.classList.remove("disabled");
        optionSelected.innerText = "Kayıtlı Hayvanları";
      });
    })
  : null;

optionSelected
  ? optionSelected.addEventListener("click", () => {
      optionsListContainer.classList.toggle("active");
      optionsListContainer.classList.contains("active")
        ? searchBox.focus()
        : null;

      searchBox.value = "";
      filterList("");
    })
  : null;

optionList.forEach((e) => {
  e.addEventListener("click", () => {
    animalFieldsWrapper.classList.add("disabled");

    //animal info
    let animalInfo = e.querySelector("label").innerHTML;
    let animalInfoArray = animalInfo
      .split(/(\s+)/)
      .filter((i) => i.trim().length > 0);
    //change selected animal [0] => name
    optionSelected.innerHTML = animalInfoArray[0];

    //change animal fields
    let selectedAnimalId = e.querySelector(".animal-option-value").value;
    animalInfoArray.unshift(selectedAnimalId);

    animalFieldsInput.forEach((item, i) => {
      item.value = animalInfoArray[i];
    });
    optionsListContainer.classList.remove("active");
  });
});

searchBox
  ? searchBox.addEventListener("keyup", (e) => {
      filterList(e.target.value);
    })
  : null;

var filterList = (searchTerm) => {
  searchTerm = searchTerm.toLowerCase();
  optionList.forEach((option) => {
    let label = option.firstElementChild.innerText.toLowerCase();

    label.indexOf(searchTerm) != -1
      ? (option.style.display = "block")
      : (option.style.display = "none");
  });
};

