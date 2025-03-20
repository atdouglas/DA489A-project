const changePasswordButton = document.getElementById("change-password") as HTMLButtonElement;
const notificationSettingsButton = document.getElementById("notification_settings") as HTMLButtonElement;


if (changePasswordButton){
    changePasswordButton.addEventListener('click', () => {
        window.location.href = "/src/html/forgot-password-page.html";
    });
}


/*notificationSettingsButton.addEventListener('onclick', () => {
    //window.location.href = "http://localhost:5173/src/html/'skriv framtida url'.html";
});*/