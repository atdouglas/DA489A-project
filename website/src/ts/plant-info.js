// Grab elements from the DOM
var commonNameEl = document.getElementById('commonName');
var scientificNameEl = document.getElementById('scientificName');
var maintenanceEl = document.getElementById('maintenance');
var poisonInfoEl = document.getElementById('poisonInfo');
var wateringFrequencyEl = document.getElementById('wateringFrequency');
var descriptionTextEl = document.getElementById('descriptionText');
// Example data (in real usage, you might fetch from an API)
var plantData = {
    commonName: 'Poison Ivy',
    scientificName: 'Toxicodendron radicans',
    family: 'Anacardiaceae',
    maintenance: 'Low maintenance',
    poisonInfo: 'Yes, toxic to pets',
    wateringFrequency: 'Once a week',
    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. \n  Vivamus dignissim est non turpis ultrices, nec placerat nunc bibendum. \n  Etiam commodo est in lorem accumsan vehicula. Quisque in erat dictum, \n  fermentum purus in, scelerisque arcu."
};
// Assign data to the DOM
commonNameEl.textContent = plantData.commonName;
scientificNameEl.textContent = "".concat(plantData.scientificName, " - ").concat(plantData.family);
maintenanceEl.textContent = plantData.maintenance;
poisonInfoEl.textContent = plantData.poisonInfo;
wateringFrequencyEl.textContent = plantData.wateringFrequency;
descriptionTextEl.textContent = plantData.description;
// If you have a button "Add to garden", you can add some logic here
var addGardenButton = document.querySelector('.add-garden-button');
addGardenButton.addEventListener('click', function () {
    alert("You have added ".concat(plantData.commonName, " to your garden!"));
});
