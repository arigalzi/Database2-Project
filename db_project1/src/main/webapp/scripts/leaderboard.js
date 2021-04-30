
/**
 * AJAX call management
 */

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


window.addEventListener("load", () => {
    makeCall("GET", "./Leaderboard", null,
        function(req) {
            if (req.readyState === 4) {
                var message = req.responseText;
                if (req.status === 200) {
                    var con = JSON.parse(message);
                    if(con != null) {
                        const table = document.getElementById("id_tableBody");
                        con.forEach((k) => {
                            let row = table.insertRow();
                            let username = row.insertCell(0);
                            username.innerHTML = k;
                            let score = row.insertCell(1);
                            score.innerHTML = k;
                        })
                    }
                    else{
                        document.getElementById("id_Leaderboard_Table").innerHTML =
                            "<div " +
                            " <blockquote class=\"blockquote text-center\">\n" +
                            "  <p class=\"mb-0\">Sorry no product for today, we will be back soon!</p>\n" +
                            "  <footer class=\"blockquote-footer\">Staff</footer>\n" +
                            " </blockquote>\n" +
                            "</div>";
                    }
                }
            } else {
                //display error
            }
        }
    );

});

/*
 * It is used to insert the mandatory questions in the HTML
 * @param questionList

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


*/

