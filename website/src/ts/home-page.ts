import { Plant, UserPlant } from './types'
import { getCookie, clearCookie } from './cookieUtil';
import { getUserLibrary } from './api_connection';
import { deleteUserPlantFromLibrary } from './api_connection';

const plantsContainer = document.querySelector('.plants-container') as HTMLElement;
const addPlantCard = document.querySelector('.add-plant-card') as HTMLElement;
const confirmModal = document.getElementById('confirmModal') as HTMLElement;
const yesButton = confirmModal.querySelector('.yes-button') as HTMLButtonElement;
const noButton = confirmModal.querySelector('.no-button') as HTMLButtonElement;
let cardToDelete: HTMLElement | null = null;
let plantToDelete: string | null = null;
const token: string | null = getCookie("accessToken");
const userId: string | null = getCookie("userId")

if(token === null){
    window.location.href = "/src/html/login-page.html"
}

document.addEventListener('DOMContentLoaded', () => {
    loadGarden();
});

addPlantCard.addEventListener('click', () => {

});

yesButton.addEventListener('click', () => {
    if (cardToDelete && plantToDelete) {
        cardToDelete.remove();
        cardToDelete = null;
        if (plantToDelete != null && userId != null && token !=null){
            deleteUserPlantFromLibrary(plantToDelete,userId,token)
        }else {
            console.log("Error the plant was not deleted")
        }
    }
    confirmModal.style.display = 'none';
});

noButton.addEventListener('click', () => {
    cardToDelete = null;
    plantToDelete = null;
    confirmModal.style.display = 'none';
});


async function loadGarden() {
    let plants: UserPlant[] | null = null;
    let statusCode: number | null = null;

    if(token != null && userId != null){
        const {data, status} = await getUserLibrary(userId, token)
        plants = data;
        statusCode = status;
        console.log("STATUS: " + statusCode)
    } 
    
    if (statusCode === 401) {
        console.error("You are unauthorized")
        if (token != null) {
            clearCookie(token);
        }
        window.location.href = "/src/html/login-page.html";
    }else if (statusCode === 419) {
        console.error("Your token expired")
        if (token != null) {
            clearCookie(token);
        }
        window.location.href = "/src/html/login-page.html";
    }

    if(plants == null){
        return
    }
    plants.forEach(plant => {
        createPlantCard(plant);
    });
    
}

function createPlantCard(plant: UserPlant) {
    const newCard = document.createElement('div');
    newCard.classList.add('plant-card');
    newCard.setAttribute('data-plant-id', plant.id.toString());

    const imageUrl = plant.image_url || "../../public/plant-1573.png";

    let mainName: string;
    let subTitle: string;
    let lastWatered: string;

    if (plant.nickname && plant.nickname.trim() !== "") {
        mainName = plant.nickname;
        subTitle = plant.common_name || "No common name";
    } else {
        mainName = plant.common_name || "No common name";
        subTitle = plant.scientific_name || "No scientific name";
    }

    if(plant.last_watered > 0){
        lastWatered = calculateLastWatered(plant.last_watered);
    }else{
        lastWatered = "0 h"
    }
    newCard.innerHTML = `
        <span class="delete-icon">🗑</span>
        <img src="${imageUrl}" alt="Plant" class="plant-image" />
        <h3 class="plant-name">${mainName}</h3>
        <p class="plant-subtitle">${subTitle}</p>
        <div class="plant-water-info">
          <span class="water-time">${lastWatered}</span>
          <button class="water-button">Water</button>
        </div>
    `;
    plantsContainer.insertBefore(newCard, addPlantCard);
    console.log(plant.user_plant_id)
    attachDeleteListener(newCard,plant.user_plant_id.toString());
    attachWaterButtonListener(newCard);
}

function calculateLastWatered(waterInMilli: number): string{
    const progress = Date.now() - waterInMilli
    const hours: number = (((progress/1000)/60)/60);
    const roundedHours: number = Math.ceil(hours);

    if(roundedHours < 24){
        return (roundedHours + "h")
    } else if (roundedHours == 24){
        return "1d"
    }else{
        const totalDays: number = Math.floor(roundedHours/24)
        const totalHours: number = roundedHours - (totalDays*24)

        if(totalHours == 0){
            return (totalDays + "d")
        }

        return (totalDays + "d " + totalHours + "h")
    }
}

//TODO Change this implementation. Should not be a delete button, should be a "more options" button instead where change nickname and delete exists.
function attachDeleteListener(card: HTMLElement, plantID : string): void {
    const deleteIcon = card.querySelector('.delete-icon') as HTMLElement;
    
    if (deleteIcon) {
        deleteIcon.addEventListener('click', (e: Event) => {
            e.stopPropagation();
            cardToDelete = card;
            plantToDelete = plantID
            confirmModal.style.display = 'flex';
        });
    }
}

//TODO Fix this to update last_watered on plant. (Needs to be implemented in the server and in api_connection first)
function attachWaterButtonListener(card: HTMLElement, ) {
    const waterButton = card.querySelector('.water-button') as HTMLElement;
    if(waterButton) {
        waterButton.addEventListener('click', (event: Event) => {
            
        })
    }
}


