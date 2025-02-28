
//listener for registerbutton
document.addEventListener("DOMContentLoaded", () => {
    const registerButton = document.getElementById("register-button");
    registerButton?.addEventListener("click", () => {
        window.location.href = "register-page.html";
    });
});