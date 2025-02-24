// Select the main elements
var plantsContainer = document.querySelector('.plants-container');
var addPlantCard = document.querySelector('.add-plant-card');
var confirmModal = document.getElementById('confirmModal');
var yesButton = confirmModal.querySelector('.yes-button');
var noButton = confirmModal.querySelector('.no-button');

// Variable to keep track of the card to be deleted
var cardToDelete = null;

/**
 * Attaches a delete event listener to the delete icon within a card.
 * @param {Element} card - The card element to which the delete event will be added.
 */
function attachDeleteListener(card) {
    var deleteIcon = card.querySelector('.delete-icon');
    if (deleteIcon) {
        deleteIcon.addEventListener('click', function (e) {
            e.stopPropagation(); // Prevent the event from bubbling up to parent elements
            cardToDelete = card; // Store the card that should be deleted
            confirmModal.style.display = 'flex'; // Show the confirmation modal
        });
    }
}

// Handle click event on the "Add another plant" card
addPlantCard.addEventListener('click', function () {
    // Create a new card element
    var newCard = document.createElement('div');
    newCard.classList.add('plant-card');

    // Set the inner HTML of the new card with the delete icon and plant details
    newCard.innerHTML = "\n    <span class=\"delete-icon\">\uD83D\uDDD1</span>\n    <img src=\"\../../public/plant-1573.png\" alt=\"New Plant\" class=\"plant-image\" />\n    <h3 class=\"plant-name\">New Plant</h3>\n    <p class=\"plant-subtitle\">Species</p>\n    <div class=\"plant-water-info\">\n      <span class=\"water-time\">0 h</span>\n      <button class=\"water-button\">Water</button>\n    </div>\n  ";

    // Insert the new card before the "Add another plant" card
    plantsContainer.insertBefore(newCard, addPlantCard);
    // Attach the delete listener to the new card
    attachDeleteListener(newCard);
});

// Handle the "Yes" button click in the confirmation modal
yesButton.addEventListener('click', function () {
    if (cardToDelete) {
        cardToDelete.remove(); // Remove the card from the DOM
        cardToDelete = null;   // Reset the tracking variable
    }
    confirmModal.style.display = 'none'; // Hide the confirmation modal
});

// Handle the "No" button click in the confirmation modal
noButton.addEventListener('click', function () {
    cardToDelete = null; // Reset the tracking variable
    confirmModal.style.display = 'none'; // Hide the confirmation modal
});

// For any existing plant cards (excluding the "Add another plant" card),
// attach the delete listener
document.querySelectorAll('.plant-card:not(.add-plant-card)').forEach(function (card) {
    attachDeleteListener(card);
});
