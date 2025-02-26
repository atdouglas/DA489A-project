document.addEventListener('DOMContentLoaded', () => {
    const searchInput = document.getElementById('search-input') as HTMLInputElement;
    const searchIcon = document.getElementById('search-icon');

    const performSearch = (searchTerm: string) => {
        window.location.href = `../html/search-page.html?query=${encodeURIComponent(searchTerm)}`;
    }

    searchInput.addEventListener('keypress', (event) => {
        if(event.key === 'Enter') {
            const searchTerm = searchInput.value.trim();

            if(searchTerm){
                performSearch(searchTerm);
            }
        }
    });

    searchIcon?.addEventListener('click', () => {
        const searchTerm = searchInput.value.trim();

            if(searchTerm){
                performSearch(searchTerm);
            }
    });

});