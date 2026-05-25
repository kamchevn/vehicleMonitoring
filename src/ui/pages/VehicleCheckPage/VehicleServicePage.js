import React, { useEffect, useState } from "react";
import Header from "../../components/layout/Header/Header";
import useVehicles from "../../../hooks/useVehicles";
import Container from "react-bootstrap/Container";
import VehicleServiceForm from "../../components/checks/VehicleServiceForm/VehicleServiceForm";
import Button from "react-bootstrap/Button";
import { useNavigate, useParams } from "react-router";

const VehicleServicePage = () => {
    const { serviceType, id } = useParams();
    const isVehiclePreselected = Boolean(id);
    const { findById } = useVehicles();
    const [vehicle, setVehicle] = useState(null);

    useEffect(() => {
        if (!isVehiclePreselected) return;

        const loadVehicle = async () => {
            try {
                const v = await findById(Number(id));
                setVehicle(v);
            } catch (err) {
                console.error("Failed to load vehicle:", err);
            }
        };

        loadVehicle();
    }, [id, isVehiclePreselected, findById]);

    const navigate = useNavigate();
    const goBack = () => navigate("/vehicles");

    return (
        <>
            <Header />
            <Container>
                <Button variant="secondary" className="mt-2 mb-3" onClick={goBack}>
                    ← Go back
                </Button>
                {vehicle && vehicle.condition === "UNKNOWN" && (
                    <h1 style={{ marginLeft: 0 }}>Check Components</h1>
                )}
                {(!vehicle || (vehicle && vehicle.condition !== "UNKNOWN" && serviceType === "REGULAR")) && (
                    <h1 style={{ marginLeft: 0 }}>Service Vehicle</h1>
                )}
                <VehicleServiceForm
                    serviceType={serviceType}
                    vehicle={vehicle}
                    isVehiclePreselected={isVehiclePreselected}
                />
            </Container>
        </>
    );
};

export default VehicleServicePage;