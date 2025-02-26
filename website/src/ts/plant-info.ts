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
        if (!response.ok) {
            throw new Error('Plant not found');
        }
        const plant: Plant = await response.json();

        // Update main info
        const commonNameEl = document.querySelector('.common-name') as HTMLElement;
        const scientificNameEl = document.querySelector('.scientific-name') as HTMLElement;
        const familyNameEl = document.querySelector('.family-name') as HTMLElement;
        const descriptionTextEl = document.querySelector('.description-text') as HTMLElement;
        const plantImageEl = document.querySelector('.plant-image img') as HTMLImageElement;

        commonNameEl.textContent = plant.common_name || "Unknown common name";
        scientificNameEl.textContent = plant.scientific_name || "Unknown scientific name";
        familyNameEl.textContent = plant.family || "Unknown family";
        descriptionTextEl.textContent =  "No description available.";
        plantImageEl.src = plant.image_url || "../../public/plant-1573.png";

        const maintenanceEl = document.getElementById('maintenance-value') as HTMLElement;
        const poisonsEl = document.getElementById('poisons-value') as HTMLElement;
        const wateringEl = document.getElementById('watering-value') as HTMLElement;

        maintenanceEl.textContent = plant.maintenance || "No information";
        poisonsEl.textContent = plant.poisonous_to_pets ? "Yes" : "No";

        const msInADay = 86400000;
        const days = plant.watering_frequency ? plant.watering_frequency / msInADay : 0;
        const daysRounded = Math.round(days * 10) / 10;
        wateringEl.textContent = daysRounded > 0 ? `Every ${daysRounded} days` : "No information";

        const addButton = document.querySelector('.add-button') as HTMLButtonElement;
        addButton.addEventListener('click', () => {
            addToGarden(plant);
        });
    } catch (error) {
        console.error('Error loading plant:', error);
    }
});

const addToGarden = (plant: Plant) => {
    const stored = localStorage.getItem('myGarden');
    let garden: Plant[] = stored ? JSON.parse(stored) : [];
    garden.push(plant);
    localStorage.setItem('myGarden', JSON.stringify(garden));
    alert(`"${plant.common_name || plant.scientific_name}" added to your garden!`);
};
