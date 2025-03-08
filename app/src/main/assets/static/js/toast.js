const mainContainer = document.querySelector(".main-container");

const toastContainer = document.createElement('div');
toastContainer.classList.add('toast-container', 'position-fixed', 'p-3');
toastContainer.id = 'toastContainer';
toastContainer.style.bottom = '60px';
toastContainer.style.left = '10px';
mainContainer.append(toastContainer);

const loadingSpinnerElement = document.createElement('div');
loadingSpinnerElement.classList.add('loading-spinner-container', 'd-none');
loadingSpinnerElement.id = 'loadingSpinner'
loadingSpinnerElement.innerHTML =  `
      <div class="loading-spinner-content">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
            <p class="mt-3 text-white">Please wait...</p>
        </div>
`;
mainContainer.append(loadingSpinnerElement);

function showToast(message, type = "primary", duration = 3000) {
    const container = document.getElementById("toastContainer");
    const toastEl = document.createElement("div");
    toastEl.classList.add("toast", "align-items-center", "text-bg-" + type, "border-0");
    toastEl.setAttribute("role", "alert");
    toastEl.setAttribute("aria-live", "assertive");
    toastEl.setAttribute("aria-atomic", "true");
    toastEl.innerHTML = `
      <div class="d-flex">
        <div class="toast-body">${message}</div>
      </div>
    `;
    container.appendChild(toastEl);
  
    // Initialize bootstrap toast
    const toast = new bootstrap.Toast(toastEl, { delay: duration });
    toast.show();
  
    toastEl.addEventListener("hidden.bs.toast", () => {
      if (container.contains(toastEl)) {
        container.removeChild(toastEl);
      }
    });
  }

  const loadingSpinner = document.getElementById('loadingSpinner');

      function showLoadingSpinner(message = 'Please wait...') {
          const spinnerText = loadingSpinner.querySelector('p');
          spinnerText.textContent = message; 
          loadingSpinner.classList.remove('d-none', 'fade-out');
          loadingSpinner.classList.add('fade-in');

          setTimeout(() => {
              hideLoadingSpinner();
          }, 1000 * 60 * 30);
      }
      
      function hideLoadingSpinner() {
          loadingSpinner.classList.remove('fade-in');
          loadingSpinner.classList.add('fade-out');
          setTimeout(() => {
              loadingSpinner.classList.add('d-none');
          }, 300);
      }