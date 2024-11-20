import logo from './logo.svg';
import './App.css';
import Dashboard from './Dashboard';
import AllSystemsPage from './AllSystemsPage';
import React, { useEffect } from 'react';

function App() {
  useEffect(() => {
    const checkLoginStatus = async () => {
      try {
        const response = await fetch('http://localhost:8080/login', {
          method: 'GET',
          credentials: 'include', // Include credentials if you're dealing with cookies
        });
        
        if (response.status === 200) {
          // Get the username and store it in session storage
          const userNameResponse = await fetch('http://localhost:8080/api/getUserName', {
            method: 'GET',
            credentials: 'include',
          });
          if (userNameResponse.status === 200) {
            const userNameData = await userNameResponse.text();
            sessionStorage.setItem('username', userNameData); // Store the username in session storage
          }
          
          // Continue loading the app
          console.log('Logged in');
        } else if (response.status === 302) {
          // Get the redirect location
          const redirectLocation = response.headers.get('Location');
          if (redirectLocation) {
            // Redirect to the location
            window.location.href = redirectLocation;
          }
        } else {
          // Handle other status codes or errors
          console.error('Unexpected status:', response.status);
        }
      } catch (error) {
        console.error('Error checking login status:', error);
      }
    };

    checkLoginStatus();
  }, []);
  
  return (
    <div className="App">
      {/* <Dashboard></Dashboard> */}
      <AllSystemsPage></AllSystemsPage>
    </div>
  );
}

export default App;
