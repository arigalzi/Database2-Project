
/**
 * AJAX call management
 */

function displayError (errorText) {
    let error = document.getElementById("id_quest_error");
    error.textContent = errorText;
    error.style.display = "block";
}

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
    let username = localStorage.getItem("username");
    let isAdmin = localStorage.getItem("isAdmin");
    showUsername(isAdmin,username);
    makeCall("GET", "./Leaderboard", null,
        function(req) {
            if (req.readyState === 4) {
                var message = req.responseText;
                if (req.status === 200) {
                    var con = JSON.parse(message);
                    if(con != null) {
                        const table = document.getElementById("id_tableBody");
                        console.log(con);
                        con.forEach(function (value, k) {
                            let row = table.insertRow();
                            let position = row.insertCell(0);
                            position.innerHTML = k+1;
                            let usernamescore = row.insertCell(1);
                            usernamescore.innerHTML = value;
                        });
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
                displayError("Something went wrong during Leaderboard creation")
            }
        }
    );

});

function showUsername(admin,username) {
    if (String(admin) === "false") {
        document.getElementById("var_username").innerText = "Logged in: @" + username;
    }
    else{
        document.getElementById("var_username").innerText = "Logged as Admin: @" + username;
    }
}


function clearLocalStorage(){
    localStorage.clear();
    console.log("LocStorage cleared..");
}

