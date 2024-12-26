import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

import Login from './components/Login';
import Home from './components/Home';
import Register from './components/Register';
import UpdatePerson from './components/UpdatePerson';
import MapaIndividual from './components/MapaIndividual';
import MapComponent from './components/MapComponent';
import MapaMultiple from './components/MapaMultiple';
import ChangePassword from './components/ChangePassword';

const App = () => {
  return (
   
      <Router>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/home" element={<Home />} />
          <Route path="/register" element={<Register />} />
          <Route path="/mapComponent" element={<MapComponent />} />
          <Route path="/mapaMultiple" element={<MapaMultiple />} />
          <Route path="/coordenadas/:idpersona" element={<MapaIndividual />} />
          <Route path="/update/:id" element={<UpdatePerson />} />
          <Route path="/change-password/:id" element={<ChangePassword />} />
        </Routes>
      </Router>

  );
};

export default App;
