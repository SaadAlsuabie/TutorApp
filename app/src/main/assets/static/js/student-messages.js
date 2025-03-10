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
    console.log("RequestID: " + requestId);
    console.log("Message: "+ message);
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

const conversationContainer = document.querySelector('.messages-level1-container');

function displayConversations(response){
    const messages = response.data;

    if (messages.length === 0){
        const noTutorsMessage = document.createElement('p');
          noTutorsMessage.textContent = 'No Conversations found.';
          noTutorsMessage.style.textAlign = 'center';
          noTutorsMessage.style.color = '#555';
          conversationContainer.appendChild(noTutorsMessage);
    }

    messages.forEach(message =>{
        const conversationCard = document.createElement('div');
        conversationCard.classList.add('conversation-card', 'd-flex', 'justify-content-between', 'align-items-start');
        conversationCard.setAttribute('data-chat-id', message.chat_session_id);
        
        let unreadBadge = '';
        if (message.unread_count > 0) {
            unreadBadge = `<span class="badge bg-primary unread-badge">${message.unread_count} new</span>`;
        }
        conversationCard.innerHTML = `
            <div>
            <h6>${message.full_name}</h6>
            <p class="conversation-snippet">
                ${message.message}
            </p>
            </div>
            <div class="text-end">
            <p class="conversation-meta mb-1">${message.time}</p>
            ${unreadBadge} <br>
            <span style="font-size: xx-small;">click card to open</span>
            </div>
        `;
        conversationContainer.append(conversationCard);
    });
}
function startMessagePolling(requestId, studentId) {
  if (messagePollingInterval) {
      clearInterval(messagePollingInterval);
  }

  messagePollingInterval = setInterval(() => {
      Android.fetchMessagesFromServer(requestId, studentId);
  }, 5000);

  const messageModal = document.getElementById('messageModal');
  messageModal.addEventListener('hidden.bs.modal', () => {
      isModalOpen = false;
      if (messagePollingInterval) {
          clearInterval(messagePollingInterval);
          messagePollingInterval = null;
      }
  });
}
conversationContainer.addEventListener('click', (event) => {
    if (event.target.classList.contains('conversation-card')) {
        const requestId = event.target.getAttribute('data-chat-id');
        const studentId = 1
        // console.log('Messaging button clicked for request ID:', requestId);
        fetchMessages(requestId, studentId);
        startMessagePolling(requestId, studentId);
    }
});

function fetchMessages(requestId, studentId) {
    showLoadingSpinner();
    // displayMessagesFromServer(messages, requestId);
    Android.fetchMessagesFromServer(requestId, studentId);
}

const conversations = {
    data: [
      {
        chat_session_id: 1,
        unread_count: 0,
        full_name: "sewell clark",
        message: "cool.",
        time: "2025-03-08 01:24 p.m"
      },
      {
        chat_session_id: 2,
        unread_count: 6,
        full_name: "sewell clark",
        message: "Alright.",
        time: "2025-03-08 01:24 p.m"
      },
    ]
  }
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


  // displayConversations(conversations);
  Android.fetchConversations();