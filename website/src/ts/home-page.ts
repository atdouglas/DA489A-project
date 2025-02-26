interface Plant {
    id: number;
    common_name: string | null;
    scientific_name: string | null;
    family: string | null;
    maintenance: string | null;
    image_url: string | null;
    light: string | null;
    watering_frequency: number | null;
    poisonous_to_pets: boolean | null;
}

const plantsContainer = document.querySelector('.plants-container') as HTMLElement;
const addPlantCard = document.querySelector('.add-plant-card') as HTMLElement;
const confirmModal = document.getElementById('confirmModal') as HTMLElement;
const yesButton = confirmModal.querySelector('.yes-button') as HTMLButtonElement;
const noButton = confirmModal.querySelector('.no-button') as HTMLButtonElement;
let cardToDelete: HTMLElement | null = null;

function attachDeleteListener(card: HTMLElement): void {
    const deleteIcon = card.querySelector('.delete-icon') as HTMLElement;
    if (deleteIcon) {
        deleteIcon.addEventListener('click', (e: Event) => {
            e.stopPropagation();
            cardToDelete = card;
            confirmModal.style.display = 'flex';
        });
    }
}

addPlantCard.addEventListener('click', () => {
    const newCard = document.createElement('div');
    newCard.classList.add('plant-card');
    newCard.innerHTML = `
    <span class="delete-icon">🗑</span>
    <img src="../../public/plant-1573.png" alt="New Plant" class="plant-image" />
    <h3 class="plant-name">New Plant</h3>
    <p class="plant-subtitle">Species</p>
    <div class="plant-water-info">
      <span class="water-time">0 h</span>
      <button class="water-button">Water</button>
    </div>
  `;
    plantsContainer.insertBefore(newCard, addPlantCard);
    attachDeleteListener(newCard);
});

yesButton.addEventListener('click', () => {
    if (cardToDelete) {
        cardToDelete.remove();
        cardToDelete = null;
    }
    confirmModal.style.display = 'none';
});
noButton.addEventListener('click', () => {
    cardToDelete = null;
    confirmModal.style.display = 'none';
});
document.querySelectorAll('.plant-card:not(.add-plant-card)').forEach(card => {
    attachDeleteListener(card as HTMLElement);
});

// Load saved garden items on page load
document.addEventListener('DOMContentLoaded', () => {
    loadGarden();
});

function loadGarden() {
    const stored = localStorage.getItem('myGarden');
    if (!stored) return;
    const garden: Plant[] = JSON.parse(stored);
    garden.forEach(plant => {
        createPlantCard(plant);
    });
}

function createPlantCard(plant: Plant) {
    const newCard = document.createElement('div');
    newCard.classList.add('plant-card');
    const plantName = plant.common_name || plant.scientific_name || "Unknown Plant";
    const imageUrl = plant.image_url || "../../public/plant-1573.png";
    newCard.innerHTML = `
    <span class="delete-icon">🗑</span>
    <img src="${imageUrl}" alt="Plant" class="plant-image" />
    <h3 class="plant-name">${plantName}</h3>
    <p class="plant-subtitle">${plant.family || "No family"}</p>
    <div class="plant-water-info">
      <span class="water-time">0 h</span>
      <button class="water-button">Water</button>
    </div>
  `;
    plantsContainer.insertBefore(newCard, addPlantCard);
    attachDeleteListener(newCard);
}
