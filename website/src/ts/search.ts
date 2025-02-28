document.addEventListener('DOMContentLoaded', () => {
    const URLparams = new URLSearchParams(window.location.search);
    const searchTerm = URLparams.get('query');
    
    if(searchTerm){
        const searchQueryElement = document.getElementById('search-query');
        if(searchQueryElement){
            searchQueryElement.textContent = `"${searchTerm}"`;
        }
        fetchSearchResults(searchTerm);
    }
});    

const fetchSearchResults = async (searchTerm: string) => {
    try {
        //const url = `/api/search/${encodeURIComponent(searchTerm)}`;
        const url = `http://localhost:7888/search/${encodeURIComponent(searchTerm)}`;

        console.log('This is the formatted url:', url);
        const response = await fetch(url);
        if(!response.ok){
            throw new Error('nätverket snear yao');
        }
        const plants: Plant[] = await response.json();
        console.log('Plants:', plants);
        updateSearchResults(plants);
    }catch(error) {
        console.error('Something went wrong with fetch: ', error);
    }
};

const updateSearchResults = (plants: Plant[]) => {
    const searchResultsContainer = document.querySelector('.search-results');

    if(searchResultsContainer){
        searchResultsContainer.innerHTML = '';

        plants.forEach(plant => {
            const searchHit = document.createElement('div');
            searchHit.className = 'search-hit';

            const plantImage = document.createElement('img');
            plantImage.src = plant.image_url || '../../public/plant-1573.png';
            plantImage.alt = 'Plant image';
            plantImage.className = 'plant-image';

            const plantDetails = document.createElement('div');
            plantDetails.className = 'plant-details';

            const plantName = document.createElement('h2');
            plantName.className = 'plant-name';
            plantName.textContent = plant.common_name || 'No known common name';

            const scientificName = document.createElement('p');
            scientificName.className = 'scientific-name';
            scientificName.textContent = plant.scientific_name || 'No known scientific name';

            const plantActions = document.createElement('div');
            plantActions.className = 'plant-actions';

            const infoButton = document.createElement('button');
            infoButton.className = 'info-button';
            infoButton.textContent = 'Plant info';

            const libraryButton = document.createElement('button');
            libraryButton.className = 'library-button';
            libraryButton.textContent = 'Add to library';

            plantDetails.appendChild(plantName);
            plantDetails.appendChild(scientificName);

            plantActions.appendChild(infoButton);
            plantActions.appendChild(libraryButton);

            searchHit.appendChild(plantImage);
            searchHit.appendChild(plantDetails);
            searchHit.appendChild(plantActions);

            searchResultsContainer.appendChild(searchHit);
        });
    }
};


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