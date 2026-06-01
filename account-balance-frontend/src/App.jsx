import { useState, useRef } from 'react'
import './App.css'

import TransactionsTable from './components/TransactionsTable'
import ExportModal from './components/ExportPopUp'
import ErrorBanner from './components/ErrorBanner'

function App() {
  const [transactions, setTransactions] = useState([])
  const [error, setError] = useState(null)
  const [file, setFile] = useState(null)
  const fileInputRef = useRef(null)
  const [accountNr, setAccountNr] = useState('')
  const [dateFrom, setDateFrom] = useState('')
  const [dateTo, setDateTo] = useState('')
  const [balance, setBalance] = useState(null)
  const [showExportModal, setShowExportModal] = useState(false)

  const handleImport = async (selectedFile) => {
    const formData = new FormData()
    formData.append('file', selectedFile)

    try {
      const response = await fetch(`http://localhost:7000/api/import`, {
        method: 'POST',
        body: formData
      })
      if (!response.ok) {
        const msg = await response.text()
        setError(msg)
        return
      }
      const data = await response.json()
      setTransactions(data)
      setError(null)
    } catch (e) {
      setError('Could not connect to server')
    }
  
  }

  const handleCalculate = async () => {
    if (!accountNr.trim()) {
      setError('Account number is required')
      return
    }
    if (dateFrom && dateTo && dateFrom > dateTo) {
      setError('Date from cannot be later than date to')
      return
    }
    
    try {
      const params = new URLSearchParams({ accountNr })
      if (dateFrom) params.append('dateFrom', dateFrom.replace('T', ' ') + ':00')
      if (dateTo)   params.append('dateTo',   dateTo.replace('T', ' ') + ':00')

      const response = await fetch(`http://localhost:7000/api/calculate?${params}`)
      if (!response.ok) {
        const msg = await response.text()
        setError(msg)
        return
      }
      const data = await response.json()
      setBalance(data)
      setError(null)
    } catch (e) {
      setError('Could not connect to server')
    }
  }

  const handleExport = async (exportDateFrom, exportDateTo) => {
      if (exportDateFrom && exportDateTo && exportDateFrom > exportDateTo) {
      setError('Date from cannot be later than date to')
      return
    }
    try {
      const params = new URLSearchParams()
      if (exportDateFrom) params.append('dateFrom', exportDateFrom.replace('T', ' ') + ':00')
      if (exportDateTo)   params.append('dateTo',   exportDateTo.replace('T', ' ') + ':00')

      const response = await fetch(`http://localhost:7000/api/export?${params}`, {
        method: 'POST'
      })
      if (!response.ok) {
        const msg = await response.text()
        setError(msg)
        return
      }

      const blob = await response.blob()
      const url = window.URL.createObjectURL(blob)

      const disposition = response.headers.get('Content-Disposition')
      const filename = disposition
        ? disposition.split('filename=')[1]
        : 'statement-export.csv'

      const link = document.createElement('a')
      link.href = url
      link.download = filename
      document.body.appendChild(link)
      link.click()

      link.remove()
      window.URL.revokeObjectURL(url)

      setShowExportModal(false)
      setError(null)
    } catch (e) {
      setError('Could not connect to server')
    }
  }


  return (
    <div className="container">
       <ErrorBanner
        message={error}
        onClose={() => setError(null)}
      />
      <h1>Bank Account Balance Management Service</h1>
      <p className='label'>Import statement file:</p>
      <div className="actions">
      <input type="file" accept=".csv" ref={fileInputRef}
      style={{ display: 'none' }}
      onChange={e => {
        const selectedFile = e.target.files[0]

        if (!selectedFile) {
          return
        }

        setFile(selectedFile)
        handleImport(selectedFile)
      }} />
      <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
      <button className='bttn' onClick={() => fileInputRef.current.click()}>
        Import
      </button>
       {file && <p>{file.name}</p>}
       </div>
      <button className='bttn' onClick={() => setShowExportModal(true)}>Export statement</button>
      </div>
      <TransactionsTable transactions={transactions} />
      <div className="card">
        <h2>Calculate account balance</h2>
        <div>
          <div>
            <label>Account number</label>
            <input type='text' placeholder='Bank account number' value={accountNr} onChange={e => setAccountNr(e.target.value)}></input>
          </div>
          <div className="date-row">
            <div>
              <label>Date from</label>
              <input
                type="datetime-local"
                value={dateFrom}
                onChange={e => setDateFrom(e.target.value)}
              />
            </div>
            <div>
              <label>Date to</label>
              <input
                type="datetime-local"
                value={dateTo}
                onChange={e => setDateTo(e.target.value)}
              />
            </div>
          </div>
          
        </div>
       
        <button className='bttn' onClick={handleCalculate}>Calculate</button>
        {balance !== null && <p>Balance: {balance}</p>}
      </div>
      {showExportModal && (
        <ExportModal
          onClose={() => setShowExportModal(false)}
          onExport={handleExport}
        />
      )}
    </div>
    
    )
}

export default App
