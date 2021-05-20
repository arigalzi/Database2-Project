
window.addEventListener("load", () => {
          let admin = localStorage.getItem("isAdmin");
          let username = localStorage.getItem("username");
          showAdminUsername(admin,username);

        }

);

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

