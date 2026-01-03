import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import LandingPage from './LandingPage';
import CoursePage from './CoursePage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/course" element={<CoursePage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
