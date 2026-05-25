import "./VehicleServiceForm.css";
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import {Alert} from "react-bootstrap";
import {useEffect, useState} from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import {useMediaQuery} from "../../../../hooks/useMediaQuery";
import useVehicles from "../../../../hooks/useVehicles";
import useComponents from "../../../../hooks/useComponents";
import Spinner from "react-bootstrap/Spinner";
import ComponentsGrid from "../../vehicleComponents/ComponentsGrid/ComponentsGrid";
import PagePagination from "../../pagination/PagePagination";
import useServices from "../../../../hooks/useServices";
import {useNavigate} from "react-router";

const VehicleServiceForm = ({ serviceType,vehicle,isVehiclePreselected }) => {
    const isMobile = useMediaQuery("(max-width: 575px)");
    const { findAll } = useVehicles();
    const { fetchPage, components, loading, pageInfo } = useComponents();
    const { checkComponentsOrServiceVehicle } = useServices();
    const [vehicles, setVehicles] = useState([]);
    const [selectedVehicleId, setSelectedVehicleId] = useState("");
    const [selectedComponents, setSelectedComponents] = useState({}); // { [componentId]: condition }
    const [page, setPage] = useState(1);

    const [note, setNote] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [show, setShow] = useState(false);
    const navigate = useNavigate();
    const isUrgent = serviceType === "URGENT";
    const isRegular = serviceType === "REGULAR";
    const isUnknownCheck = isUrgent && vehicle?.condition === "UNKNOWN";

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const handleToggleSelect = (component) => {
        setSelectedComponents(prev => {
            if (prev[component.id]) {
                const copy = { ...prev };
                delete copy[component.id];
                return copy;
            }
            return {
                ...prev,
                [component.id]: {
                    name: component.name,
                    condition: "VERY_GOOD"
                }
            };
        });
    };

    const handleConditionChange = (componentId, newCondition) => {
        setSelectedComponents(prev => ({
            ...prev,
            [componentId]: newCondition
        }));
    };

    useEffect(() => {
        if (!isVehiclePreselected && isUrgent) {
            findAll()
                .then((res) => setVehicles(res))
                .catch(() => {
                    setErrorMessage("Failed to load vehicles.");
                    handleShow();
                });
        }
    }, [isVehiclePreselected, isUrgent, findAll]);

    useEffect(() => {
        if (isUrgent && !isVehiclePreselected && vehicles.length > 0 && !selectedVehicleId) {
            const firstId = vehicles[0].id;
            setSelectedVehicleId(String(firstId));
        }
    }, [vehicles, isUrgent, isVehiclePreselected]);

    const loadComponents = async (vehicleId, pageNum = 1) => {
        if (!vehicleId) return;

        let condition = "";
        if (isRegular) {
            condition = "POOR,OOS";
        }
        if (isUnknownCheck) {
            condition = "UNKNOWN";
        }

        try {
            await fetchPage(vehicleId, "", condition, pageNum, 4);
            setPage(pageNum);
        } catch {
            setErrorMessage("Failed to load components.");
            handleShow();
        }
    };

    const handleSubmit = async () => {
        const vehicleIdToUse = vehicle?.id || selectedVehicleId;

        if (!vehicleIdToUse) {
            setErrorMessage("Vehicle must be selected.");
            handleShow();
            return;
        }

        if (Object.keys(selectedComponents).length === 0) {
            setErrorMessage("Please select at least one component.");
            handleShow();
            return;
        }

        const payload = {
            serviceType,
            note,
            checkTime: new Date().toISOString(),
            componentDetails: Object.entries(selectedComponents).map(
                ([componentId, data]) => ({
                    componentId: Number(componentId),
                    currentCondition: data.condition
                })
            )
        };

        try {
            await checkComponentsOrServiceVehicle(
                vehicleIdToUse,
                payload
            );

            navigate(`/vehicles`);

        } catch (err) {
            setErrorMessage(
                err.response?.data || "Failed to submit condition check."
            );
            handleShow();
        }
    };

    useEffect(() => {
        if (selectedVehicleId) {
            setPage(1);
            loadComponents(selectedVehicleId, 1);
        }
    }, [selectedVehicleId]);

    useEffect(() => {
        if (vehicle) {
            setPage(1);
            loadComponents(vehicle.id, 1);
        }
    }, [vehicle]);

    return (
        <>
            <Modal
                show={show}
                onHide={handleClose}
                backdrop="static"
                keyboard={false}
                bg="dark"
                aria-labelledby="contained-modal-title-vcenter"
                centered
            >
                <Modal.Header>
                    <Modal.Title id="contained-modal-title-vcenter">Error</Modal.Title>
                </Modal.Header>
                <Modal.Body>{errorMessage}</Modal.Body>
                <Modal.Footer>
                    <Button variant="danger" onClick={handleClose}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>
        <Form>
            <Row className="mt-3 mb-4">
                <Form.Group as={Col} controlId="idSelect">
                    {isVehiclePreselected && (
                        <>
                            <Form.Label>Selected Vehicle</Form.Label>
                            <Form.Control
                                type="text"
                                disabled
                                value={vehicle?.name || ""}
                            />
                        </>
                    )}
                    {!isVehiclePreselected && isUrgent && (
                        <>
                            <Form.Label>Select Vehicle</Form.Label>
                            <Form.Select
                                value={selectedVehicleId}
                                onChange={(e) => {
                                    setSelectedVehicleId(e.target.value);
                                    setSelectedComponents({});
                                }}
                                style={{ maxHeight: "100px", overflowY: "auto" }}
                            >
                                {vehicles.map(v => (
                                    <option key={v.id} value={v.id}>
                                        {v.name}
                                    </option>
                                ))}
                            </Form.Select>
                        </>
                    )}
                </Form.Group>

                <Form.Group as={Col} controlId="checkType">
                    <Form.Label>Service Type</Form.Label>
                    <Form.Control disabled value={serviceType}/>
                </Form.Group>
            </Row>
            {isUnknownCheck && (
                <Alert variant="secondary">
                    Vehicle has UNKNOWN condition.
                    Initial component inspection required.
                </Alert>
            )}
            <Row className="mb-4">
                <Form.Group as={Col} md={6} lg={6} controlId="counter">
                    <Form.Label>Note</Form.Label>
                    <Form.Control type="textarea" value={note} onChange={(e) => setNote(e.target.value)}/>
                </Form.Group>
            </Row>

            {Object.keys(selectedComponents).length > 0 ? (
                <Row className="mb-4">
                    <Form.Group as={Col}>
                        <Form.Label>Selected Components:</Form.Label>
                        {Object.keys(selectedComponents).map(id => {
                            const { name, condition } = selectedComponents[id];
                            return (
                                <Row key={id} className="mb-2 align-items-center">
                                    {isMobile && (
                                        <Col className="mb-1" md={2}>{name}</Col>
                                    )}
                                    {!isMobile && (
                                        <Col md={2}>{name}</Col>
                                    )}
                                    <Col sm md={3}>
                                        <Form.Select
                                            value={condition}
                                            onChange={(e) =>
                                                setSelectedComponents(prev => ({
                                                    ...prev,
                                                    [id]: { ...prev[id], condition: e.target.value }
                                                }))
                                            }
                                        >
                                            <option value="VERY_GOOD">Very Good</option>
                                            <option value="GOOD">Good</option>
                                            <option value="POOR">Poor</option>
                                            <option value="OOS">Out of Service</option>
                                        </Form.Select>
                                    </Col>
                                </Row>
                            );
                        })}
                    </Form.Group>
                </Row>
            ) : (
                <Row className="mb-4">
                    <Form.Group as={Col} md={6} lg={6} controlId="counter">
                        <Form.Label>Components: <span>None selected!</span></Form.Label>
                    </Form.Group>
                </Row>
            )}
            <Button variant="success" className="mb-5" type="button" onClick={handleSubmit}>
                Submit
            </Button>
            <Row className="mb-3">
                {loading ? (
                    <Spinner animation="grow" variant="dark" />
                ) : (
                    <>
                        <ComponentsGrid components={components} showCondition={false} selectedIds={Object.keys(selectedComponents).map(id => Number(id))} onToggleSelect={(component) => handleToggleSelect(component)} />
                        <PagePagination
                            page={page}
                            totalPages={pageInfo?.totalPages || 1}
                            onPageChange={(newPage) => {
                                setPage(newPage);
                                loadComponents(vehicle?.id || selectedVehicleId, newPage);
                            }}
                        />
                    </>
                )}
            </Row>
        </Form>
        </>
    )
}

export default VehicleServiceForm;