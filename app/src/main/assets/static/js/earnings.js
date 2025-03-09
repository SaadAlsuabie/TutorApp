
const transactionsContainer = document.querySelector('.transactions-container')

function displayTransactions(response){
    hideLoadingSpinner();
    const transactions = response.data
    const earnings = document.querySelector('.earnings-amount');
    earnings.innerHTML = `$ ${response.balance}`;

    
    if (transactions.length === 0){
        const noTutorsMessage = document.createElement('p');
          noTutorsMessage.textContent = 'No Transactions found.';
          noTutorsMessage.style.textAlign = 'center';
          noTutorsMessage.style.color = '#555';
          transactionsContainer.appendChild(noTutorsMessage);
    }
    transactions.forEach(transaction =>{
        
        const transactionCard = document.createElement('div');
        transactionCard.classList.add('transaction-card');
        transactionCard.innerHTML = `
        <div class="d-flex justify-content-between align-items-start">
          <div>
            <h6>Withdrawal Request</h6>
            <p class="transaction-info">
              Date: ${transaction.payment_date}<br />
              Transaction ID: #${transaction.transaction_id}
            </p>
          </div>
          <span class="badge bg-success badge-status">Paid</span>
        </div>
        <p class="transaction-amount mb-0">$ ${transaction.amount}</p>
        `;
        transactionsContainer.append(transactionCard);
    });
}
const transactions = {
    balance: "100.00",
    data: [
        {
            amount: "5.00",
            transaction_id: "WTD7VGHI8G",
            payment_date: "2025-03-08 15:59",
            status: "pending"
          },
          {
            amount: "5.00",
            transaction_id: "WTD7VGHI8G",
            payment_date: "2025-03-08 15:59",
            status: "pending"
          },
          {
            amount: "5.00",
            transaction_id: "WTD7VGHI8G",
            payment_date: "2025-03-08 15:59",
            status: "pending"
          },
          {
            amount: "5.00",
            transaction_id: "WTD7VGHI8G",
            payment_date: "2025-03-08 15:59",
            status: "pending"
          }

    ]
  }

  
  function getTransactions(){
      Android.fetchTransactions();
    }
    
  showLoadingSpinner();
  getTransactions();
  // displayTransactions(transactions);