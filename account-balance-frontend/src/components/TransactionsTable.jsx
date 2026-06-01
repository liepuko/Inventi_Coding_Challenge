function TransactionsTable({ transactions }) {
  if (!transactions || transactions.length === 0) {
    return null
  }

  return (
    <div className="card">
      <h2>Imported Data</h2>

      <table>
        <thead>
          <tr>
            <th>Account Nr</th>
            <th>Date</th>
            <th>Beneficiary</th>
            <th>Comment</th>
            <th>Amount</th>
            <th>Currency</th>
          </tr>
        </thead>

        <tbody>
          {transactions.map((t, i) => (
            <tr key={i}>
              <td>{t.accountNr}</td>
              <td>{t.date}</td>
              <td>{t.beneficiaryAcc}</td>
              <td>{t.comment}</td>
              <td>{t.amount}</td>
              <td>{t.currency}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

export default TransactionsTable