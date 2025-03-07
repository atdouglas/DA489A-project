import { registerUser } from "./api_connection";

const emailInput = document.getElementById("emailInput") as HTMLInputElement;
const passwordInput = document.getElementById("passwordInput") as HTMLInputElement;
const confirmPasswordInput = document.getElementById("confirmPasswordInput") as HTMLInputElement;
const securityQuestionInput = document.getElementById("securityQuestionInput") as HTMLInputElement;
const securityAnswerInput = document.getElementById("securityAnswerInput") as HTMLInputElement;
const registerButton = document.getElementById("registerButton") as HTMLButtonElement;
const registerForm = document.getElementById("registerForm") as HTMLFormElement;


document.addEventListener("DOMContentLoaded", () => {
    const signInLink = document.querySelector(".already-registered") as HTMLAnchorElement;
    signInLink?.addEventListener("click", (event) => {
        event.preventDefault();
        window.location.href = "login-page.html";
    });

    confirmPasswordInput.addEventListener("input", validatePasswordMatch);
});

registerForm.addEventListener("submit", (event) => {
    performRegistration(event)
})

async function performRegistration(event: SubmitEvent){
    event.preventDefault()

    let email: string = emailInput.value;
    let password: string = passwordInput.value;
    let confirmPassword: string = confirmPasswordInput.value;
    let securityQuestion: string = securityQuestionInput.value;
    let securityAnswer: string = securityAnswerInput.value;

    if(!verifyPassword(password, confirmPassword)){
        confirmPasswordInput.setCustomValidity("Passwords does not match.")
        confirmPasswordInput.reportValidity();
        return;
    }else{
        confirmPasswordInput.setCustomValidity("")
        confirmPasswordInput.reportValidity();
    }

    const success: number = await registerUser(email, password, securityQuestion, securityAnswer);

    console.log(success);

    if(success == 200){
        window.location.href = "login-page.html?showToast=loginSuccessful"; 
    }else{
        //TODO Make this error handling look better.
        alert("An error occured during registration. Please try again.\n" +
                "Status code: " + success);
    }

}

function validatePasswordMatch() {
    let password: string = passwordInput.value;
    let confirmPassword: string = confirmPasswordInput.value;

    if(password == "" || confirmPassword == ""){
        return;
    }

    if (!verifyPassword(password, confirmPassword)) {
        confirmPasswordInput.setCustomValidity("Passwords do not match.");
    } else {
        confirmPasswordInput.setCustomValidity("");
    }

    confirmPasswordInput.reportValidity();
}

function verifyPassword(password: string, confirmPassword: string): boolean{
    if(password !== confirmPassword){
        return false;
    }
    return true;
}



