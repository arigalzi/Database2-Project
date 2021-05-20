

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
        responseFunc(request,requestName);
    };
    /*after declaring what happens
    when you receive the response, you need to actually make the request*/
    request.open(method, url);
    var submission;

    if(requestName == null) {
        if (element == null) {
            request.send();
        } else {
            request.send(new FormData(element));
        }
    }
    else if(requestName.trim() === 'Cancel') {
        submission = false;
        if (element == null) {
            request.send();
        } else
            request.setRequestHeader("submitted", submission);
        request.send(new FormData(element));
    }
    else if(requestName.trim() === 'Submit') {
        submission = true;
        if (element == null) {
            request.send();
        } else {
            request.setRequestHeader("submitted", submission)
            request.send(new FormData(element));
        }
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
            "                   <div class=\"form-group\">\n" +
            "                      <div class=\"col\">\n" +
            "                           <input style=\"text-align: center;\" name=\"mandatory\" id=\"answer\" placeholder=\"Insert your answer\" required/>\n" +
            "                           <div class=\"invalid-tooltip\">\n" +
            "                                Please provide a valid answer. This field is mandatory.\n" +
            "                           </div>\n" +
            "                        </div>\n" +
            "                     </div>\n"
        container.appendChild(questionBox);
    });
}

function showMessage(html_id, message){
    let element = document.getElementById(html_id);
    element.innerText = message;
}

function showMandatoryForm(){
    let mandatory = document.getElementById("mandatory_form");
    let optional = document.getElementById("optional_form");
    optional.style.display = "none"; //element is not displayed
    mandatory.style.display = "block";
}

function showNoForms(){
    let mandatory = document.getElementById("mandatory_form");
    let optional = document.getElementById("optional_form");
    optional.style.display = "none"; //element is not displayed
    mandatory.style.display = "none";
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
    console.log(action)
    let form = document.getElementById("questionnaire_form");
    for (var pair of new FormData(form).entries()) {
        console.log(pair[0]+ ', ' + pair[1]);
    }

    //Manage mandatory form
    manageRequest("POST", './AnswerData', form, action,
      function(request,action){
        //When the server has responded that we reset the form
          if(action.trim() === "Cancel") {
              if (request.readyState === 4 && request.status === 200) {
                  document.getElementById("mandatory_form").value = "";
                  showNoForms();
                  showMessage("error_message", "The form has been canceled successfully \n Go back to HomePage");
              } else {
                  showMessage("error_message", "Error in cancelling the form")
              }
          }
          else if(action.trim() === "Submit") {
              if (request.readyState === 4) {
                  if (request.status === 200) {
                      window.location.assign("../db_project1_war_exploded/greetings.html");
                  } else if (request.status === 403) {
                      window.location.assign("../db_project1_war_exploded/banned.html");
                  }
                  else if (request.status === 406) { // Not acceptable request
                      showMessage("error_mandatory", "Please respond to the mandatory questions to proceed")
                  }
              }
          }

      });


}



window.addEventListener('load', () => {
    let username = localStorage.getItem("username");
    let isAdmin = localStorage.getItem("isAdmin");
    showUsername(isAdmin,username);
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
                    showNoForms();
                    showMessage("error_message","Error: Questionnaire already completed \n Go back to HomePage");
                    break;
                case 406: // not acceptable
                    showNoForms();
                    showMessage("error_message","Error: The questionnaire of the day not created yet by Admin");
                    break;
                case 401: // unauthorized
                    showMessage("error_message","unauthorized");
                    break;
                case 403: // banned
                    showNoForms();
                    showMessage("error_message","Unable to process your request you have been BANNED");
                    break;
                case 500: // server error
                    showMessage("error_message","internal server error");
                    break;
            }
        }
    });
});

function showUsername(admin,username) {
    console.log("In questionnaire: ", admin, username);
    if (String(admin) === "false") {
        document.getElementById("var_username").innerText = "Logged in: @" + username;
    }
    else{
        document.getElementById("var_username").innerText = "Logged as Admin: @" + username;
    }
}


//We need to handle when the user inserts the input


function clearLocalStorage(){
    localStorage.clear();
    console.log("LocStorage cleared..");
}
