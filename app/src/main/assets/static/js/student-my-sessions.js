

const requestContainer = document.querySelector('.request-container');
const filterBtns = document.querySelectorAll('.filter-btn');
let savedResponse = null;
// let savedResponse = 345;

filterBtns.forEach(button =>{
    
    button.addEventListener('click', (event)=>{
        if (event.target.classList.contains('accepted')) {
            if (!(savedResponse === null)){
                displayRequests(savedResponse, 'accepted', 'student');
            }
        } else
        if (event.target.classList.contains('pending')) {
            if (!(savedResponse === null)){
                displayRequests(savedResponse, 'pending', 'student');
            }
        } else
        if (event.target.classList.contains('completed')) {
            if (!(savedResponse === null)){
                displayRequests(savedResponse, 'completed', 'student');
            }
        } else 
        if (event.target.classList.contains('declined')) {
            if (!(savedResponse === null)){
                displayRequests(savedResponse, 'declined', 'student');
            }
        }

    });
});

function displayRequests(jsonResponse, page, portal){
    if (savedResponse === null){
        savedResponse = jsonResponse;
    }
    let requests = null;
    requestContainer.innerHTML = '';
    let badge = '';
    let action = '';
    let btnText = '';
    let btnTextMessage = '';

    if (portal === 'student'){
        if (page === 'accepted'){
            requests = jsonResponse.accepted;
            badge = `<span class="badge bg-success badge-status">Confirmed</span>`;
            btnText = 'View Details';
            btnTextMessage = 'Message Tutor';
        } else if(page === 'pending'){
            requests = jsonResponse.pending;
            badge = `<span class="badge bg-warning text-dark badge-status">Pending</span>`;
            btnText = 'View Details';
            btnTextMessage = 'Message Tutor';

        } else if (page === 'completed'){
            requests = jsonResponse.completed;
            badge = `<span class="badge bg-secondary badge-status">Completed</span>`;
            btnText = 'View Details';
            btnTextMessage = 'Message Tutor';

        } else if (page === 'declined'){
            requests = jsonResponse.declined;
            badge = `<span class="badge bg-danger badge-status">Declined</span>`;
            btnText = 'View Details';
            btnTextMessage = 'Message Tutor';
        }
    }
    if (requests === null){
        const noTutorsMessage = document.createElement('p');
        noTutorsMessage.textContent = 'No Requests found.';
        noTutorsMessage.style.textAlign = 'center';
        noTutorsMessage.style.color = '#555';
        requestContainer.appendChild(noTutorsMessage);
    } else {
        if (requests.length === 0){
            const noTutorsMessage = document.createElement('p');
              noTutorsMessage.textContent = 'No Requests found.';
              noTutorsMessage.style.textAlign = 'center';
              noTutorsMessage.style.color = '#555';
              requestContainer.appendChild(noTutorsMessage);
            return
            }

        requests.forEach(request =>{
            const sessionCard = document.createElement('div');
            sessionCard.classList.add('session-card');
            if (page === 'completed' || page === 'declined'){
                let myvar = '';
                if (page === 'declined'){
                    myvar = `Reason: ${request.decline_reason}`;
                }
                sessionCard.innerHTML = `
                    <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <h6>${request.session_type} with ${request.tutor}</h6>
                        <p class="session-info">
                        Course: ${request.course} <br />
                        Date & Time: ${request.request_date}
                        </p>
                        ${myvar}
                    </div>
                    ${badge}
                    
                `;
            } else if (page === 'pending'){
                sessionCard.innerHTML = `
                    <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <h6>${request.session_type} with ${request.tutor}</h6>
                        <p class="session-info">
                        Course: ${request.course} <br />
                        Date & Time: ${request.request_date}
                        </p>
                    </div>
                    ${badge}
                    </div>
                    <button class="btn-primary-sm-custom action-btn my-btns view-details"
                    data-student-name = "${request.name}"
                    data-session-type = "${request.session_type}"
                    data-tutor-name = "${request.tutor}"
                    data-message = "${request.message}"
                    data-request-date = "${request.request_date}"
                    >
                    ${btnText}
                    </button>
                    `;
            } else {
                sessionCard.innerHTML = `
                    <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <h6>${request.session_type} with ${request.tutor}</h6>
                        <p class="session-info">
                        Course: ${request.course} <br />
                        Date & Time: ${request.request_date}
                        </p>
                    </div>
                    ${badge}
                    </div>
                    <button class="btn-primary-sm-custom action-btn my-btns view-details"
                    data-student-name = "${request.name}"
                    data-session-type = "${request.session_type}"
                    data-tutor-name = "${request.tutor}"
                    data-message = "${request.message}"
                    data-request-date = "${request.request_date}"
                    >
                    ${btnText}
                    </button>
                    <button class="btn btn-sm btn-secondary action-btn my-btns message-tutor" data-request-id="${request.request_id}">
                    ${btnTextMessage}
                    </button>
                    `;
            }
            requestContainer.append(sessionCard);
        });
    }
}


requestContainer.addEventListener('click', (event) => {
    if (event.target.classList.contains('message-tutor')){
        const requestID = event.target.getAttribute('data-request-id');
        fetchMessages(requestID)
    } else
    if (event.target.classList.contains('view-details')){
        const studentName = event.target.getAttribute('data-student-name');
        const sessionType = event.target.getAttribute('data-session-type');
        const tutorName = event.target.getAttribute('data-tutor-name');
        const message = event.target.getAttribute('data-message');
        const requestDate = event.target.getAttribute('data-request-date');
        showDetails(studentName, sessionType, tutorName, message, requestDate);
    }
});



function showDetails(studentName, sessionType, tutorName, message, requestDate){
    document.getElementById('studentName').textContent = studentName;
    document.getElementById('sessionType').textContent = sessionType;
    document.getElementById('tutorName').textContent = tutorName;
    document.getElementById('message').textContent = message;
    document.getElementById('requestDate').textContent = requestDate;

    const messageModal = new bootstrap.Modal(document.getElementById('ViewDetailsModal'));
    messageModal.show();
}

function fetchMessages(requestId, studentId=null) {
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
    pending: [
        {
            name: "sewell clark",
            session_type: "one-on-one",
            course: "MATH101",
            message: "Looking for group session on Python basics",
            request_date: "Sept 17, 2:00 PM",
            request_id: 1,
            tutor: "Ethan Smith",
            tutor_id:2,
            status: "pending"
        },
    ],
    accepted: [
        {
            name: "sewell clark",
            session_type: "one-on-one",
            course: "MATH101",
            message: "Looking for group session on Python basics",
            request_date: "Sept 17, 2:00 PM",
            request_id: 1,
            tutor: "Ethan Smith",
            tutor_id:2,
            status: "pending"
        },
    ],
    completed: [
        {
            name: "sewell clark",
            session_type: "one-on-one",
            course: "MATH101",
            message: "Looking for group session on Python basics",
            request_date: "Sept 17, 2:00 PM",
            request_id: 1,
            tutor: "Ethan Smith",
            tutor_id:2,
            status: "completed"
        },
    ],
    declined:[
        {
            name: "sewell clark",
            session_type: "one-on-one",
            course: "MATH101",
            message: "Looking for group session on Python basics",
            request_date: "Sept 17, 2:00 PM",
            request_id: 1,
            tutor: "Ethan Smith",
            tutor_id:2,
            status: "completed"
        },
    ]
}

displayRequests(data, 'accepted', 'student')
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
