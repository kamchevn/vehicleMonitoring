import React from 'react';
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import {Accordion} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {useNavigate} from "react-router";

const ServicesGrid = ({ services }) => {
    const navigate = useNavigate();
    const redirectToDetails = (serviceId) => {
        navigate(`/services/${serviceId}`)
    }
    return (
        <Container className="mt-3">
            <Row xs={1} className="g-3">
                {services.map((service, index) => (
                    <Col key={service.id ?? index} xs={12} sm={12} md={6} lg={4}>
                        <Accordion>
                            <Accordion.Item eventKey="0">
                                <Accordion.Header>
                                    🔹 Date inserted: {new Date(service.timeOfEntry).toLocaleString("en-GB", {
                                        year: "numeric",
                                        month: "short",
                                        day: "2-digit",
                                        hour: "2-digit",
                                        minute: "2-digit",
                                        second: "2-digit"
                                    })} — Vehicle: {service.vehicle?.name || "Unknown Vehicle"} — Type: {service.serviceType === "URGENT" ? "Urgent" : "Regular"}
                                </Accordion.Header>

                                <Accordion.Body>
                                    <p><strong>Updated Condition:</strong> {service.currentCondition}</p>
                                    <p><strong>Number of components serviced:</strong> {service.componentDetails.length}</p>
                                    <p><strong>Note:</strong> {service.note ?? "No note"}</p>
                                    <Button variant="info" onClick={() => redirectToDetails(service.id)}>
                                        ℹ️ More info
                                    </Button>
                                </Accordion.Body>
                            </Accordion.Item>
                        </Accordion>
                    </Col>
                ))}
            </Row>
        </Container>
    );
}

export default ServicesGrid;