interface Plant {
    id: number;
    common_name: string | null;
    scientific_name: string | null;
    family: string | null;
    image_url: string | null;
    maintenance: string | null;
    light: string | null;
    watering_frequency: number | null;
    poisonous_to_pets: boolean | null;
}

document.addEventListener('DOMContentLoaded', async () => {
    const params = new URLSearchParams(window.location.search);
    const plantId = params.get('id');
    if (!plantId) return;

    try {
        const response = await fetch(`http://localhost:7888/plants/${plantId}`);
        if (!response.ok) throw new Error('Plant not found');
        const plant: Plant = await response.json();

        // Update main plant info
        const commonNameEl = document.querySelector('.common-name') as HTMLElement;
        const scientificNameEl = document.querySelector('.scientific-name') as HTMLElement;
        const familyNameEl = document.querySelector('.family-name') as HTMLElement;
        const descriptionTextEl = document.querySelector('.description-text') as HTMLElement;
        const plantImageEl = document.querySelector('.plant-image img') as HTMLImageElement;

        commonNameEl.textContent = plant.common_name || "Unknown common name";
        scientificNameEl.textContent = plant.scientific_name || "Unknown scientific name";
        familyNameEl.textContent = plant.family || "Unknown family";
        descriptionTextEl.textContent = "No description available.";
        plantImageEl.src = plant.image_url || "../../public/plant-1573.png";

        // Update additional plant info
        const maintenanceEl = document.getElementById('maintenance-value') as HTMLElement;
        const poisonsEl = document.getElementById('poisons-value') as HTMLElement;
        const wateringEl = document.getElementById('watering-value') as HTMLElement;
        maintenanceEl.textContent = plant.maintenance || "No information";
        poisonsEl.textContent = plant.poisonous_to_pets ? "Yes" : "No";
        const msInADay = 86400000;
        const days = plant.watering_frequency ? plant.watering_frequency / msInADay : 0;
        const daysRounded = Math.round(days * 10) / 10;
        wateringEl.textContent = daysRounded > 0 ? `Every ${daysRounded} days` : "No information";

        // Set up add-to-garden with confirmation modal
        const addButton = document.querySelector('.add-button') as HTMLButtonElement;
        addButton.addEventListener('click', () => {
            plantToAdd = plant;
            confirmAddModal.style.display = 'flex';
        });
    } catch (error) {
        console.error('Error loading plant:', error);
    }
});

// Global variable for storing the plant to add
let plantToAdd: Plant | null = null;

// Get the Add confirmation modal elements
const confirmAddModal = document.getElementById('confirmAddModal') as HTMLElement;
const yesAddButton = confirmAddModal.querySelector('.yes-add-button') as HTMLButtonElement;
const noAddButton = confirmAddModal.querySelector('.no-add-button') as HTMLButtonElement;

// When user clicks "Yes" in the add confirmation modal
yesAddButton.addEventListener('click', () => {
    if (plantToAdd) {
        addToGarden(plantToAdd);
        showToast(`Perfect, added "${plantToAdd.common_name || plantToAdd.scientific_name}" to the garden!`);
        plantToAdd = null;
    }
    confirmAddModal.style.display = 'none';
});

// When user clicks "No" in the add confirmation modal
noAddButton.addEventListener('click', () => {
    plantToAdd = null;
    confirmAddModal.style.display = 'none';
});

// Function to save the plant to localStorage
function addToGarden(plant: Plant) {
    const stored = localStorage.getItem('myGarden');
    let garden: Plant[] = stored ? JSON.parse(stored) : [];
    garden.push(plant);
    localStorage.setItem('myGarden', JSON.stringify(garden));
}

// Function to show a toast notification that fades in and out
function showToast(message: string) {
    const toast = document.getElementById('toast') as HTMLElement;
    if (toast) {
        toast.textContent = message;
        toast.style.display = 'block';
        // Force reflow for transition (optional)
        void toast.offsetWidth;
        toast.classList.add('show');
        // Hide toast after 3 seconds with fade out
        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => {
                toast.style.display = 'none';
            }, 500); // match the CSS transition duration
        }, 3000);
    }
}
