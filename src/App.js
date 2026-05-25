import './App.css';
import React from 'react';
import {BrowserRouter, Routes, Route} from "react-router";
import Layout from "../src/ui/components/layout/Layout/Layout"
import VehiclesPage from "./ui/pages/VehiclesPage/VehiclesPage";
import AddEditVehiclePage from "./ui/pages/AddEditVehiclePage/AddEditVehiclePage";
import VehicleServicePage from "./ui/pages/VehicleCheckPage/VehicleServicePage";
import ServicesPage from "./ui/pages/ChecksPage/ServicesPage";
import VehicleInfoPage from "./ui/pages/VehicleInfoPage/VehicleInfoPage";
import ServiceInfoPage from "./ui/pages/CheckInfoPage/ServiceInfoPage";
import RegisterPage from "./ui/pages/RegisterPage/RegisterPage";
import LoginPage from "./ui/pages/LoginPage/LoginPage";

const App = () => {
  return (
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Layout/>}></Route>
          <Route path="/register" element={<RegisterPage/>}></Route>
          <Route path="/login" element={<LoginPage/>}></Route>
          <Route path="/vehicles" element={<VehiclesPage/>}></Route>
          <Route path="/vehicles/:id" element={<VehicleInfoPage/>}></Route>
          <Route path="/vehicles/add" element={<AddEditVehiclePage/>}></Route>
          <Route path="/vehicles/edit/:id" element={<AddEditVehiclePage/>}></Route>
          <Route path="/vehicles/service/:serviceType" element={<VehicleServicePage/>}></Route>
          <Route path="/vehicles/service/:serviceType/:id" element={<VehicleServicePage/>}></Route>
          <Route path="/services" element={<ServicesPage/>}></Route>
          <Route path="/services/:id" element={<ServiceInfoPage/>}></Route>
        </Routes>
      </BrowserRouter>
  );
};


export default App;
