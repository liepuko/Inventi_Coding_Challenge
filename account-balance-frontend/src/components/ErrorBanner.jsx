function ErrorBanner({ message, onClose }) {
  if (!message) {
    return null
  }

  return (
    <div className="error-banner" role="alert">
      <span>{message}</span>

      <button className="error-close" onClick={onClose}>
        ×
      </button>
    </div>
  )
}

export default ErrorBanner