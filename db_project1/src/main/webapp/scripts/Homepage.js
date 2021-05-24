
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
    makeCall("GET", "./GoToHomepage", null,
        function(req) {
            if (req.readyState === 4) {
                var message = req.responseText;
                if (req.status === 200) {
                    var con = JSON.parse(message);
                    localStorage.setItem("username",con.username);
                    localStorage.setItem("isAdmin",con.admin);
                    console.log(con.username,con.admin);
                    showUsername(con.admin,con.username);

                    console.log(con.userStatus);

                    if(con.userStatus === "NOT_AVAILABLE"){
                        document.getElementById("id_product_title").innerHTML = "No Product";
                        document.getElementById("id_product_image").innerHTML = "";
                        document.getElementById("id_product_description").innerHTML = "";
                        let newRow = document.getElementById("No_Review_info_available");
                        newRow.innerHTML = "<th colSpan=\"3\" style=\"width:100%;font-weight:normal\">No reviews present for this Product</th>"
                    }
                    /*else if (con.userStatus === "BANNED") {
                        document.getElementById("prod_block").innerHTML =
                            "  <p class=\"mb-0\">You've been banned due to \"inappropriate language during the questionnaire submission\"</p>\n";
                    }*/
                    else {
                        document.getElementById("id_product_title").innerText = con.prodName;
                        document.getElementById("id_product_image").src = "data:image/png;base64," + con.encodedImg;
                        document.getElementById("id_product_description").innerText = con.prodDescription;
                        const table = document.getElementById("id_Review_Body");
                        if(con.reviews != null){
                            console.log(con.reviews);
                            Object.keys(con.reviews).forEach(function (k) {
                                let row = table.insertRow();
                                let review = row.insertCell(0);
                                review.innerHTML = con.reviews[k];
                            })
                        }
                        else{
                            let newRow = document.getElementById("No_Review_info_available");
                            newRow.innerHTML = "<th colSpan=\"3\" style=\"width:100%;font-weight:normal\">No reviews present for this Product</th>"
                        }

                    }
                }
            } else {
                //display error
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


function clearLocalStorage(){
    localStorage.clear();
    console.log("LocStorage cleared..");
}
