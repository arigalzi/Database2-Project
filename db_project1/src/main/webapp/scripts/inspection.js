let date;

function makeCall(method, url, formElement, cback, reset = true) {
    var req = new XMLHttpRequest(); // visible by closure
    req.onreadystatechange = function() {
        cback(req)
    }; // closure
    req.open(method, url);
    if (formElement == null) {
        req.send();
    } else if(formElement instanceof FormData) {
        req.send(formElement);
    } else {
        req.send(new FormData(formElement));
        if ( reset === true) {
            formElement.reset();
        }
    }
}

function insertQuestionsAnswers(con, tableBody, tableHead){
    let numberOfRows = 0;
    console.log(con);
    if(con.length!==0) {
        for (let i = 0; i < con.length; i++) {
            let user = con[i].username;
            let question = con[i].questions;
            let answers = con[i].answers;
            let isCanceled = con[i].canceled;
            if (isCanceled === false) {
                numberOfRows = numberOfRows + 1;
            for (let k = 0; k < question.length; k++) {
                let row = tableHead.insertRow();
                for (let j = 0; j < 3; j++) {
                    let fillText = row.insertCell(j);
                    if (j === 0) fillText.innerText = user;
                    if (j === 1) fillText.innerText = question[k];
                    if (j === 2) fillText.innerText = answers[k];
                }
              }
            }
        }
    }
    if(numberOfRows === 0){
        let row = tableSubHead.insertRow();
        let notFilled = row.insertCell(-1);
        notFilled.innerText = "Sorry but nobody filled this questionnaire";
    }
}

function insertCancelledUsers(con,tableCancHead, tableCancBody){
    let numberOfCanceled = 0;
    if(con.length!==0){
        for (let i = 0; i <con.length ; i++) {
            let user = con[i].username;
            let isCanceled = con[i].canceled;
            if(isCanceled === true){
                numberOfCanceled = numberOfCanceled + 1;
                let row = tableCancBody.insertRow();
                cell = row.insertCell(0);
                cell.innerText = user;
            }
        }
    }
    if(numberOfCanceled === 0){
        let row = tableCancHead.insertRow();
        let noFilled = row.insertCell(-1);
        noFilled.innerText = "Sorry but nobody cancelled this questionnaire";
    }

}

function populateTable(con,tableSubHead, tableSubBody, tableCancHead, tableCancBody){
    console.log(con);
    insertQuestionsAnswers(con, tableSubBody, tableSubHead);
    insertCancelledUsers(con, tableCancBody, tableCancHead);
}


function manageSearch()
{
    let form = document.getElementById("form-inspection");
    date = document.getElementById("date").getAttribute("date");
    console.log(date +"  -> first");
    makeCall("POST", "./Inspection", form,
        function (req) {
            if (req.readyState === 4) {
                let message = req.responseText;
                let con = JSON.parse(message);
                if (req.status === 200) {
                    let username = localStorage.getItem("username");
                    let admin = localStorage.getItem("isAdmin");
                    showUsername(admin, username);

                    const tableHead = document.getElementById("id_Inspection_Head");
                    const tableBody = document.getElementById("id_Inspection_tableBody");
                    const tableCancHead = document.getElementById("id_Cancel_Head");
                    const tableCancBody = document.getElementById("id_Cancel_tableBody");

                    populateTable(con, tableHead, tableBody, tableCancHead, tableCancBody);

                    date = con[1].prodDate.split(", 12:00:00");
                    console.log(date +"  -> second");

                    document.getElementById("deletion").innerHTML =
                        "<button id=\"#buttonDeletion\" class=\"btn btn-secondary\" style=\"margin-left: -40px\"\n" +
                        "type=\"button\" onclick=\"manageDelete()\">" + "Delete Questionnaire Data </button>";
                }

                }
                else if(req.status === 400){
                        showMessage("I_error_message", "You can only search a past data")
                }
            }
        );
}

function manageDelete()
{
    console.log(date+"  -> third ");

    makeCall("GET", "./Deletion?date="+ date, null,
        function(request) {
            if (request.readyState === 4 && request.status === 200) {
                window.location.assign("../db_project1_war_exploded/cancelGreetings.html");
            }
            else if(request.status === 400){
                showMessage("D_error_message", "You can only cancel a past data")
            }
        }
    );
}

function showMessage(html_id, message){
    let element = document.getElementById(html_id);
    element.innerText = message;
}



function showUsername(admin,username) {
    if (admin === false) {
        document.getElementById("var_username").innerText = "Logged in: @" + username;
    }
    else{
        document.getElementById("var_username").innerText = "Logged as Admin: @" + username;
    }
}