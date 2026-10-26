const filterButtons = document.querySelectorAll('.filter-buttons .btn');

filterButtons.forEach(button => {
    button.addEventListener('click', () => {
        filterButtons.forEach(btn => btn.classList.remove('active'));
        button.classList.add('active');

        const categoria = button.textContent.trim().toLowerCase();
        const cards = document.querySelectorAll('.mascotas-section .col-md-4');

        cards.forEach(card => {
            const textoCard = card.textContent.toLowerCase();

            if (categoria === 'todos') {
                card.style.display = 'block';
            } else if (categoria === 'perros' && (textoCard.includes('golden') || textoCard.includes('beagle') || textoCard.includes('perro'))) {
                card.style.display = 'block';
            } else if (categoria === 'gatos' && (textoCard.includes('gata') || textoCard.includes('siamesa') || textoCard.includes('gato'))) {
                card.style.display = 'block';
            } else if (categoria === 'bebés' && textoCard.includes('meses')) {
                card.style.display = 'block';
            } else if (categoria === 'adultos' && (textoCard.includes('año') || textoCard.includes('años'))) {
                card.style.display = 'block';
            } else {
                card.style.display = 'none';
            }
        });
    });
});

const botonesDetalles = document.querySelectorAll('[data-bs-target="#modalDetalles"]');

botonesDetalles.forEach(boton => {
    boton.addEventListener("click", function () {
        document.getElementById("detalleNombre").textContent = this.dataset.nombre;
        document.getElementById("detalleNombreGrande").textContent = this.dataset.nombre;
        document.getElementById("detalleRaza").textContent = this.dataset.raza;
        document.getElementById("detalleEdad").textContent = this.dataset.edad;
        document.getElementById("detalleSexo").textContent = this.dataset.sexo;
        document.getElementById("detalleTamano").textContent = this.dataset.tamano;
        document.getElementById("detallePersonalidad").textContent = this.dataset.personalidad;
        document.getElementById("detalleDescripcion").textContent = this.dataset.descripcion;
        document.getElementById("detalleImagen").src = this.dataset.imagen;
        document.getElementById("detalleImagen").alt = this.dataset.nombre;
    });
});

(() => {
    'use strict'
    const forms = document.querySelectorAll('.needs-validation')
    
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            event.preventDefault();
            event.stopPropagation();

            if (!form.checkValidity()) {
                form.classList.add('was-validated');
            } else {

                if (form.id === 'formAdopcionModal') {
                    const modalAdopcionEl = document.getElementById('modalAdopcion');
                    if (modalAdopcionEl) {
                        const modalAdopcion = bootstrap.Modal.getInstance(modalAdopcionEl);
                        if (modalAdopcion) modalAdopcion.hide();
                    }
                }


                const modalExitoEl = document.getElementById('modalExito');
                if (modalExitoEl) {
                    const modalExito = new bootstrap.Modal(modalExitoEl) || bootstrap.Modal.getInstance(modalExitoEl);
                    modalExito.show();
                }

                form.reset();
                form.classList.remove('was-validated');
            }
        }, false)
    })
})()
document.addEventListener("DOMContentLoaded", function () {
    const modalDetalles = document.getElementById('modalDetalles');
    if (modalDetalles) {
        modalDetalles.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            
            // Extraer info de los atributos data-*
            const nombre = button.getAttribute('data-nombre');
            const raza = button.getAttribute('data-raza');
            const edad = button.getAttribute('data-edad');
            const sexo = button.getAttribute('data-sexo');
            const tamano = button.getAttribute('data-tamano');
            const personalidad = button.getAttribute('data-personalidad');
            const descripcion = button.getAttribute('data-descripcion');
            const imagen = button.getAttribute('data-imagen');

            // Actualizar los elementos del modal
            modalDetalles.querySelector('#detalleNombre').textContent = "Detalles de " + nombre;
            modalDetalles.querySelector('#detalleNombreGrande').textContent = nombre;
            modalDetalles.querySelector('#detalleRaza').textContent = raza;
            modalDetalles.querySelector('#detalleEdad').textContent = edad;
            modalDetalles.querySelector('#detalleSexo').textContent = sexo;
            modalDetalles.querySelector('#detalleTamano').textContent = tamano;
            modalDetalles.querySelector('#detallePersonalidad').textContent = personalidad;
            modalDetalles.querySelector('#detalleDescripcion').textContent = descripcion;
            modalDetalles.querySelector('#detalleImagen').setAttribute('src', imagen);
        });
    }
});
;