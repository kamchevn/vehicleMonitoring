import React from 'react';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import {useState} from "react";
import {useMediaQuery} from "../../../../hooks/useMediaQuery";

const ComponentsSearch = ({ onSearch }) => {
    const isMobile = useMediaQuery("(max-width: 575px)");
    const [measuringUnit, setMeasuringUnit] = useState("");
    const [condition, setCondition] = useState("");
    const handleSubmit = event => {
        event.preventDefault();
        onSearch(measuringUnit, condition);

    };

    return (
        <Container>
            <Form onSubmit={handleSubmit}>
                {isMobile && (
                    <>
                        <Row>
                            <Col>
                                <Form.Group controlId="measuringUnit" className="mb-3">
                                    <Form.Label>Measuring Unit</Form.Label>
                                    <Form.Select
                                        value={measuringUnit}
                                        onChange={(e) => setMeasuringUnit(e.target.value)}
                                    >
                                        <option value="">-- Select Measuring Unit --</option>
                                        <option value="KILOMETERS">Kilometers</option>
                                        <option value="BURNT_FUEL">Burnt Fuel</option>
                                    </Form.Select>
                                </Form.Group>
                            </Col>
                        </Row>
                        <Row>
                            <Col>
                                <Form.Group controlId="condition" className="mb-3">
                                    <Form.Label>Condition</Form.Label>
                                    <Form.Select
                                        value={condition}
                                        onChange={(e) => setCondition(e.target.value)}
                                    >
                                        <option value="">-- Select Condition --</option>
                                        <option value="VERY_GOOD">Very Good</option>
                                        <option value="GOOD">Good</option>
                                        <option value="POOR">Poor</option>
                                        <option value="OOS">Not Functional</option>
                                    </Form.Select>
                                </Form.Group>
                            </Col>
                        </Row>
                        <Row>
                            <Col>
                                <Form.Group controlId="type" className="mb-3">
                                    <Button type="submit" className="mt-lg-2 mt-md-2 mt-sm-2 mt-xl-2 mt-2">🔍 Search</Button>
                                </Form.Group>
                            </Col>
                        </Row>
                    </>
                )}
                {!isMobile && (
                    <Row>
                        <Col>
                            <Form.Group controlId="measuringUnit" className="mb-3">
                                <Form.Label>Measuring Unit</Form.Label>
                                <Form.Select
                                    value={measuringUnit}
                                    onChange={(e) => setMeasuringUnit(e.target.value)}
                                >
                                    <option value="">-- Select Unit --</option>
                                    <option value="KILOMETERS">Kilometers</option>
                                    <option value="BURNT_FUEL">Burnt Fuel</option>
                                </Form.Select>
                            </Form.Group>
                        </Col>
                        <Col>
                            <Form.Group controlId="condition" className="mb-3">
                                <Form.Label>Condition</Form.Label>
                                <Form.Select
                                    value={condition}
                                    onChange={(e) => setCondition(e.target.value)}
                                >
                                    <option value="">-- Select Condition --</option>
                                    <option value="VERY_GOOD">Very Good</option>
                                    <option value="GOOD">Good</option>
                                    <option value="POOR">Poor</option>
                                    <option value="OOS">Not Functional</option>
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
export default ComponentsSearch;