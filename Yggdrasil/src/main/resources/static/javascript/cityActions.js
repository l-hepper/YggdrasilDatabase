function toggleFields(value) {
    document.getElementById('searchByIdFields').classList.toggle('d-none', value !== 'id');
    document.getElementById('searchByNameFields').classList.toggle('d-none', value !== 'name');
    document.getElementById('searchByCountryCodeFields').classList.toggle('d-none', value !== 'countryCode');
    document.getElementById('largestDistrictsFields').classList.toggle('d-none', value !== 'largestDistricts');
    document.getElementById('smallestDistrictsFields').classList.toggle('d-none', value !== 'smallestDistricts');
    document.getElementById('byDistrictFields').classList.toggle('d-none', value !== 'byDistrict');
    document.getElementById('populationBelowFields').classList.toggle('d-none', value !== 'populationBelow');
    document.getElementById('populationAboveFields').classList.toggle('d-none', value !== 'populationAbove');
}


$(document).ready(function() {
    let $successModal = $('#successModal');

    $('#deleteCityActionModal').on('show.bs.modal', function(event) {
        let button = $(event.relatedTarget);
        let cityId = button.data('city-id');
        let cityName = button.data('city-name');

        let modal = $(this);
        modal.find('#confirmDeleteButton').data('city-id', cityId);
        modal.find('#confirmDeleteButton').data('city-name', cityName);
    });

    $('#confirmDeleteButton').on('click', function() {
        let cityId = $(this).data('city-id');
        let cityName = $(this).data('city-name');
        if (cityId) {
            $.ajax({
                url: '/cities/delete/' + encodeURIComponent(cityId),
                type: 'DELETE',
                success: function(result) {
                    $('#deleteCityActionModal').modal('hide');
                    $successModal.find('#cityName').text(cityName);
                    $successModal.find('#cityId').text(cityId);
                    $successModal.modal('show');
                    $successModal.on('hidden.bs.modal', function() {
                        window.location.href = '/cities';
                    });
                },
                error: function(xhr, status, error) {
                    alert('An error occurred while deleting the city.');
                    console.error('Error:', error);
                }
            });
        }
    });
});


$(document).ready(function() {
    $('#updateCityModal').on('show.bs.modal', function(event) {
        let button = $(event.relatedTarget);


        let cityId = button.data('city-id');
        let cityName = button.data('city-name');
        let countryCode = button.data('country-code');
        let district = button.data('district');
        let population = button.data('population');

        let modal = $(this);
        modal.find('#cityIdUpdate').val(cityId);
        modal.find('#cityNameUpdate').val(cityName);
        modal.find('#countryCodeUpdate').val(countryCode);
        modal.find('#districtUpdate').val(district);
        modal.find('#populationUpdate').val(population);

    });

    $('#updateCityForm').on('submit', function(e) {
        e.preventDefault();

        let cityId = $('#cityIdUpdate').val();
        let cityName = $('#cityNameUpdate').val();
        let countryCode = $('#countryCodeUpdate').val();
        let district = $('#districtUpdate').val();
        let population = $('#populationUpdate').val();

        let cityData = {
            id: cityId,
            name: cityName,
            countryCode: countryCode,
            district: district,
            population: population
        };

        $.ajax({
            url: '/cities/update/' + encodeURIComponent(cityId),
            type: 'PATCH',
            data: JSON.stringify(cityData),
            contentType: 'application/json',
            success: function(response) {
                $('#updateCityModal').modal('hide');

                $('#updatedCityName').text(cityData.name);
                $('#updatedCityId').text(cityData.id);
                $('#updatedCountryCode').text(cityData.countryCode);
                $('#updatedDistrict').text(cityData.district);
                $('#updatedPopulation').text(cityData.population);

                $('#updateSuccessModal').modal('show');
            },
            error: function(xhr, status, error) {
                alert('An error occurred while updating the city.');
                console.error('Error:', error);
            }
        });
    });
    $('#viewCityButton').on('click', function() {
        let cityId = $('#updatedCityId').text();
        window.location.href = "/cities/search?searchMethod=id&cityId=" + encodeURIComponent(cityId) + "&name=&countryCode=&largestDistricts=&smallestDistricts=&district=&populationBelow=&populationAbove=";
    });

    $('#viewAllCitiesButton').on('click', function() {
        window.location.href = "/cities";
    });
});