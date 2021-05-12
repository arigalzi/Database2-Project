
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
    if(con.completed.length!==0){
        let row = tableHead.insertRow();
        let answers = row.insertCell(0);
        answers.innerText = "Question";
        let i = 1;
        Object.keys(con.answers).forEach(function (k) {
            answers = row.insertCell(i);
            answers.innerText = "Answer of " + answers[k];
            i++;
        })

        Object.keys(con.questions).forEach(function (k) {
            let row = tableBody.insertRow();
            let answers = row.insertCell(0);
            answers.innerText = con.questions[k];
            let i = 1;
            Object.keys(con.answers).forEach(function (k) {
                answers = row.insertCell(i);
                answers.innerText = con.answers[k].getAttribute();
                i++;
            })
        })
    }
    else{
        let row = tableSubHead.insertRow();
        let noFilled = row.insertCell(-1);
        noFilled.innerText = "Nobody submitted this questionnaire";
    }
}

function insertCancelledUsers(con,tableCancHead, tableCancBody){
    if(con.canceled.length!==0){
        let row = tableCancHead.insertRow();
        let users = row.insertCell(0);
        users.innerText = "User Have Canceled Questionnaire";

        Object.keys(con.canceled).forEach(function (k) {
            let row = tableCancBody.insertRow();
            users = row.insertCell(0);
            users.innerText = con.canceled[k];
        })

    }
    else{
        let row = tableSubHead.insertRow();
        let noFilled = row.insertCell(-1);
        noFilled.innerText = "Nobody cancelled this questionnaire";
    }

}

function populateTable(con,tableSubHead, tableSubBody, tableCancHead, tableCancBody){
    if (con.completed.length===0 && con.canceled.length===0) {
        let row = tableSubHead.insertRow();
        let noFilled = row.insertCell(-1);
        noFilled.innerText = "Sorry but nobody filled or cancelled this questionnaire";
    }
    else{
            insertQuestionsAnswers(con, tableSubBody, tableSubHead);

            insertCancelledUsers(con, tableCancBody, tableCancHead);
    }
}

buttonI.addEventListener("click", () => {
    let form = document.getElementById("form-inspection");

        makeCall("POST", "./Inspection", form,
            function(req) {
                if (req.readyState === 4) {
                    let message = req.responseText;
                    if (req.status === 200) {
                        let con = JSON.parse(message);
                        let username = localStorage.getItem("username");
                        let admin = localStorage.getItem("isAdmin");
                        showUsername(admin,username);

                        document.getElementById("id_product_title").innerText="Title\n";
                        document.getElementById("id_product_description").innerText="Description\n";
                        document.getElementById("id_product_title").innerText = con.prodName;
                        document.getElementById("id_product_date").innerText = con.date.split(", 12:00:00")[0];
                        document.getElementById("id_product_image").src = "data:image/png;base64," + con.encodedImg;
                        document.getElementById("id_product_description").innerText = con.prodDescription;
                        document.getElementById("#date").setAttribute("date",con.date);

                        const tableHead = document.getElementById("id_tableSubHead");
                        const tableBody = document.getElementById("id_tableSubBody");
                        const tableCancHead = document.getElementById("id_tableCancHead");
                        const tableCancBody = document.getElementById("id_tableCancBody");

                        populateTable(con,tableHead, tableBody, tableCancHead, tableCancBody);

                        document.getElementById("deletion").innerHTML =
                            "<button id=\"#buttonDeletion\" className=\"btn btn-secondary\" style=\"margin-left: -40px\"\n" +
                            "type=\"submit\">Delete</button>";

                    }
                }
                else if(request.status === 400){
                        showMessage("error_message", "You can only search a past data")
                }
            }
        );
});

buttonD.addEventListener("click", () => {
    let form = document.getElementById("form-deletion");

        makeCall("POST", "./Deletion", form,
            function(request) {
                if (request.readyState === 4 && request.status === 200) {
                    window.location.assign("../db_project1_war_exploded/cancelGreetings.html");
                }
                else if(request.status === 400){
                    showMessage("error_message", "You can only cancel a past data")
                }
                else {
                    showMessage("error_message", "Error in canceling the product")
                }
            }
        );

});

function showUsername(admin,username) {
    if (admin === false) {
        document.getElementById("var_username").innerText = "Logged in: @" + username;
    }
    else{
        document.getElementById("var_username").innerText = "Logged as Admin: @" + username;
    }
}