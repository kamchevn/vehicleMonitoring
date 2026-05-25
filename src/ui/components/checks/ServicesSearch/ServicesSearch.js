import React, {useEffect} from 'react';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import {useState} from "react";
import {useMediaQuery} from "../../../../hooks/useMediaQuery";
import useVehicles from "../../../../hooks/useVehicles";

const ServicesSearch = ({ onSearch }) => {
    const isMobile = useMediaQuery("(max-width: 575px)");
    const { findAll } = useVehicles();
    const [vehicles, setVehicles] = useState([]);
    const [vehicleId, setVehicleId] = useState("");
    const [serviceType, setServiceType] = useState("");
    const handleSubmit = event => {
        event.preventDefault();
        onSearch(vehicleId, serviceType);
    };

    useEffect(() => {
            findAll()
                .then((res) => setVehicles(res))
    },[findAll])

    return (
        <Container>
            <Form onSubmit={handleSubmit}>
                {isMobile && (
                    <>
                        <Row>
                            <Col>
                                <Form.Group controlId="vehicleId" className="mb-3">
                                    <Form.Label>Vehicle</Form.Label>
                                    <Form.Select
                                        value={vehicleId ?? ""}
                                        onChange={(e) => setVehicleId(e.target.value || null)}
                                    >
                                        <option value="">-- Select Vehicle --</option>
                                        {vehicles.map(v => (
                                            <option key={v.id} value={v.id}>
                                                {v.name}
                                            </option>
                                        ))}}
                                    </Form.Select>
                                </Form.Group>
                            </Col>
                        </Row>
                        <Row>
                            <Col>
                                <Form.Group controlId="serviceType">
                                    <Form.Label>Service Type</Form.Label>
                                    <Form.Select
                                        value={serviceType ?? ""}
                                        onChange={(e) => setServiceType(e.target.value || null)}
                                    >
                                        <option value="">-- Select Service Type --</option>
                                        <option value="REGULAR">Regular</option>
                                        <option value="URGENT">Urgent</option>
                                    </Form.Select>
                                </Form.Group>
                            </Col>
                        </Row>
                        <Row>
                            <Col>
                                <Form.Group controlId="type" className="mb-3">
                                    <br/>
                                    <Button type="submit" className="mt-lg-2 mt-md-2 mt-sm-2 mt-xl-2 mt-2">🔍 Search</Button>
                                </Form.Group>
                            </Col>
                        </Row>
                    </>
                )}
                {!isMobile && (
                    <Row>
                        <Col>
                            <Form.Group controlId="vehicleId" className="mb-3">
                                <Form.Label>Vehicle</Form.Label>
                                <Form.Select
                                    value={vehicleId ?? ""}
                                    onChange={(e) => setVehicleId(e.target.value || null)}
                                >
                                    <option value="">-- Select Vehicle --</option>
                                    {vehicles.map(v => (
                                        <option key={v.id} value={v.id}>
                                            {v.name}
                                        </option>
                                    ))}}
                                </Form.Select>
                            </Form.Group>
                        </Col>
                        <Col>
                            <Form.Group controlId="serviceType" className="mb-3">
                                <Form.Label>Service Type</Form.Label>
                                <Form.Select
                                    value={serviceType ?? ""}
                                    onChange={(e) => setServiceType(e.target.value || null)}
                                >
                                    <option value="">-- Select Service Type --</option>
                                    <option value="REGULAR">Regular</option>
                                    <option value="URGENT">Urgent</option>
                                </Form.Select>
                            </Form.Group>
                        </Col>
                        <Col>
                            <Form.Group controlId="type" className="mb-3">
                                <br/>
                                <Button type="submit" className="mt-lg-2 mt-md-2 mt-sm-2 mt-xl-2 mt-2">🔍 Search</Button>
                            </Form.Group>
                        </Col>
                    </Row>
                )}
            </Form>
        </Container>
    );
}
export default ServicesSearch;