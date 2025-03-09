
const transactionsContainer =  document.querySelector('.transactions');

function fetchTransactions(){
    showLoadingSpinner();
    Android.fetchTransactions();
}

function displayTransactions(response){
    const transactions = response.data;

    if (transactions.length === 0){
        const noTutorsMessage = document.createElement('p');
          noTutorsMessage.textContent = 'No Requests found.';
          noTutorsMessage.style.textAlign = 'center';
          noTutorsMessage.style.color = '#555';
          transactionsContainer.appendChild(noTutorsMessage);
        return
    }

    transactions.forEach(transaction =>{
        const paymentCard = document.createElement('div');
        paymentCard.classList.add('payment-card');

        paymentCard.innerHTML = `
            <div>
                <h6>Deposit</h6>
                <p class="payment-info">
                Date: ${transaction.payment_date} <br />
                Transaction ID: #${transaction.transaction_id}
                </p>
            </div>
            <span class="badge bg-success badge-status">Paid</span>
            </div>
            <p class="payment-amount mb-0">$${transaction.amount}</p>
        `;

        transactionsContainer.append(paymentCard);
    });
 

}
const transactions = {
    data : [
        {
            amount: "10.00",
            transaction_id: "TXN234JKIOP",
            payment_date: "2025-03-25 12:00 p.m",
            status:"paid"
        }
    ],
    balance: "0.00"
}

displayTransactions(transactions);