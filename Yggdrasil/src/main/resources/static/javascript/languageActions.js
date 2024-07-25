$(document).ready(function () {
    if ("[[${noLanguageFound}]]" === 'true') {
        $('#noLanguageModal').modal('show');
    }

    $('#noLanguageModal').on('hidden.bs.modal', function () {
        window.location.href = '/languages';
    });
});

// Ensure DOM is fully loaded
document.addEventListener('DOMContentLoaded', function () {
    let updateLanguageForm = document.getElementById('updateLanguageForm');
    if (updateLanguageForm) {
        updateLanguageForm.addEventListener('submit', function (event) {
            event.preventDefault();
            patchLanguage();
        });
    } else {
        console.error('Update language form not found.');
    }
});

function openUpdateModal(element) {
    if (element) {
        let countryCode = element.getAttribute('data-country-code');
        let language = element.getAttribute('data-language');
        let isOfficial = element.getAttribute('data-is-official');
        let percentage = element.getAttribute('data-percentage');

// Check if modal elements exist
        let updateLanguageModal = document.querySelector('#updateLanguageModal');
        if (updateLanguageModal) {
            document.querySelector('#updateLanguageModal #countryCode').value = countryCode;
            document.querySelector('#updateLanguageModal #language').value = language;
            document.querySelector('#updateLanguageModal #hiddenCountryCode').value = countryCode;
            document.querySelector('#updateLanguageModal #hiddenLanguage').value = language;
            document.querySelector('#updateLanguageModal #isOfficial').value = isOfficial;
            document.querySelector('#updateLanguageModal #percentage').value = percentage;

            $('#updateLanguageModal').modal('show');
        } else {
            console.error('Update language modal not found.');
        }
    }
}

function patchLanguage() {
    let countryCode = document.querySelector('#updateLanguageModal #hiddenCountryCode').value;
    let language = document.querySelector('#updateLanguageModal #hiddenLanguage').value;
    let isOfficial = document.querySelector('#updateLanguageModal #isOfficial').value;
    let percentage = document.querySelector('#updateLanguageModal #percentage').value;

    percentage = parseFloat(percentage);
    if (isNaN(percentage) || percentage < 0 || percentage > 100) {
        alert('Percentage must be between 0 and 100.');
        return;
    }

    let updateData = {
        isOfficial: isOfficial,
        percentage: percentage
    };

    fetch(`/languages/${encodeURIComponent(countryCode)}/${encodeURIComponent(language)}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updateData)
    })
        .then(response => {
            if (response.ok) {
                alert('Language updated successfully.');
                location.reload();
            } else {
                alert('Failed to update language.');
            }
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
}

function deleteLanguage(element) {
    let countryCode = element.getAttribute('data-country-code');
    let language = element.getAttribute('data-language');

    if (confirm('Are you sure you want to delete this language?')) {
        fetch(`/languages/${encodeURIComponent(countryCode)}/${encodeURIComponent(language)}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json' // Specify content type if needed
            }
        })
            .then(response => {
                if (response.ok) {
                    alert('Language deleted successfully.');
                    location.reload();
                } else {
                    alert('Failed to delete language.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Failed to delete language.');
            });
    }
}