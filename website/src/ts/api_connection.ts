const URL: string = "http://localhost:7888"

export async function registerUser(email: string, password: string, securityQuestion: string, securityAnswer: string){
    const user = {
        email: email,
        password: password,
        securityQuestion: securityQuestion,
        securityAnswer: securityAnswer
    }
    let status: number = 404;

    try{
        const response = await fetch(
                URL+"/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(user),
            });

        status = response.status;
    }catch(error){
        console.error("Login failiure: ", error);
    }

    return status;
}



