// Modal open/close for add new pet
var modal = document.querySelector(".container-modal-new-record");
var closeButton = document.querySelector(".modal-closed-button");
var modalForm = document.querySelector(".container-modal-new-record form");
var modalContent = document.querySelector(".wrapper_content");

// Animal option
var optionSelected = document.querySelector(".option-selected");
var selectedOwnerId = document.querySelector(".selected-owner-id");
var optionsListContainer = document.querySelector(".container-animal-list");
var searchBox = document.querySelector(".option_animal-search input");
var optionList = document.querySelectorAll(".option-wrapper");
var animalFieldsInput = document.querySelectorAll(
  ".wrapper-modal.animal input"
);
var animalFieldsWrapper = document.querySelector(".wrapper-modal.animal");
var selectedOptionOwnerId = document.querySelector(
  ".animal-option-value.animal-update"
);
var clearSelectedOwnerButton = document.querySelector(
  ".clear-selected-owner.animal-update"
);

optionsListContainer.classList.contains("active") ? searchBox.focus() : null;

clearSelectedOwnerButton.addEventListener("click", () => {
  selectedOwnerId.value = "";
  optionSelected.innerText = "Bulunan Hayvan Sahipleri";
  optionsListContainer.classList.contains("active")
    ? optionsListContainer.classList.remove("active")
    : null;

  console.log("selectedOwnerId", selectedOwnerId.value);
});

optionSelected.addEventListener("click", () => {
  console.log("tıklk");
  optionsListContainer.classList.toggle("active");
  optionsListContainer.classList.contains("active") ? searchBox.focus() : null;
  searchBox.value = "";
  filterList("");
});

optionList.forEach((e) => {
  e.addEventListener("click", () => {
    //animal info
    let animalInfo = e.querySelector("label").innerHTML;
    let animalInfoArray = animalInfo
      .split(/(\s+)/)
      .filter((i) => i.trim().length > 0);
    //change selected animal [0] => name
    optionSelected.innerHTML = animalInfoArray[0];

    selectedOwnerId.value = e.querySelector(
      ".animal-option-value.animal-update"
    ).value;
    optionsListContainer.classList.remove("active");

    console.log("selectedOwnerId", selectedOwnerId.value);
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
