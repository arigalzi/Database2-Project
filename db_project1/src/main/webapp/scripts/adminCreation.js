let questionCounter = 1;
let reviewCounter = 1;

/**
 * method is GET or POST, url: the server (file) location
 * The file can be any kind of file, like .txt and .xml, or server scripting files
 * (which can perform actions on the server before sending the response back)
 * @type {XMLHttpRequest}
 * send()	Sends the request to the server (used for GET)
 send(string)	Sends the request to the server (used for POST)
 * element is the object to send to Server
 * The ResponseFunc is passed as input
 */

function manageRequest(method, url, formElement, responseFunc) {
    var request = new XMLHttpRequest(); // visible by closure
    request.onreadystatechange = function() {
        responseFunc(request);
    }; // closure
    request.open(method, url);
    if (formElement == null) {
        request.send();
    } else if(formElement instanceof FormData) {
        request.send(formElement);
    } else {
        request.send(new FormData(formElement));

    }
}


/**
 * Here if the user clicks cancel we reset the forms and go back to the Mandatory form
 * only if the request is managed by the server and status is OK
 */
function manageCreationForm(){
    let form = document.getElementById("creation_form");

    //Manage creation form
    manageRequest("POST", './CreationData', form,
        function(request){
            //When the server has responded that we reset the form
            if (request.readyState === 4 && request.status === 200) {
                window.location.assign("../db_project1_war_exploded/adminGreetings.html");
            }
            else if(request.status === 409){ // Conflict request
                showMessage("error_message", "Error: Product of the day already assigned for this date")
            }
            else if(request.status === 400){
                showMessage("error_message", "Please fill in the form correctly")
                checkFields();
            }
            else if(request.status === 403){
                showMessage("error_message", "You cannot set a past date")
            }
            else{
                //other errors
            }

        });

}


function showMessage(html_id, message){
    let element = document.getElementById(html_id);
    element.innerText = message;
}



function addProductQuestion(){
    let container = document.getElementById("productQuestion-container");
    let questionBox = document.createElement("div");
    questionBox.className = "productQuestions";

    questionBox.innerHTML = " <input type=\"text\" style=\"margin-left: -120px; margin-top: 20px;\" id=\"question\" name=\"productQuestion\" >"
    container.appendChild(questionBox);
    let question = document.getElementById("question");
    question.placeholder = "Insert question number "+ questionCounter;
    question.id = "question"+questionCounter;
    questionCounter = questionCounter + 1;

}

function addReview(){
    let container = document.getElementById("review-container");
    let reviewBox = document.createElement("div");
    reviewBox.className = "reviews";

    reviewBox.innerHTML = " <input type=\"text\" style=\"margin-left: -120px; margin-top: 20px;\" id=\"review\" name=\"productReview\" >"
    container.appendChild(reviewBox);
    let review = document.getElementById("review");
    review.placeholder = "Insert review number "+ reviewCounter;
    review.id = "review"+reviewCounter;
    reviewCounter = reviewCounter + 1;

}

function deleteReview(){
    let container = document.getElementById("review-container");
    container.lastChild.remove();
    reviewCounter = reviewCounter -1;

}

function deleteProductQuestion(){
    let container = document.getElementById("productQuestion-container");
    container.lastChild.remove();

    questionCounter = questionCounter -1;

}

function checkFields(){
    let nameField = document.getElementsByName("productName");
    let dateField = document.getElementsByName("productDate");
    let descriptionField = document.getElementsByName("productDescription");
    console.log(nameField);
    let inputs = [nameField[0],dateField[0],descriptionField[0]];
    for (let i = 0; i <inputs.length ; i++) {
        console.log(inputs[i].name)
        if(inputs[i].value === "")
            showFieldsFeedback(inputs[i].name)
        else
            hideFieldsFeedback(inputs[i].name)

    }
}

function showFieldsFeedback(inputField){

    switch (inputField){
        case "productName": document.getElementById("invalid_productName").style.display = "block";
            break;
        case "productDate": document.getElementById("invalid_productDate").style.display = "block";
            break;
        case "productDescription": document.getElementById("invalid_productDescription").style.display = "block";
            break;
    }

}

function hideFieldsFeedback(inputField){

    switch (inputField){
        case "productName": document.getElementById("invalid_productName").style.display = "none";
            break;
        case "productDate": document.getElementById("invalid_productDate").style.display = "none";
            break;
        case "productDescription": document.getElementById("invalid_productDescription").style.display = "none";
            break;
    }

}

window.addEventListener('load',() =>{
    let username = localStorage.getItem("username");
    document.getElementById("var_username").innerText = "Logged as Admin: @" + username;
    document.getElementById("invalid_productName").style.display = "none";
    document.getElementById("invalid_productDate").style.display = "none";
    document.getElementById("invalid_productDescription").style.display = "none";
})
