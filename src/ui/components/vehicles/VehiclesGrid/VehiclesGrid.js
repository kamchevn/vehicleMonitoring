import React from 'react';
import VehicleCard from "../VehicleCard/VehicleCard";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Button from "react-bootstrap/Button";
import { useNavigate } from "react-router";
const VehiclesGrid = ({vehicles, onDelete, insertIntervalForVehicle}) => {
    const navigate = useNavigate();

    const addForm = () => {
       navigate("/vehicles/add");
    };

    const urgentCheck = () => {
        navigate("/vehicles/service/URGENT")
    }

    return (
        <Container className="mt-3">
            <Button variant="success" className="mb-4 me-2" onClick={addForm}>Add vehicle</Button>
            {Array.isArray(vehicles) && vehicles.length > 0  && (
                <Button variant="outline-danger" className="mb-4" onClick={urgentCheck}>Urgent service</Button>
            )}
            <Row xs={1} sm={2} md={2} lg={4} className="g-3">
                {vehicles.map(vehicle => (
                    <Col key={vehicle.id} className="mb-4">
                        <VehicleCard
                            vehicle={vehicle}
                            onDelete={onDelete}
                            insertIntervalForVehicle={insertIntervalForVehicle}
                        />
                    </Col>
                ))}
            </Row>
        </Container>
    );
}

export default VehiclesGrid;