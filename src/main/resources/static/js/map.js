let myMap;
let coords;

ymaps.ready(init);

function init(){
    if (showStreet !== null) {
        let geocoder = ymaps.geocode("Гродно " + showStreet);
        geocoder.then(
            function (res) {
                myMap.setZoom(16);
                myMap.setCenter(res.geoObjects.get(0).geometry.getCoordinates());
                myMap.balloon.open(res.geoObjects.get(0).geometry.getCoordinates(), showStreet);
            }
        );
    }

    myMap = new ymaps.Map("map", {
            center: [53.6753, 23.8290],
            zoom: 11,
            controls: ['zoomControl']
        }, {
            restrictMapArea: true,
            suppressMapOpenBlock: true,
            yandexMapDisablePoiInteractivity: true
        });

    let clusterer = new ymaps.Clusterer();

    labels.forEach(function (value) {
            clusterer.add(new ymaps.Placemark(value.coordinates.split(','), {
                id: value.id,
                coords: value.coordinates,
                name: value.name,
                street: value.street,
                type: value.type,
                image: value.image,
                media: value.media,
                balloonContentHeader: value.name,
                balloonContentBody: value.description,
                hintContent: value.name
            }, {
                iconLayout: 'default#image',
                iconImageHref: value.image,
                iconImageSize: [30, 30],
                iconImageOffset: [-15, -30],
                preset: value.category,
                openBalloonOnClick: false
            }));
        })

    myMap.geoObjects.add(clusterer);

    myMap.events.add('click', function (e) {
        if (isAuthenticated === false) {
            return;
        }
        document.getElementById('labelForm').reset();
        document.getElementById('labelId').value = '';
        coords = e.get('coords');
        document.getElementById('coordsInput').value = coords;
        let geocoder = ymaps.geocode(coords, {
            kind: 'street'
        });
        geocoder.then(
            function (res) {
                document.getElementById('streetInput').value = res.geoObjects.get(0).getThoroughfare();
            }
        );

        myMap.balloon.open(coords, {
            contentBody:'<button type="button" class="btn btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#editModal" aria-controls="editModal">' +
                '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-plus-square" viewBox="0 0 16 16">' +
                '<path d="M14 1a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h12zM2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2z"></path>' +
                '<path d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4z"></path>' +
                '</svg>' +
                ' Добавить метку ' +
                '</button>',
        });

        changeSelect();
    })

    myMap.geoObjects.events.add('click', function (e) {
        var label = e.get("target");

        if (label.properties._data.type === "PHOTO") {
            $('#modalPhotos' + label.properties._data.id).modal("show");
        } else if (label.properties._data.type === "VIDEO") {
            $('#modalVideos' + label.properties._data.id).modal("show");
        } else if (label.properties._data.type === "BIOGRAPHY") {
            $('#modalBiography' + label.properties._data.id).modal("show");
        } else if (label.properties._data.type === "HISTORY") {
            $('#modalHistory' + label.properties._data.id).modal("show");
        } else if (label.properties._data.type === "OBJECT") {
            $('#modalObject' + label.properties._data.id).modal("show");
        }
    })

    myMap.geoObjects.events.add('contextmenu', function (e) {
        if (isAuthenticated === false) {
            return;
        }

        document.getElementById('labelForm').reset();

        let label = e.get('target');
        coords = label.properties._data.coords.split(',')
        document.getElementById('coordsInput').value = coords
        document.getElementById('streetInput').value = label.properties._data.street
        document.getElementById('labelName').value = label.properties._data.name
        document.getElementById('labelType').value = label.properties._data.type
        document.getElementById('labelId').value = label.properties._data.id

        const editMediaButton = document.getElementById("editMediaButton");
        editMediaButton.setAttribute("href", '#editMediaModal' + label.properties._data.id);

        const id = document.getElementById("deleteLabelImageId");
        id.value = label.properties._data.id;

        const deleteLabelImageBlock = document.getElementById("deleteLabelImageBlock");
        deleteLabelImageBlock.appendChild(id)

        tinyMCE.activeEditor.execCommand("mceInsertContent", false, label.properties._data.balloonContentBody);

        myMap.balloon.open(coords, {
            contentBody:'<div>' +
                '<h6>' + label.properties._data.name + '</h6>' +
                '<div class="container">' +
                '  <div class="row align-items-start">' +
                '    <div class="col">' +
                '<button name="editButton" id="editButton" style="margin-top: 5px; width: 180px" type="button" class="btn btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#editModal" aria-controls="editModal">' +
                '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pencil" viewBox="0 0 16 16">' +
                '<path d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207 11.207 2.5zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293l6.5-6.5zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z"></path>' +
                '</svg>' +
                ' Изменить метку ' +
                '</button>' +
                '    </div>' +
                '  </div>' +
                '  <div class="row align-items-center">' +
                '    <div class="col">' +
                '<form action="/delete_label" method="post">' +
                '<button style="margin-top: 5px; width: 180px" type="submit" class="btn btn-outline-danger">' +
                '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-trash" viewBox="0 0 16 16">' +
                '<path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"></path>' +
                '<path fill-rule="evenodd" d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"></path>' +
                '</svg>' +
                ' Удалить метку' +
                '</button>' +
                '<input type="hidden" name="id" value="' + label.properties._data.id + '">' +
                '</form>' +
                '    </div>\n' +
                '  </div>\n' +
                '</div>' +
                '</div>',
        });

        changeSelect();
    })

    function changeSelect() {
        switch (document.getElementById('labelType').value) {
            case 'PHOTO':
                document.getElementById('addContentText').textContent = 'Загрузить фото';
                document.getElementById('mediaContent').setAttribute('accept', "image/*");
                document.getElementById('mediaContentBlock').style.display = '';
                document.getElementById('editMediaButton').textContent = 'Добавленные фото';
                break
            case 'VIDEO':
                document.getElementById('addContentText').textContent = 'Загрузить видео';
                document.getElementById('mediaContent').setAttribute('accept', "video/*");
                document.getElementById('mediaContentBlock').style.display = '';
                document.getElementById('editMediaButton').textContent = 'Добавленные видео';

                break
            default:

                document.getElementById('addContentText').textContent = 'Загрузить документ';
                document.getElementById('mediaContentBlock').style.display = '';
                document.getElementById('mediaContent').setAttribute('accept', "application/pdf");
                document.getElementById('editMediaButton').textContent = 'Добавленный документ';
                break

        }
    }

    document.getElementById('labelType').addEventListener('change', function() {
        changeSelect();
    });
}
