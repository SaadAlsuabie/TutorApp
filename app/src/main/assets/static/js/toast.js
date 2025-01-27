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