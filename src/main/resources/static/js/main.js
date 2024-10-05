document.addEventListener('DOMContentLoaded', () => {
    const searchForm = document.getElementById('search-form');
    const searchInput = document.getElementById('search-input');
    const categorySelect = document.getElementById('category-select');
    const resultsContainer = document.getElementById('results');
    const loadingIndicator = document.getElementById('loading');
    const modal = document.getElementById('modal');
    const modalContent = document.getElementById('modal-content');
    const currentYearSpan = document.getElementById('current-year');

    currentYearSpan.textContent = new Date().getFullYear();

    searchForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const query = searchInput.value.trim();
        const category = categorySelect.value;
        if (query) {
            searchMarvelItems(category, query);
        }
    });

    function searchMarvelItems(category, query) {
        loadingIndicator.classList.remove('hidden');
        resultsContainer.innerHTML = '';

        fetch(`/search?category=${encodeURIComponent(category)}&query=${encodeURIComponent(query)}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                loadingIndicator.classList.add('hidden');
                if (data.length === 0) {
                    resultsContainer.innerHTML = '<p class="text-center text-gray-600">No results found. Try a different search term or category.</p>';
                } else {
                    displayResults(data);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                loadingIndicator.classList.add('hidden');
                resultsContainer.innerHTML = `<p class="text-center text-red-600">An error occurred: ${error.message}</p>`;
            });
    }

    function displayResults(items) {
        items.forEach(item => {
            const card = document.createElement('div');
            card.className = 'bg-white rounded-lg shadow-md overflow-hidden transition duration-300 transform hover:scale-105 cursor-pointer';
            card.innerHTML = `
                <img src="${item.thumbnail ? item.thumbnail.path + '.' + item.thumbnail.extension : '/placeholder.png'}" alt="${item.name || item.title}" class="w-full h-48 object-cover">
                <div class="p-4">
                    <h2 class="text-xl font-semibold mb-2 text-gray-800">${item.name || item.title}</h2>
                    <p class="text-gray-600 text-sm line-clamp-3">${item.description || 'No description available.'}</p>
                </div>
            `;
            card.addEventListener('click', () => showItemDetails(item));
            resultsContainer.appendChild(card);
        });
    }

    function showItemDetails(item) {
        modalContent.innerHTML = `
            <h3 class="text-lg leading-6 font-medium text-gray-900">${item.name || item.title}</h3>
            <div class="mt-2 px-7 py-3">
                <img src="${item.thumbnail ? item.thumbnail.path + '.' + item.thumbnail.extension : '/placeholder.png'}" alt="${item.name || item.title}" class="w-full h-64 object-cover rounded-md">
                <p class="text-sm text-gray-500 mt-4">${item.description || 'No description available.'}</p>
            </div>
            <div class="items-center px-4 py-3">
                <button id="close-modal" class="px-4 py-2 bg-red-500 text-white text-base font-medium rounded-md w-full shadow-sm hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-300">
                    Close
                </button>
            </div>
        `;
        modal.classList.remove('hidden');

        document.getElementById('close-modal').addEventListener('click', () => {
            modal.classList.add('hidden');
        });
    }
});