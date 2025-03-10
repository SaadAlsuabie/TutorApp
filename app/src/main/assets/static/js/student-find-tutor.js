window.onload = function () {
    showLoadingSpinner();
    AndroidInterface.getTutors('', '', '', '', 'onload');
};


function searchTutors() {
    const tutorname = document.querySelector('.search-input').value;
    const faculty = document.querySelector('.filter-dropdown:nth-of-type(1)').value;
    const major = document.querySelector('.filter-dropdown:nth-of-type(2)').value;
    const course = document.querySelector('.filter-dropdown:nth-of-type(3)').value;

    showLoadingSpinner();

    AndroidInterface.getTutors(tutorname, faculty, major, course, 'search');
}

  function displayTutors(tutorList, sessions) {
      hideLoadingSpinner();
      const container = document.querySelector('.container'); // Get the container where tutor cards will be added

      const existingCards = container.querySelectorAll('.tutor-card');
      existingCards.forEach(card => card.remove());
      const tutors = tutorList;
      tutors.forEach(tutor => {
          const tutorCard = document.createElement('div');
          tutorCard.className = 'tutor-card';

          tutorCard.innerHTML = `
              <div class="d-flex justify-content-between">
                  <h6>${tutor.name || 'N/A'}</h6>
              </div>
              <p class="tutor-info">
                  Faculty: ${tutor.faculty || 'N/A'}<br />
                  Major: ${tutor.major || 'N/A'}<br />
                  Course: ${tutor.course || 'N/A'}
              </p>
              <button class="btn-primary-sm-custom btn-request-session" data-user="${tutor.user}">Request Session</button>
          `;

          // Append the card to the container
          container.appendChild(tutorCard);
      });

      // Show a message if no tutors are found
      if (tutors.length === 0) {
          const noTutorsMessage = document.createElement('p');
          noTutorsMessage.textContent = 'No tutors found.';
          noTutorsMessage.style.textAlign = 'center';
          noTutorsMessage.style.color = '#555';
          container.appendChild(noTutorsMessage);
      }

      // Add event listeners to the "Request Session" buttons
      document.querySelectorAll('.btn-request-session').forEach(button => {
        button.addEventListener('click', () => {
          const userId = button.getAttribute('data-user');
          showRequestModal(userId, sessions);
        });
      });

  }

  //${sessions.map(session => `<option value="${session}">${session}</option>`).join('')}
function showRequestModal(userId, sessions) {
    const tutorID = userId
    let modals = document.querySelectorAll('.requestModal');
    if (modals){
        modals.forEach(modal => {
            modal.remove();
        });
    }
// Create the modal content
const modalContent = `
  <div class="modal fade requestModal" id="requestModal" tabindex="-1" aria-labelledby="requestModalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="requestModalLabel">Request Session</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <form id="requestForm">
            <div class="mb-3">
              <label for="sessionType" class="form-label">Session Type</label>
              <select class="form-select" id="sessionType" required>
               <option value="one-on-one">One-on-One</option>
               <option value="group">Group</option>
               <option value="recorded">Recorded</option>
              </select>
            </div>
            <div class="mb-3">
              <label for="request_time" class="form-label">Requested Time</label>
              <input
                  type="datetime-local"
                  class="form-control"
                  id="request_time"
                  placeholder="Requested time"
                  
                />
            </div>
            <div class="mb-3">
              <label for="message" class="form-label">Message to the Tutor</label>
              <input
                  type="text"
                  class="form-control"
                  id="message"
                  placeholder="Message"
                  
                />
            </div>
            <button type="submit" id="submit-request-btn" class="btn btn-primary"  data-tutor-id="${tutorID}">Submit</button>
          </form>
        </div>
      </div>
    </div>
  </div>
`;

document.body.insertAdjacentHTML('beforeend', modalContent);

const modal = new bootstrap.Modal(document.getElementById('requestModal'));
modal.show();

document.getElementById('requestForm').addEventListener('submit', (e) => {
  e.preventDefault();
  const sessionType = document.getElementById('sessionType').value;
  const RequestTime = document.getElementById('request_time').value;
  const Message = document.getElementById('message').value;
  const submitBtn = document.getElementById('submit-request-btn');
  const tutorID = submitBtn.getAttribute('data-tutor-id');

    console.log("TutorID: "+ tutorID);
    console.log("SessionType: "+sessionType);
    console.log("RequesteTime: "+RequestTime);
    console.log("Message: "+Message);
    if (sessionType === '' || RequestTime === '' || Message === '' || sessionType === ''){
        showToast("All fields required", 'danger', 4000);
        return;
    }
  showLoadingSpinner();
  modal.hide();
  AndroidInterface.requestSession(tutorID, sessionType, RequestTime, Message);

  
});
}

const tutors = {
    data: [
        {
            user: 1,
            name: 'Sewell Clark',
            faculty: 'Engineering',
            major: 'Computer Science',
            course: 'CS 203'
        },
        {
            user: 2,
            name: 'Sewell Clark',
            faculty: 'Engineering',
            major: 'Computer Science',
            course: 'CS 203'
        },
        {
            user: 3,
            name: 'Sewell Clark',
            faculty: 'Engineering',
            major: 'Computer Science',
            course: 'CS 203'
        },
        {
            user: 4,
            name: 'Sewell Clark',
            faculty: 'Engineering',
            major: 'Computer Science',
            course: 'CS 203'
        }
    ],
    sessions: [

    ]
}

function displayTutorsFromSever(response){
  displayTutors(response.data, response.sessions);
}
