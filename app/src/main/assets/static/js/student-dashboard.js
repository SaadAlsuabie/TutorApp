const upcoming = document.getElementById('upcoming');
const pending = document.getElementById('pending');
const recorded = document.getElementById('recorded');

const upcomingContainer = document.querySelector('.upcoming-container');


function fetchDashboard(){
    showLoadingSpinner();
    Android.fetchDashboard();
}


function displayDetails(response){
    hideLoadingSpinner();
    const details = response.data
    upcoming.innerHTML = details.upcoming;
    pending.innerHTML = details.pending;
    recorded.innerHTML = details.recorded;

    const requests = details.upcoming_sessions;

    if (requests.length === 0){
        const noDetails = document.createElement('p');
        noDetails.textContent = 'No Upcoming sessions found';
        noDetails.style.textAlign = 'center';
        noDetails.style.color = '#555';
        upcomingContainer.append(noDetails);
        return
    }

    requests.forEach(request =>{
        const sessionCard = document.createElement('div');
        sessionCard.classList.add('session-card');
        sessionCard.innerHTML = `
        <div class="d-flex justify-content-between align-items-start">
            <div>
              <h6>${request.session_type} with ${request.tutor_name}</h6>
              <p class="session-info">
                ${request.course} <br />
                ${request.requested_time}
              </p>
            </div>
            <span class="badge bg-success badge-status">Confirmed</span>
          </div>
        `;
        upcomingContainer.append(sessionCard);

    });

}

const data = {
    data: {
        upcoming: "1",
        pending: "0",
        recorded: "6",
        upcoming_sessions: [
            {
                tutor_name: "Sewell Clark",
                session_type: "one-on-one",
                course: "Physics 305",
                requested_time: "Sept 15, 9:00 AM",
                status: "confirmed"
            }
        ]
    }
}

// fetchDashboard();
displayDetails(data);