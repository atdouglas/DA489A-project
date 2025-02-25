// Select the main elements from the DOM
const plantsContainer = document.querySelector('.plants-container') as HTMLElement;
const addPlantCard = document.querySelector('.add-plant-card') as HTMLElement;
const confirmModal = document.getElementById('confirmModal') as HTMLElement;
const yesButton = confirmModal.querySelector('.yes-button') as HTMLButtonElement;
const noButton = confirmModal.querySelector('.no-button') as HTMLButtonElement;

// Variable to keep track of which card is to be deleted
let cardToDelete: HTMLElement | null = null;

/**
 * Attaches a delete event listener to the delete icon within a card.
 * @param card - The card element to which the delete functionality is added.
 */
function attachDeleteListener(card: HTMLElement): void {
    const deleteIcon = card.querySelector('.delete-icon') as HTMLElement;
    if (deleteIcon) {
        // When the delete icon is clicked
        deleteIcon.addEventListener('click', (e: Event) => {
            e.stopPropagation(); // Prevent the event from bubbling up to parent elements
            cardToDelete = card; // Store the reference to the card to be deleted
            confirmModal.style.display = 'flex'; // Show the confirmation modal
        });
    }
}

// Add an event listener to the "Add another plant" card
addPlantCard.addEventListener('click', () => {
    // Create a new card element
    const newCard = document.createElement('div');
    newCard.classList.add('plant-card');

    // Set the inner HTML of the new card, including the delete icon and plant details
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

    // Insert the new card into the container before the "Add another plant" card
    plantsContainer.insertBefore(newCard, addPlantCard);

    // Attach the delete listener to the new card
    attachDeleteListener(newCard);
});

// Handle the "Yes" button click in the delete confirmation modal
yesButton.addEventListener('click', () => {
    if (cardToDelete) {
        cardToDelete.remove(); // Remove the card from the DOM
        cardToDelete = null;   // Reset the cardToDelete variable
    }
    confirmModal.style.display = 'none'; // Hide the modal
});

// Handle the "No" button click in the delete confirmation modal
noButton.addEventListener('click', () => {
    cardToDelete = null; // Clear the cardToDelete variable
    confirmModal.style.display = 'none'; // Hide the modal
});

// Attach delete listeners to any existing plant cards (excluding the "Add another plant" card)
document.querySelectorAll('.plant-card:not(.add-plant-card)').forEach(card => {
    attachDeleteListener(card as HTMLElement);
});
