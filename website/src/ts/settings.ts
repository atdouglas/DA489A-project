import { getNotificationsActivated, updateNotificationsActivated } from "./api_connection";
import { getCookie } from "./cookieUtil";

document.addEventListener('DOMContentLoaded', () => {
    const notificationButton = document.getElementById("notification-btn") as HTMLButtonElement;
    const statusIndicator = notificationButton.querySelector(".status");
    const userId = getCookie("userId");
    const token = getCookie("accessToken");
    let notifEnabled = true;

    if(!userId || !token){
        window.location.href = "/src/html/login-page.html";
        return;
    }

    getNotificationsActivated(userId, token).then(response => {
        if (response !== null){
            notifEnabled = response;
            updateButtonState();
        }
    });

    notificationButton.addEventListener('click', () => {
        notifEnabled = !notifEnabled;
        updateNotif(userId, token, notifEnabled).then(() => {
            updateButtonState();
            showToast(`Notifications ${notifEnabled ? 'enabled' : 'disabled'}`);
        }).catch(error => { 
            console.error("Update failed: ", error)
            showToast("Failed to update settings");
        });
    });
    

    function updateButtonState () {
        if(statusIndicator){
            statusIndicator.textContent = notifEnabled ? "On" : "Off";
            notificationButton.classList.toggle('inactive', !notifEnabled);
        }
    }

    const changePasswordButton = document.getElementById("change-password") as HTMLButtonElement;

    if (changePasswordButton){
        changePasswordButton.addEventListener('click', () => {
            window.location.href = "/src/html/forgot-password-page.html";
        });
    }

    function showToast(message: string){
        const toast = document.getElementById('toast');
        if(toast !== null){
            toast.textContent = message;
            toast.style.display = 'block';
            setTimeout(() => {
                toast.style.display = 'none';
            }, 2000);
        }
    }

    async function updateNotif(userId: string, token: string, enabled: boolean) {
        const response = await updateNotificationsActivated(userId, token, enabled);
    
        if(!response){
            //input error handling
        }
        return response;
    }
});
