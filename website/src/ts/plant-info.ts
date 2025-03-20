import { Plant } from './types'
import { getCookie } from "./cookieUtil";
import { UserPlant } from './types';
import { postPlantToUserLibrary } from './api_connection';

const commonNameEl = document.querySelector('.common-name') as HTMLElement;
const scientificNameEl = document.querySelector('.scientific-name') as HTMLElement;
const familyNameEl = document.querySelector('.family-name') as HTMLElement;
const descriptionTextEl = document.querySelector('.description-text') as HTMLElement;
const plantImageEl = document.querySelector('.plant-image img') as HTMLImageElement;
const maintenanceEl = document.getElementById('maintenance-value') as HTMLElement;
const poisonsEl = document.getElementById('poisons-value') as HTMLElement;
const wateringEl = document.getElementById('watering-value') as HTMLElement;
const careGuidesBtn = document.getElementById('care-guides-btn') as HTMLButtonElement;

const addToGardenButton = document.querySelector('.add-button') as HTMLButtonElement;

const confirmAddModal = document.getElementById('confirmAddModal') as HTMLElement;
const yesAddButton = confirmAddModal.querySelector('.yes-add-button') as HTMLButtonElement;
const noAddButton = confirmAddModal.querySelector('.no-add-button') as HTMLButtonElement;

const token : string | null= getCookie("accessToken");
document.addEventListener('DOMContentLoaded', async () => {
    const params = new URLSearchParams(window.location.search);
    const plantId = params.get('id');
    if (!plantId) return;

    try {
        const response = await fetch(`http://localhost:7888/plants/${plantId}`);
        if (!response.ok) throw new Error('Plant not found');
        const plant : Plant = await response.json();
        setupPlantDescription(plant)
        setupCareGuidesBtn(plant)

        // Set up add-to-garden with confirmation modal
        if(token != null){
            addToGardenButton.addEventListener('click', () => {
                plantToAdd = plant;
                
                const nicknameInput = document.getElementById('plantNickname') as HTMLInputElement;
                if (nicknameInput) {
                    nicknameInput.value = "";
                }
                confirmAddModal.style.display = 'flex';
            });

        }else{
            addToGardenButton.addEventListener('click', () => {
                window.location.href = "/src/html/login-page.html";
                //kanske lägga till en popup med att man måste vara inloggad?
            })
        }
    } catch (error) {
        console.error('Error loading plant:', error);
    }

});

// Global variable for storing the plant to add
let plantToAdd: Plant | null = null;

function setupPlantDescription(plant: Plant){
        commonNameEl.textContent = plant.common_name || "Unknown common name";
        scientificNameEl.textContent = plant.scientific_name || "Unknown scientific name";
        familyNameEl.textContent = plant.family || "Unknown family";
        descriptionTextEl.textContent = plant.description || "No description available.";
        plantImageEl.src = plant.image_url || "../../public/plant-1573.png";
        maintenanceEl.textContent = plant.maintenance || "No information";
        poisonsEl.textContent = plant.poisonous_to_pets ? "Yes" : "No";

        const msInADay = 86400000;
        const days = plant.watering_frequency ? plant.watering_frequency / msInADay : 0;
        const daysRounded = Math.round(days * 10) / 10;
        wateringEl.textContent = daysRounded > 0 ? `Every ${daysRounded} days` : "No information";
}

yesAddButton.addEventListener('click', () => {
    if (plantToAdd) {
        const nicknameInput = document.getElementById('plantNickname') as HTMLInputElement;
        let nickname = nicknameInput ? nicknameInput.value.trim() : "";
        
        if (nickname === null || nickname === "" || nickname === undefined) {
            if(plantToAdd.common_name != null){
                addToGarden(plantToAdd,plantToAdd.common_name)
            }
        }else{
            addToGarden(plantToAdd, nickname);
        }
        showToast(`Perfect, added the plant to the garden!`);
        plantToAdd = null;
    }
    confirmAddModal.style.display = 'none';
});

noAddButton.addEventListener('click', () => {
    plantToAdd = null;
    confirmAddModal.style.display = 'none';
});


function setupCareGuidesBtn(plant : Plant){
    careGuidesBtn.addEventListener('click', () =>{
        window.location.href = "care-guides-page.html?plantid=" + plant.id + "&plantName=" + plant.common_name; 
    });
}

const addToGarden = (plant: Plant, nickname : string) => {
    const userid :string|null = getCookie("userId")
    const accessToken :string|null = getCookie("accessToken")
    
    if (userid!= null && accessToken != null && (nickname != null)){
        postPlantToUserLibrary(userid,accessToken,nickname,plant.id)
    }else{
        console.log("failed to addToGarden")
    }
};

function showToast(message: string) {
    const toast = document.getElementById('toast') as HTMLElement;
    if (toast) {
        toast.textContent = message;
        toast.style.display = 'block';
        void toast.offsetWidth;
        toast.classList.add('show');
        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => {
                toast.style.display = 'none';
            }, 500);
        }, 3000);
    }
}
