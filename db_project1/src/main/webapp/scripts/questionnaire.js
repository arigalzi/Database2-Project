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

function manageRequest(method,url,element,requestName,responseFunc) {
    var  request= new XMLHttpRequest();

    // Process the server response assigning a function to the variable
    request.onreadystatechange = function () {
        responseFunc(request);
    };
    /*after declaring what happens
    when you receive the response, you need to actually make the request*/
    request.open(method, url);
    var submission;
    switch (requestName) {
        case "cancel":
            submission = false;
            if (element == null) {
                request.send();
            } else
                request.setRequestHeader("cancelled", submission);
            request.send(new FormData(element));
            break;
        case "submit":
            submission = true;
            if (element == null) {
                request.send();
            } else
                request.setRequestHeader("submitted", submission)
            request.send(new FormData(element));
            break;
        default:
            if (element == null) {
                request.send();
            } else
                request.send(new FormData(element));
            break;

    }

}

/**
 * It is used to insert the mandatory questions in the HTML
 * @param questionList
 */
function addQuestions(questionList){

    let container = document.getElementById("id_question_container");
    container.innerHTML = "";
    questionList.forEach((question) => {

        let questionBox = document.createElement("div");
        questionBox.className = "mandatory-question";
        questionBox.innerHTML =
            "             <label class=\"col-form-label\">" + question + "</label>\n" +
            "                   <div class=\"row\">\n" +
            "                      <div class=\"col\">\n" +
            "                           <input style=\"text-align: center;\" name=\"mandatory\" id=\"answer\" placeholder=\"Answer\" required></input>\n" +
            "                           <div class=\"invalid-tooltip\">\n" +
            "                                Please provide a valid answer. This field is mandatory.\n" +
            "                           </div>\n" +
            "                        </div>\n" +
            "                     </div>\n"
        container.appendChild(questionBox);
    });
}
function showError(){

}

function showMessage(html_id, message){
    let element = document.getElementById(html_id);
    element.innerText = message;
}

function hideMessage(html_id){
    let element = document.getElementById(html_id);
    element.innerText = "";
}

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



/**
 * Here if the user clicks cancel we reset the forms and go back to the Mandatory form
 * only if the request is managed by the server and status is OK
 */
function manageForms(button_type){
    let action = button_type.textContent;
    let form1 = document.getElementById("mandatory_form");
    let form2 = document.getElementById("optional_form");
    manageRequest("POST", './AnswerData', form1, action,
      function(request,action){
        switch (action){
            case "Cancel":
                if (request.readyState === 4 && request.status === 200) {
                    document.getElementById("mandatory_form").reset();
                    showMandatoryForm();
                    showMessage("error_message","The form has been canceled successfully");
                }
                else{
                    showMessage("error_message","Error in cancelling the form")
                }
                break;
            case "Submit":
                if (request.readyState === 4){
                    if(request.status === 200){
                        window.location.assign("../thanks.html");
                    }
                    else if(request.status === 400){
                        window.location.assign("../banned.html");
                    }
                }
                break;

        }

      });
    manageRequest("POST", './AnswerData', form2, "cancel",
        function(request){
            if (request.readyState === 4 && request.status === 200) {
                document.getElementById("optional_form").reset();
                showMandatoryForm();
                showMessage("error_message","The form has been canceled successfully");
            }
            else{
                showMessage("error_message","Error in cancelling the form")
            }
        })


}



window.addEventListener('load', () => {
    initForms();
    console.log('page is fully loaded');
    manageRequest("GET","./QuestionnaireData",null,null,function(request){
        if (request.readyState === 4 && request.status === 200) {
            let message = request.responseText;
            let parsed = JSON.parse(message);
            console.log(parsed);
            addQuestions(parsed);
        }
        else {
            switch (request.status) {
                case 400: // bad request
                    showMessage("error_message","Bad request");
                    break;
                case 401: // unauthorized
                    showMessage("error_message","unauthorized");
                    break;
                case 500: // server error
                    showMessage("error_message","internal server error");
                    break;
            }
        }
    });
});




//We need to handle when the user inserts the input