



function showMandatoryForm(){
    let mandatory = document.getElementById("mandatory_form");
    let optional = document.getElementById("optional_form");

    optional.style.display = "none"; //element is not displayed
    mandatory.style.display = "block";
}


function showOptionalForm(){

    let mandatory = document.getElementById("mandatory_form");
    let optional = document.getElementById("optional_form");

    optional.style.display = "block";
    mandatory.style.display = "none"; //element is not displayed
}

//Used when the page loads to show only the first part
function initForms(){
 showMandatoryForm();
}

window.addEventListener("load", () => {
    initForms();
});