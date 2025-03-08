
function displayDashboard(details_, page){

    if (page === "tutor") {
        const dashboardDetails = details_.data;
    
        const bookings = dashboardDetails.accepted_bookings;
        const requests = dashboardDetails.pending_requests;
        const earnings = dashboardDetails.earnings;
        const upcomingSessionsList = dashboardDetails.upcoming_sessions;
    
        console.log("bookings: "+bookings);
        if (Array.isArray(upcomingSessionsList)){
          console.log("an array");
        } else {
          console.log("unknown type");
        }
         
        const acceptedBookings = document.getElementById('bookings');
        const pendingRequests = document.getElementById('requests');
        const Earnings = document.getElementById('earnings');
    
        acceptedBookings.innerHTML = bookings;
        pendingRequests.innerHTML = requests;
        Earnings.innerHTML = `$ ${earnings}`;
    
        const sessionsContainer = document.querySelector('.sessionscontainer')
        sessionsContainer.innerHTML = ''
        if (upcomingSessionsList.length === 0){
          const noTutorsMessage = document.createElement('p');
          noTutorsMessage.textContent = 'No Upcoming sessions found.';
          noTutorsMessage.style.textAlign = 'center';
          noTutorsMessage.style.color = '#555';
          sessionsContainer.appendChild(noTutorsMessage);
        } else {
          upcomingSessionsList.forEach(session =>{
            const sessionCard = document.createElement('div');
            sessionCard.className = 'session-card'
            sessionCard.innerHTML = `
                  <div class="d-flex justify-content-between align-items-start">
                    <div>
                      <h6>${session.session_type} with ${session.student_name}</h6>
                      <p class="session-info">
                        Course: ${session.course} <br />
                        ${session.requested_time}
                      </p>
                    </div>
                    <span class="badge bg-success badge-status">Confirmed</span>
                  </div>
                  <div class="session-actions mt-2 text-end">
                    <button class="btn btn-sm btn-primary d-none" type="button">
                      View Details
                    </button>
                    <button class="btn btn-sm btn-secondary d-none" type="button">
                      Message Student
                    </button>
                  </div>
            
            `
            sessionsContainer.append(sessionCard)
          });
    
        }
    } else {
        const dashboardDetails = details_.data;
        
        const bookings = dashboardDetails.accepted_bookings;
        const requests = dashboardDetails.pending_requests;
        const earnings = dashboardDetails.earnings;
        const upcomingSessionsList = dashboardDetails.upcoming_sessions;

        console.log("bookings: "+bookings);
        if (Array.isArray(upcomingSessionsList)){
        console.log("an array");
        } else {
        console.log("unknown type");
        }
        
        const acceptedBookings = document.getElementById('bookings');
        const pendingRequests = document.getElementById('requests');
        const Earnings = document.getElementById('earnings');

        acceptedBookings.innerHTML = bookings;
        pendingRequests.innerHTML = requests;
        Earnings.innerHTML = `$ ${earnings}`;

        const sessionsContainer = document.querySelector('.sessionscontainer')
        sessionsContainer.innerHTML = ''
        if (upcomingSessionsList.length === 0){
        const noTutorsMessage = document.createElement('p');
        noTutorsMessage.textContent = 'No Upcoming sessions found.';
        noTutorsMessage.style.textAlign = 'center';
        noTutorsMessage.style.color = '#555';
        sessionsContainer.appendChild(noTutorsMessage);
        } else {
        upcomingSessionsList.forEach(session =>{
            const sessionCard = document.createElement('div');
            sessionCard.className = 'session-card'
            sessionCard.innerHTML = `
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                    <h6>${session.session_type} with ${session.student_name}</h6>
                    <p class="session-info">
                        Course: ${session.course} <br />
                        ${session.requested_time}
                    </p>
                    </div>
                    <span class="badge bg-success badge-status">Confirmed</span>
                </div>
                <div class="session-actions mt-2 text-end">
                    <button class="btn btn-sm btn-primary d-none" type="button">
                    View Details
                    </button>
                    <button class="btn btn-sm btn-secondary d-none" type="button">
                    Message Student
                    </button>
                </div>
            
            `
            sessionsContainer.append(sessionCard)
        });

        }
    }
  }

const data = {
data: {
    accepted_bookings: 0,
    pending_requests: 0,
    earnings: "0.00",
    upcoming_sessions: [
    {
    student_name: "John Doe",
    session_type: "one-on-one",
    course: "MATH 101",
    requested_time: "2025-12-03 14:00",
    status: "pending"
    },
    {
    student_name: "John Doe",
    session_type: "one-on-one",
    course: "MATH 101",
    requested_time: "2025-12-03 14:00",
    status: "pending"
    },

    ]
}
};


showLoadingSpinner();
Android.fetchDashboard();