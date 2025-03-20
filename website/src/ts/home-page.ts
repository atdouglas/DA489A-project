import { UserPlant } from './types'
import { getCookie, clearCookie } from './cookieUtil';
import { getUserLibrary, updateUserPlantLastWatered, updateUserPlantNickname } from './api_connection';
import { deleteUserPlantFromLibrary } from './api_connection';

const plantsContainer = document.querySelector('.plants-container') as HTMLElement;
const addPlantCard = document.querySelector('.add-plant-card') as HTMLElement;

const confirmModal = document.getElementById('confirmModal') as HTMLElement;
const yesDelButton = confirmModal.querySelector('.yes-button') as HTMLButtonElement;
const noDelButton = confirmModal.querySelector('.no-button') as HTMLButtonElement;

const changeNickModal = document.getElementById('change-nick-modal') as HTMLElement
const nickChangeButton = changeNickModal.querySelector('.nick-change-button') as HTMLButtonElement;
const nickCancelButton = changeNickModal.querySelector('.nick-cancel-button') as HTMLButtonElement;
const nickInput = changeNickModal.querySelector("#change-nick-input") as HTMLInputElement;
const nickError = changeNickModal.querySelector("#nickname-error") as HTMLParagraphElement;

const addPlantModal = document.querySelector('#add-plant-modal') as HTMLDivElement
const addPlantSearchBtn = addPlantModal.querySelector('.add-plant-search-button') as HTMLButtonElement;
const addPlantCancelBtn = addPlantModal.querySelector('.search-cancel-button') as HTMLButtonElement;
const addPlantSearchInput = addPlantModal.querySelector("#search-new-plant-input") as HTMLInputElement;
const addPlantError = addPlantModal.querySelector("#addPlantError") as HTMLParagraphElement;

let cardToDelete: HTMLElement | null = null;
let plantToDelete: number | null = null;
let plantToChangeNick: number | null = null;
let cardToChangeNick: HTMLElement | null = null;
const token: string | null = getCookie("accessToken");
const userId: string | null = getCookie("userId")

if (token === null) {
    window.location.href = "/src/html/login-page.html"
}

document.addEventListener('DOMContentLoaded', () => {
    loadGarden();
});

setupAddPlantCard()

yesDelButton.addEventListener('click', () => {
    if (cardToDelete && plantToDelete) {
        cardToDelete.remove();
        cardToDelete = null;
        if (plantToDelete != null && userId != null && token != null) {
            deleteUserPlantFromLibrary(plantToDelete, userId, token)
        } else {
            console.log("Error the plant was not deleted")
        }
    }
    confirmModal.style.display = 'none';
});

noDelButton.addEventListener('click', () => {
    cardToDelete = null;
    plantToDelete = null;
    confirmModal.style.display = 'none';
});

nickChangeButton.addEventListener('click', () => {
    const newNick: string = nickInput.value;

    if (newNick === null || newNick == "" || newNick.length < 3) {
        nickError.style.display = "flex";
    } else if (plantToChangeNick) {
        changePlantNickname(newNick);
        changeNickModal.style.display = 'none';
    }
});

nickCancelButton.addEventListener('click', () => {
    plantToChangeNick = null;
    cardToChangeNick = null;
    nickInput.value = "";
    changeNickModal.style.display = 'none';
    nickError.style.display = "none";

});

function setupAddPlantCard(){
    addPlantCard.addEventListener('click', () => {
        addPlantModal.style.display = 'flex';
    });

    addPlantSearchBtn.addEventListener('click', () => {
        handleSearch();
    });

    addPlantSearchInput.addEventListener('keydown', (event) => {
    if (event.key === 'Enter') {
        event.preventDefault();
        handleSearch();
    }
    });

    addPlantCancelBtn.addEventListener('click', () => {
        addPlantModal.style.display = 'none';
        addPlantError.style.display = 'none'
        addPlantSearchInput.value = "";
    })
}

function handleSearch(){
    const searchInput = addPlantSearchInput.value;

    if(searchInput == null || searchInput == ""){
        addPlantError.style.display = 'flex';
    }else{
        addPlantError.style.display = 'none'
        addPlantModal.style.display = 'none';
        addPlantSearchInput.value = "";
        window.location.href = `../html/search-page.html?query=${encodeURIComponent(searchInput)}`
    }
}

async function loadGarden() {
    let plants: UserPlant[] | null = null;
    let statusCode: number | null = null;

    if (token != null && userId != null) {
        const { data, status } = await getUserLibrary(userId, token)
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
    } else if (statusCode === 419) {
        console.error("Your token expired")
        if (token != null) {
            clearCookie(token);
        }
        window.location.href = "/src/html/login-page.html";
    }

    if (plants == null) {
        return
    }
    plants.forEach(plant => {
        createPlantCard(plant);
    });

}

