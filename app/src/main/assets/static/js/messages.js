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



conversationContainer.addEventListener('click', (event) => {
    if (event.target.classList.contains('conversation-card')) {
        const requestId = event.target.getAttribute('data-chat-id');
        const studentId = 1
        // console.log('Messaging button clicked for request ID:', requestId);
        fetchMessages(requestId, studentId);
        startMessagePolling(requestId, studentId);
    }
});


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
  // displayConversations(conversations);
  Android.fetchConversations();
