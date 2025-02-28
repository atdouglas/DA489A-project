document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.querySelector(".loginbox") as HTMLDivElement;
    const emailInput = document.querySelector('input[type="email"]') as HTMLInputElement;
    const passwordInput = document.querySelector('input[type="password"]') as HTMLInputElement;
    const loginButton = document.querySelector('button[type="submit"]') as HTMLButtonElement;
    const registerButton = document.getElementById("register-button");

    registerButton?.addEventListener("click", () => {
        window.location.href = "register-page.html";
    });

    loginForm?.addEventListener("submit", async(event) => {
        event.preventDefault();
        const email = emailInput.value;
        const password = passwordInput.value;
        const user = {email, password};

        try {
            const response = await fetch("http://localhost:7888/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(user),
            });

            if(response.ok){
                const data = await response.json();
                console.log("Successful login:" + data); //TODO remove

                localStorage.setItem("accessToken", data.accessToken);
                window.location.href= "/home-page.html";
            }else {
                const error = await response.text();
                alert(`Login failed: ${error}`);
            }
        }catch (error){
            console.error("Login failiure: ", error);
            alert("An error occured during login. Please try again");
        }
    });
});