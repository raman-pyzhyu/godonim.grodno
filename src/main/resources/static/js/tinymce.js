tinymce.init({
    height : "300",
    selector: 'textarea',
    menubar: false,
    plugins: 'lists link image media',
    toolbar1: 'undo redo bold italic underline forecolor backcolor | fontfamily fontsize | link | image media',
    toolbar2: 'alignleft aligncenter alignjustify alignright | numlist bullist',
    toolbar_mode: 'floating',
    tinycomments_mode: 'embedded',
    tinycomments_author: 'Author name',
    file_picker_types: 'image media',
    file_picker_callback: function(callback, value, meta){
        let input = document.createElement('input');
        input.setAttribute('type', 'file');
        input.setAttribute('accept', 'image/*,video/*');
        input.onchange = function () {
            let file = this.files[0];
            let reader = new FileReader();
            reader.onload = async function () {
                let formData = new FormData();
                formData.append('file', file);
                let response = await fetch('/upload', {
                    method: 'POST',
                    body: formData
                });
                let result = await response.json();
                callback(result.location, {alt: 'My alt text'});
            };
            reader.readAsDataURL(file);
        };
        input.click();
    }
});

document.addEventListener('focusin', (e) => {
    if (e.target.closest(".tox-tinymce-aux, .moxman-window, .tam-assetmanager-root") !== null) {
        e.stopImmediatePropagation();
    }
});
