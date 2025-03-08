
const requestContainer = document.querySelector('.request-container');
function displayRequests(jsonResponse, page, portal){

    if (portal === "tutor"){
        if (page === "pending"){
            const pendingRequestsList = jsonResponse.data

            pendingRequestsList.forEach(request =>{
                const requestCard = document.createElement('div');
                requestCard.classList.add('request-card');
                requestCard.innerHTML = `
                    <div class="d-flex justify-content-between align-items-start">
                        <div>
                            <h6>${request.name}</h6>
                            <p class="request-info">
                            Course: ${request.course} <br />
                            Message: ${request.message} <br />
                            Requested Date/Time: ${request.request_date}
                            </p>
                        </div>
                        <!-- Session Type Badge (one-on-one, group, recorded) -->
                        <span class="badge bg-primary badge-session-type"
                            >${request.session_type}</span
                        >
                    </div>
                    <!-- Accept / Decline Buttons -->
                    <div class="request-actions mt-2 text-end">
                        <button class="btn btn-sm btn-primary me-1 accept-btn" type="button" data-request-id="${request.request_id}">
                            Accept
                        </button>
                        <button class="btn btn-sm btn-outline-danger decline-btn" type="button" data-request-id="${request.request_id}">
                            Decline
                        </button>
                    </div>
                `;
                requestContainer.append(requestCard);
            });
    
        } else if(page === "accepted"){
            const acceptedRequestList = jsonResponse.data

            acceptedRequestList.forEach(request =>{
                const sessionCard = document.createElement('div');
                sessionCard.classList.add('session-card');

                sessionCard.innerHTML = `
                    <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <h6>${request.session_type} with ${request.name}</h6>
                        <p class="session-info">
                        Course: ${request.course} <br />
                        ${request.request_date}
                        </p>
                    </div>
                    <span class="badge bg-success badge-status">Confirmed</span>
                    </div>
                    <!-- Action Buttons -->
                    <div class="session-actions mt-2 text-end">
                    <button class="btn d-none btn-sm btn-primary" type="button">
                        View Details
                    </button>
                    <button class="btn btn-sm btn-success message-btn" type="button" data-request-id="${request.request_id} data-student-id="${request.student}>
                        Message Student
                    </button>
                    </div>
                `;
                requestContainer.append(sessionCard);
            });
        }
    } else {

    }
    
}

requestContainer.addEventListener('click', (event) => {
    if (event.target.classList.contains('accept-btn')) {
        const requestId = event.target.getAttribute('data-request-id');
        console.log('Accept button clicked for request ID:', requestId);
        handleAcceptRequest(requestId);
    } else if (event.target.classList.contains('decline-btn')) {
        const requestId = event.target.getAttribute('data-request-id');
        console.log('Decline button clicked for request ID:', requestId);
        handleDeclineRequest(requestId);
    } else if (event.target.classList.contains('message-btn')) {
        const requestId = event.target.getAttribute('data-request-id');
        const studentId = event.target.getAttribute('data-student-id');
        console.log('Messaging button clicked for request ID:', requestId);
        fetchMessages(requestId, studentId);
    }
});

function handleAcceptRequest(requestId) {
    showLoadingSpinner();
    Android.acceptRequest();
}

function handleDeclineRequest(requestId) {
    showLoadingSpinner();
    Android.declineRequest();
}

function fetchMessages(requestId, studentId) {
    showLoadingSpinner();
    displayMessagesFromServer(messages, requestId);
    // Android.fetchMessagesFromServer(requestId, studentId);
}

function sendMessage() {
    const sendBtn = document.getElementById('send-message-btn');
    const messageInput = document.getElementById('message-input');
    const message = messageInput.value.trim();
    const requestId = sendBtn.getAttribute('data-request-id');
  
    if (message === '') {
      showToast('Message cannot be empty!', 'danger', 4000);
      return;
    }
  
    messageInput.value = '';
    
    const messagesContainer = document.getElementById('messagesContainer');
    const messageElement = document.createElement('div');
    messageElement.classList.add('message', 'me');
    messageElement.textContent = message;
    messagesContainer.appendChild(messageElement);
    
    messagesContainer.scrollTop = messagesContainer.scrollHeight;
    Android.sendMessage(requestId, message);
  }

