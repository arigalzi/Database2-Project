
let buttonI = document.querySelector('#buttonInspection');
let buttonD = document.querySelector('#buttonDeletion');


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


                    document.getElementById("deletion").innerHTML =
                        "<button id=\"#buttonDeletion\" class=\"btn btn-secondary\" style=\"margin-left: -40px\"\n" +
                        "type=\"button\">Delete Questionnaire Data</button>";

                }
                else {
                    document.getElementById("id_errorTypeI").innerText = con.errorType;
                    document.getElementById("id_errorInfoI").innerText = con.errorInfo;
            }

            }
        }
    );
}

/*buttonD.addEventListener("click", () => {
    //var text = window.location.hash.substring(1);
    let form = document.getElementById("form-deletion");

        makeCall("GET", "./Deletion", form,
            function(req) {
                let message = req.responseText;
                if (req.readyState === 4) {
                    if (req.status === 200) {
                        let con = JSON.parse(message);
                        let username = localStorage.getItem("username");
                        let admin = localStorage.getItem("isAdmin");
                        showUsername(admin,username);
                    }
                } else {
                        let con = JSON.parse(message);
                        document.getElementById("id_errorTypeD").innerText = con.errorType;
                        document.getElementById("id_errorInfoD").innerText = con.errorInfo;

                }
            }
        );

});*/

function showUsername(admin,username) {
    if (admin === false) {
        document.getElementById("var_username").innerText = "Logged in: @" + username;
    }
    else{
        document.getElementById("var_username").innerText = "Logged as Admin: @" + username;
    }
}