import React from 'react';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import {useState} from "react";
import {useMediaQuery} from "../../../../hooks/useMediaQuery";

const VehiclesSearch = ({ onSearch }) => {
    const isMobile = useMediaQuery("(max-width: 575px)");
    const [name, setName] = useState("");
    const [type, setType] = useState("");
    const handleSubmit = event => {
        event.preventDefault();
        onSearch(name, type);
    };

    return (
        <Container>
            <Form onSubmit={handleSubmit}>
                {isMobile && (
                    <>
                        <Row>
                            <Col>
                                <Form.Group controlId="Name" className="mb-3">
                                    <Form.Label>Name</Form.Label>
                                    <Form.Control type="text" placeholder="Name" value={name}
                                                  onChange={(e) => setName(e.target.value)}/>
                                </Form.Group>
                            </Col>
                        </Row>
                        <Row>
                            <Col>
                                <Form.Group controlId="type">
                                    <Form.Label>Type</Form.Label>
                                    <Form.Select
                                        value={type ?? ""}
                                        onChange={(e) => setType(e.target.value || null)}
                                    >
                                        <option value="">-- Select Type --</option>
                                        <option value="CAR">Car</option>
                                        <option value="BUS">Bus</option>
                                        <option value="TRUCK">Truck</option>
                                        <option value="MOTORCYCLE">Motorcycle</option>
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
                            <Form.Group controlId="Name" className="mb-3">
                                <Form.Label>Name</Form.Label>
                                <Form.Control type="text" placeholder="Name" value={name}
                                              onChange={(e) => setName(e.target.value)}/>
                            </Form.Group>
                        </Col>
                        <Col>
                            <Form.Group controlId="type" className="mb-3">
                                <Form.Label>Type</Form.Label>
                                <Form.Select
                                    value={type ?? ""}
                                    onChange={(e) => setType(e.target.value || null)}
                                >
                                    <option value="">-- Select Type --</option>
                                    <option value="CAR">Car</option>
                                    <option value="BUS">Bus</option>
                                    <option value="TRUCK">Truck</option>
                                    <option value="MOTORCYCLE">Motorcycle</option>
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
export default VehiclesSearch;