document.addEventListener("DOMContentLoaded", () => {
    const emailInput = document.getElementById("email-input") as HTMLInputElement;
    const nextButton = document.getElementById("next-button") as HTMLButtonElement;
    const securityQuestion = document.getElementById("security-question") as HTMLParagraphElement;
    const securityAnswerInput = document.getElementById("security-answer") as HTMLInputElement;
    const securityAnswerButton = document.getElementById("submit-button") as HTMLButtonElement;
    const newPasswordInput = document.getElementById("new-password") as HTMLInputElement;
    const confirmPasswordInput = document.getElementById("confirm-password") as HTMLInputElement;
    const resetPasswordButton = document.getElementById("reset-password-button") as HTMLButtonElement;
    const toggleNewPassword = document.getElementById("toggle-new-password") as HTMLSpanElement;
    const toggleConfirmPassword = document.getElementById("toggle-confirm-password") as HTMLSpanElement;

    const step1 = document.getElementById("step-1") as HTMLDivElement;
    const step2 = document.getElementById("step-2") as HTMLDivElement;
    const step3 = document.getElementById("step-3") as HTMLDivElement;

    toggleNewPassword?.addEventListener('click', () => {
        if(newPasswordInput.type === "password"){
            newPasswordInput.type = "text";
        }else{
            newPasswordInput.type = "password";
        }
    });

    toggleConfirmPassword?.addEventListener('click', () => {
        if(confirmPasswordInput.type === "password"){
            confirmPasswordInput.type = "text";
        }else{
            confirmPasswordInput.type = "password";
        }
    });
    
    let userEmail: string;
    let securityQuestionText: string;

    nextButton?.addEventListener('click', async () => {
        userEmail = emailInput.value;

        if(!userEmail){
            alert("Please enter your email");
            return;
        }

        try {
            const response = await fetch(`http://localhost:7888/security_question?email=${encodeURIComponent(userEmail)}`, {
                method: 'GET',
                headers: {
                    "Content-Type": "application/json",
                }
            });
            if (response.ok){
                const q = await response.json();
                
                securityQuestionText = q; 
                securityQuestion.textContent = securityQuestionText;

                step1.style.display = "none";
                step2.style.display = "block";
            }else {
                const error  = await response.text();
                alert(error);
            }
        }catch (error) {
            console.error(error);
            alert('An error occured, please try again');
        }
    });

    securityAnswerButton?.addEventListener('click', async () => {
        const answer = securityAnswerInput.value;

        if(!answer){
            alert('Dont play, enter your answer');
            return;
        }

        try {
            const response = await fetch('http://localhost:7888/security_question', {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    email: userEmail,
                    securityAnswer: answer
                })
            });
            if (response.ok){
                step2.style.display = "none";
                step3.style.display = "block";
            }else {
                const error = response.text();
                alert(error);
            }
        }catch (error) {
            console.error(error);
            alert('An error occured, please try again');
        }
    });

    resetPasswordButton?.addEventListener('click', async () => {
        const newPassword = newPasswordInput.value;
        const confirmPassword = confirmPasswordInput.value;

        if(newPassword !== confirmPassword){
            alert('Passwords do not match, dubbelcheck spelling');
            return;
        }

        try {
            const response = await fetch('http://localhost:7888/update_password', {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    email: userEmail,
                    securityAnswer: securityAnswerInput.value,
                    password: newPassword
                })
            });
            if (response.ok){
                alert('Password has been reset');
                window.location.href = "../html/login-page.html";
            }else {
                const error = response.text();
                alert(error);
            }
        }catch (error) {
            console.error(error);
            alert('An error occured, please try again');
        }
    });

});