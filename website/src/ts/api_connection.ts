import { UserPlant } from "./types";

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

export async function addPlantToGardenUser(){

}

//TODO FIX THIS IMPLEMENTATION
export async function getUserLibrary(userId: string, token: string){
    let status: number = 404;
    let data: Promise<any> | null;

    try{
        const response = await fetch(
                URL+"/library/"+userId, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(token),
            });

        status = response.status;
        data = response.json()
        
        
    }catch(error){
        console.error("Login failiure: ", error);
    }

    return status;
}

export async function postPlantToUserLibary(userId: string, token: string, nickname: string, plantID: number) {
    let status: number = 404;
    try{
        const response = await fetch(
            URL+"/library/"+userId+"?token="+token+"&plantID="+plantID+"&nickname="+nickname,{
                method: "POST",
                headers: {"Content-Type": "application/json",
            }}
        )
        status = response.status;
    }catch(error){
        console.error("Plant was not added: ", error);
    }
    return status;
    
}





