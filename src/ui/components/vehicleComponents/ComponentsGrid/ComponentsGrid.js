import React from 'react';
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import ComponentCard from "../ComponentCard/ComponentCard";
const ComponentsGrid = ({components, showCondition = true, selectedIds = [], onToggleSelect}) => {
    return (
        <Container className="mt-3">
            <Row xs={1} sm={2} md={2} lg={4} className="g-3">
                {components.map(component => (
                    <Col key={component.id} className="mb-4">
                        <ComponentCard
                            component={component}
                            showCondition={showCondition}
                            isSelected={selectedIds.includes(component.id)}
                            onToggleSelect={() => onToggleSelect(component)}
                        />
                    </Col>
                ))}
            </Row>
        </Container>
    );
}
export default ComponentsGrid;