import { UserPlant, CareGuide } from "./types";

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

export async function getUserLibrary(userId: string, token: string){
    let status: number = 404;
    let data: UserPlant[] | null = null;

    try{
        const response = await fetch(
                URL+"/library/"+userId+"?token="+token, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                },
            });

        status = response.status;
        data = await response.json()
        console.log("User library fetched.")
        console.log(data)
        
        
    }catch(error){
        console.error("Login failiure: ", error);
    }

    return {data, status};
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

export async function updateUserPlantNickname(userId: string, token: string, nickname: string, userPlantId: number) {
    let status: number = 404;
    try{
        const response = await fetch(
            URL+"/library/"+userId+"/"+ userPlantId + "?token="+ token+ "&nickname="+ nickname,{
                method: "PATCH",
                headers: {"Content-Type": "application/json",
            }}
        )
        status = response.status;
    }catch(error){
        console.error("Nickname was not updated: ", error);
    }
    return status;
}

export async function updateUserPlantLastWatered(userId: string, token: string, last_watered: number , userPlantId: number) {
    let status: number = 404;
    try{
        const response = await fetch(
            URL+"/library/"+userId+"/"+ userPlantId + "?token="+ token+ "&last_watered="+ last_watered,{
                method: "PATCH",
                headers: {"Content-Type": "application/json",
            }}
        )
        status = response.status;
    }catch(error){
        console.error("Last watered was not updated: ", error);
    }
    return status;
}

export async function deleteUserPlantFromLibrary(userPlantId: number, userId: string, token: string) {
    let status: number = 404;
    try{
        const response = await fetch(
            URL+"/library/"+userId+"/"+userPlantId+"?token="+token,{
                method: "DELETE",
                headers: {"COntent-Type": "application/json",
            }}
        )
        status = response.status;
    } catch (error){
        console.error("Plant was not deleted: ", error)
    }
    return status;
}

export async function getCareGuides(plantID : number){
    let status: number = 404;
    let data: CareGuide[] | null = null;

    try{
        const response = await fetch(
                URL+"/plants/"+plantID+"/guides", {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                },
            });

        status = response.status;
        data = await response.json()
        console.log("Care guide fetched.")
        console.log(data)
        
        
    }catch(error){
        console.error("Failed to fetch care guide: ", error);
    }

    return data;
}



