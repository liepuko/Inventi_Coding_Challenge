import { useState } from 'react'

function ExportModal({ onClose, onExport }) {
  const [dateFrom, setDateFrom] = useState('')
  const [dateTo, setDateTo] = useState('')

  const handleSubmit = () => {
    onExport(dateFrom, dateTo)
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="card modal-card" onClick={e => e.stopPropagation()}>
        <button className="modal-close" onClick={onClose}>
          x
        </button>

        <h2>Export Statement</h2>

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

        <button className="bttn" onClick={handleSubmit}>
          Export
        </button>
      </div>
    </div>
  )
}

export default ExportModal