
/*window.addEventListener("load", () => {
          let admin = localStorage.getItem("isAdmin");
          let username = localStorage.getItem("username");
          showAdminUsername(admin,username);

        }

);*/

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



function clearLocalStorage(){
    localStorage.clear();
    console.log("LocStorage cleared..");
}


function showAdminUsername(admin,username) {
        console.log("In admin show username", admin,username);
        if (admin === false) {
                document.getElementById("var_username").innerText = "Logged in: @" + username;
        }
        else{
                document.getElementById("var_username").innerText = "Logged as Admin: @" + username;
        }
}

