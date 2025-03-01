//listener for sign in text
document.addEventListener("DOMContentLoaded", () => {
    const signInLink = document.querySelector(".already-registered") as HTMLAnchorElement;
    signInLink?.addEventListener("click", (event) => {
        event.preventDefault();
        window.location.href = "login-page.html";
    });
});