function displayMessagesFromServer(response, requestId) {
    
      hideLoadingSpinner();
      const messages = response.data;
  
      // Clear previous messages
      const messagesContainer = document.getElementById('messagesContainer');
      messagesContainer.innerHTML = '';
  
      messages.forEach(message => {
        const messageElement = document.createElement('div');
        messageElement.classList.add('message', message.sender === 'me' ? 'me' : 'other');
        messageElement.textContent = message.content;
        messagesContainer.appendChild(messageElement);
      });
  
      // Set the request ID on the send button
      const sendBtn = document.getElementById('send-message-btn');
      sendBtn.setAttribute('data-request-id', requestId);
  
      // Show the modal
      const messageModal = new bootstrap.Modal(document.getElementById('messageModal'));
      messageModal.show();
  
      // Scroll to the bottom of the messages container
      messagesContainer.scrollTop = messagesContainer.scrollHeight;
    
  }

const data = {
    data: [
        {
            name: "sewell clark",
            session_type: "one-on-one",
            course: "MATH101",
            message: "Looking for group session on Python basics",
            request_date: "Sept 17, 2:00 PM",
            request_id: 1,
            tutor: "Ethan Smith",
            status: "pending"
        },
        {
            name: "sewell clark",
            session_type: "one-on-one",
            course: "MATH101",
            message: "Looking for group session on Python basics",
            request_date: "Sept 17, 2:00 PM",
            request_id: 2,
            tutor: "Ethan Smith",
            status: "pending"
        }
    ]
}

const acceptedrequests = {
  data: [
    {
      name: "sewell clark",
      session_type: "one-on-one",
      course: "comp 123",
      message: "I have problem with calculus",
      request_date: "2025-10-15T14:00:00Z",
      request_id: 1,
      student: 2
    }
  ]
}
displayRequests(acceptedrequests, 'accepted', 'tutor')
const messages = {
        data: [
          {
            sender: "not me",
            content: "sure. I will be available."
          },
          {
            sender: "me",
            content: "sure. I will be available."
          },
          {
            sender: "not me",
            content: "Hi, we can have a meeting tommorow"
          },
          {
            sender: "me",
            content: "cool."
          },
          {
            sender: "not me",
            content: "sure. I will be available."
          },
          {
            sender: "me",
            content: "sure. I will be available."
          },
          {
            sender: "not me",
            content: "Hi, we can have a meeting tommorow"
          },
          {
            sender: "me",
            content: "cool."
          },
          {
            sender: "not me",
            content: "sure. I will be available."
          },
          {
            sender: "me",
            content: "sure. I will be available."
          },
          {
            sender: "not me",
            content: "Hi, we can have a meeting tommorow"
          },
          {
            sender: "me",
            content: "cool."
          },
          {
            sender: "not me",
            content: "sure. I will be available."
          },
          {
            sender: "me",
            content: "sure. I will be available."
          },
          {
            sender: "not me",
            content: "Hi, we can have a meeting tommorow"
          },
          {
            sender: "me",
            content: "cool."
          },
          {
            sender: "not me",
            content: "sure. I will be available."
          },
          {
            sender: "me",
            content: "sure. I will be available."
          },
          {
            sender: "not me",
            content: "Hi, we can have a meeting tommorow"
          },
          {
            sender: "me",
            content: "cool."
          },
        ]
      }

// showLoadingSpinner();
// displayMessagesFromServer(messages, 1);


// setTimeout(() =>{
//   hideLoadingSpinner();
//   showToast("hello world", "danger", 4000);
//   displayRequests(data, 'pending', 'tutor')
// }, 2000);