function createPlantCard(plant: UserPlant) {
    if(plant.user_plant_id === null){
        return
    }
    const newCard = document.createElement('div');
    newCard.classList.add('plant-card');

    const imageUrl = plant.image_url || "/plant-1573.png";

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

    if (plant.last_watered !== null && plant.last_watered > 0) {
        lastWatered = calculateLastWatered(plant.last_watered);
    } else {
        lastWatered = "0 h"
    }
    newCard.innerHTML = `
        <span class="more-options">
            <img id="more_vert_img" src="/more_vert.svg"/>
            <div class="more-options-menu">
                <div id="change-nickname-btn" class="more-options-button">Change nickname</div>
                <div id="delete-plant-btn" class="more-options-button">Delete plant</div>
    
            </div>
        </span>
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
    attachWaterButtonListener(newCard, plant.user_plant_id);
    attachOptionsMenu(newCard, plant.user_plant_id)
}

function calculateLastWatered(waterInMilli: number): string {
    const progress = Date.now() - waterInMilli
    const hours: number = (((progress / 1000) / 60) / 60);

    if (hours < 0.5) {
        return "0h"
    }

    const roundedHours: number = Math.ceil(hours);

    if (roundedHours < 24) {
        return (roundedHours + "h")
    } else if (roundedHours == 24) {
        return "1d"
    } else {
        const totalDays: number = Math.floor(roundedHours / 24)
        const totalHours: number = roundedHours - (totalDays * 24)

        if (totalHours == 0) {
            return (totalDays + "d")
        }

        return (totalDays + "d " + totalHours + "h")
    }
}

function attachOptionsMenu(newCard: HTMLElement, user_plant_id: number) {
    const popupContainer = newCard.querySelector(".more-options") as HTMLElement;
    const popupMenu = newCard.querySelector(".more-options-menu") as HTMLElement


    if (popupMenu && popupContainer) {
        popupContainer.addEventListener("click", () => {
            popupMenu.classList.toggle("active");
        })

        document.addEventListener("click", (event) => {
            if (!popupMenu.contains(event.target as Node) && !popupContainer.contains(event.target as Node)) {
                popupMenu.classList.remove("active");
            }
        });

        attachDeleteListener(newCard, user_plant_id, popupMenu)
        attachChangeNicknameListener(newCard, user_plant_id, popupMenu)
    }


}

function attachDeleteListener(card: HTMLElement, plantID: number, popupMenu: HTMLElement): void {
    const deleteIcon = card.querySelector('#delete-plant-btn') as HTMLElement;

    if (deleteIcon) {
        deleteIcon.addEventListener('click', (e: Event) => {
            e.stopPropagation();
            popupMenu.classList.remove("active");
            cardToDelete = card;
            plantToDelete = plantID
            confirmModal.style.display = 'flex';
        });
    }
}

function attachChangeNicknameListener(card: HTMLElement, userPlantId: number, popupMenu: HTMLElement) {
    const changeNickBtn = card.querySelector("#change-nickname-btn") as HTMLElement;

    if (changeNickBtn) {
        changeNickBtn.addEventListener('click', (e: Event) => {
            e.stopPropagation();
            popupMenu.classList.remove("active");
            plantToChangeNick = userPlantId;
            cardToChangeNick = card;
            changeNickModal.style.display = "flex";
        });
    }
}

function attachWaterButtonListener(card: HTMLElement, userPlantId: number) {
    const waterButton = card.querySelector('.water-button') as HTMLElement;
    const waterTime = card.querySelector(".water-time") as HTMLElement;

    if (waterButton) {
        waterButton.addEventListener('click', () => {
            if (userId !== null && token !== null) {
                updateUserPlantLastWatered(userId, token, Date.now(), userPlantId)
                waterTime.innerHTML = "0h"
            }
        })
    }
}

async function changePlantNickname(newNick: string) {
    if (userId !== null && token !== null && plantToChangeNick !== null && cardToChangeNick !== null) {
        const status: number = await updateUserPlantNickname(userId, token, newNick, plantToChangeNick);
        const nicknameElement = cardToChangeNick.querySelector(".plant-name");


        if (status === 200) {
            showSuccessToast("Plant nickname changed.")

            if (nicknameElement !== null) {
                nicknameElement.innerHTML = newNick;
            }

        } else {
            showErrorToast("There was an error changing the nickname.")
        }
    }

    plantToChangeNick = null;
    cardToChangeNick = null;
    nickError.style.display = 'none';
    nickInput.value = "";

}


function showSuccessToast(message: string) {
    const toast = document.getElementById('successToast') as HTMLElement;
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

function showErrorToast(message: string) {
    const toast = document.getElementById('errorToast') as HTMLElement;
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


