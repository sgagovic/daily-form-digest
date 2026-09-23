import { useState } from 'react';
import './App.css';

function App() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [company, setCompany] = useState('');
  const [message, setMessage] = useState('');
  const [nameError, setNameError] = useState('');
  const [emailError, setEmailError] = useState('');
  const [messageError, setMessageError] = useState('');

  return (
      <main className="form-page">
        <h1>Daily Entries</h1>

        <form className="contact-form" noValidate
              onSubmit={(event) => {
                event.preventDefault();

                const nextNameError =
                    name.trim() === '' ? 'Please enter your name.' : '';

                // noValidate disables browser popups, but input validity still
                // lets us check email format and display our own inline error.
                const emailInput = event.currentTarget.elements.namedItem('email');
                const nextEmailError = email.trim() === ''
                    ? 'Please enter your email.'
                    : emailInput instanceof HTMLInputElement && emailInput.validity.typeMismatch
                        ? 'Please enter a valid email address.'
                        : '';

                const nextMessageError =
                    message.trim() === '' ? 'Please enter a message.' : '';

                setNameError(nextNameError);
                setEmailError(nextEmailError);
                setMessageError(nextMessageError);

                if (nextNameError || nextEmailError || nextMessageError) {
                  return;
                }

                console.log('All validation passed');
              }}>
          <div className="form-field">
            <label htmlFor="name">Name</label>
            <input
                id="name"
                name="name"
                type="text"
                value={name}
                onChange={(event) => setName(event.target.value)}
                aria-invalid={Boolean(nameError)}
                aria-describedby={nameError ? 'name-error' : undefined}
                required
            />
            {nameError && (
                <p id="name-error" className="field-error" role="alert">
                  {nameError}
                </p>
            )}
          </div>

          <div className="form-field">
            <label htmlFor="email">Email</label>
            <input
                id="email"
                name="email"
                type="email"
                value={email}
                onChange={(event) => setEmail(event.target.value)}
                aria-invalid={Boolean(emailError)}
                aria-describedby={emailError ? 'email-error' : undefined}
                required
            />
            {emailError && (
                <p id="email-error" className="field-error" role="alert">
                  {emailError}
                </p>
            )}
          </div>

          <div className="form-field">
            <label htmlFor="company">Company (optional)</label>
            <input
                id="company"
                name="company"
                type="text"
                value={company}
                onChange={(event) => setCompany(event.target.value)}
            />
          </div>

          <div className="form-field">
            <label htmlFor="message">Message</label>
            <textarea
                id="message"
                name="message"
                rows={5}
                value={message}
                onChange={(event) => setMessage(event.target.value)}
                aria-invalid={Boolean(messageError)}
                aria-describedby={messageError ? 'message-error' : undefined}
                required
            />
            {messageError && (
                <p id="message-error" className="field-error" role="alert">
                  {messageError}
                </p>
            )}
          </div>
          <button type="submit">Submit</button>
        </form>
      </main>
  );
}

export default App;
