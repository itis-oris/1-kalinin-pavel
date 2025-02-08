document.addEventListener("DOMContentLoaded", function() {
    // Обработчик для меню пользователя
    const menuButton = document.getElementById("menuButton");
    const dropdownMenu = document.getElementById("dropdownMenu");

    if (menuButton && dropdownMenu) {
        menuButton.addEventListener("click", function(event) {
            dropdownMenu.style.display = dropdownMenu.style.display === "block" ? "none" : "block";
            event.stopPropagation();
        });
    }

    // Закрытие меню при клике вне его
    document.addEventListener("click", function() {
        if (dropdownMenu) dropdownMenu.style.display = "none";
    });
});

function toggleLike(quoteId, imgElement) {
    fetch(`/QuoteOfTheDay/main?action=like&quoteId=${quoteId}`, {
        method: 'POST',
    })
        .then(response => {
            if (response.redirected) {
                window.location.href = response.url;
            } else if (response.ok) {
                return response.json();
            } else {
                throw new Error('Failed to update like status');
            }
        })
        .then(data => {
            if (data) {
                const likeCountElement = imgElement.nextElementSibling;
                imgElement.src = data.liked ? 'assets/heart-filled.png' : 'assets/heart.png';
                likeCountElement.textContent = data.likes;
            }
        })
        .catch(error => console.error('Error:', error));
}

document.addEventListener("DOMContentLoaded", () => {
    const options = document.querySelectorAll(".option");
    const nextButton = document.getElementById("nextButton");
    const correctOption = document.getElementById("correctOption").value;

    options.forEach(option => {
        option.addEventListener("click", () => {
            const selectedOption = option.dataset.option;

            if (selectedOption === correctOption) {
                option.classList.add("correct");
            } else {
                option.classList.add("wrong");
                document.querySelector(`.option[data-option="${correctOption}"]`).classList.add("correct");
            }

            // Отключаем клики на все варианты
            options.forEach(opt => opt.style.pointerEvents = "none");

            // Показываем кнопку
            nextButton.style.display = "block";
        });
    });
});

document.addEventListener('DOMContentLoaded', () => {
    const backgroundElement = document.getElementById('background');
    const category = document.getElementById('random-quote-category').value.toString();
    const quoteSection = document.getElementById('quote-section')

    // Установим соответствующий класс фона
    if (category === 'Юмор') {
        backgroundElement.classList.add(`category-humor`);
        quoteSection.style.color = "white"
    } else if (category === 'Мудрость') {
        backgroundElement.classList.add(`category-wise`);
        quoteSection.style.color = "black"
    } else if (category === 'Мотивация') {
        backgroundElement.classList.add(`category-motivation`);
        quoteSection.style.color = "#a59e9e"
    }
});
