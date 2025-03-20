import { getCareGuides } from "./api_connection";
import { CareGuide } from "./types";

const nameElement = document.getElementById("plantName") as HTMLElement;
const mainContainer = document.getElementById("care-guides-container") as HTMLDivElement
const plantInfoBtn = document.getElementById("plant-info-btn") as HTMLButtonElement;

document.addEventListener("DOMContentLoaded", () => {
    const plantID = new URLSearchParams(window.location.search).get('plantid')
    const plantName = new URLSearchParams(window.location.search).get('plantName')

    if(plantName){
        nameElement.innerHTML = plantName + " - Care guides";
    }

    if(plantID && plantID !== ""){
        setupCareGuides(Number.parseInt(plantID))
        setupPlantInfoBtn(plantID)
    }

});


async function setupCareGuides(id : number){
    const guides : CareGuide[] | null = await getCareGuides(id);

    if(guides){
        guides.forEach(guide => {
            createGuideComponent(guide.type, guide.description)   
        });
    }
}

function createGuideComponent(type : string, description : string){
    type = type.charAt(0).toUpperCase() + type.slice(1)

    const newGuide = document.createElement('div');
    newGuide.classList.add("guide-container")
    newGuide.innerHTML = `
    <h2>${type}</h2>
    <p>${description}</p>
    `;

    mainContainer.appendChild(newGuide)

}

function setupPlantInfoBtn(plantID : string){
    plantInfoBtn.addEventListener('click', () => {
        window.location.href = `plant-info.html?id=${plantID}`
    });
}