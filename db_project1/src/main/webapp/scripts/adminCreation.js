let questionCounter = 0;
let reviewCounter = 0;

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
    let sendForm = new FormData(form);
    let imageElem = document.getElementById("img");
    for (var pair of sendForm.entries()) {
        if(pair[0] === "productImage"){
            console.log(pair[0]+ ', ' + pair[1])
        }
        console.log(pair[0]+ ', ' + pair[1]);
    }
    console.log(sendForm);
    console.log(sendForm.get("productImage"));
    //Manage creation form
    manageRequest("POST", './CreationData', form,
        function(request){
            //When the server has responded that we reset the form
            if (request.readyState === 4 && request.status === 200) {
                window.location.assign("../db_project1_war_exploded/adminGreetings.html");
            }
            else if(request.status === 400){
                showMessage("error_message", "Error: Product of the day already assigned for this date")
            }
            else {
                showMessage("error_message", "Error in creating the product")
            }

        });
//TODO GESTIRE COME BAD REQUEST ANCHE INPUT VUOTI ANCHE PER QUESTIONARIo
//TODO Gli ID se la tabella è vuota continuano ad incrementarsi e non ripartono da 1

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