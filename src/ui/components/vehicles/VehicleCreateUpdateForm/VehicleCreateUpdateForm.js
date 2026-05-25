import "./VehicleCreateUpdateForm.css";
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import {useEffect, useState} from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import {useNavigate} from "react-router";
import {useMediaQuery} from "../../../../hooks/useMediaQuery";
import React from "react";
import useTemplates from "../../../../hooks/useTemplates";
import { createComponentsFromTemplates } from "../../../../service/ComponentsService";

const VehicleCreateUpdateForm = ({ vehicle, onAdd, onEdit }) => {
    const { findByVehicleType } = useTemplates();
    const isMobile = useMediaQuery("(max-width: 575px)");
    const isSmallTablet = useMediaQuery("(min-width: 576px) and (max-width: 767px)");
    const isBigTablet = useMediaQuery("(min-width: 768px) and (max-width: 991px)");
    const [id,setId] = useState("");
    const [name,setName] = useState("");
    const [year,setYear] = useState("");
    const [totalKilometers,setTotalKilometers] = useState("0");
    const [type,setType] = useState("");
    const [fuelType,setFuelType] = useState("");
    const [coolingType,setCoolingType] = useState("");
    const [drivenType,setDrivenType] = useState("");
    const [condition,setCondition] = useState("UNKNOWN");
    const [insertPeriodType,setInsertPeriodType] = useState("MONTHLY");
    const [errorMessage,setErrorMessage] = useState("");
    const navigate = useNavigate();

    const [show, setShow] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const isEditMode = id !== "";

    const checkErrorsOrSubmit = async (e) => {
        e.preventDefault();
        let errors = "";
        if(!/^\d{4}$/.test(year)){
            errors = "Invalid year input."
        }
        if(type === ""){
            errors = "Please select a vehicle type."
        }
        if(type === "CAR" && fuelType === ""){
            errors = "Please select type of fuel for the vehicle."
        }
        if(type === "MOTORCYCLE" && coolingType === ""){
            errors = "Please select type of cooling for the vehicle."
        }
        if(type === "MOTORCYCLE" && drivenType === ""){
            errors = "Please select type of component included in the vehicle."
        }
        if (errors) {
            setErrorMessage(errors);
            return handleShow();
        }
        else{
            const formData = new FormData();
            let createdVehicle;
            if(!vehicle){
                formData.append("id",id);
            }
            formData.append("name",name);
            formData.append("year",year);
            formData.append("totalKilometers",totalKilometers);
            formData.append("type",type);
            if(type==="CAR" && fuelType !== ""){
                formData.append("fuelType",fuelType);
            }
            if(type==="MOTORCYCLE" && coolingType !== ""){
                formData.append("coolingType",coolingType);
            }
            if(type==="MOTORCYCLE" && drivenType !== ""){
                formData.append("drivenType",drivenType);
            }
            formData.append("condition",condition);
            formData.append("insertPeriodType",insertPeriodType);
            try {
                if(vehicle){
                    await onEdit(id, formData);
                }
                else {
                    createdVehicle = await onAdd(formData);
                }
                if (createdVehicle) {
                    const templates = await findByVehicleType({
                        vehicleType: createdVehicle.type,
                        fuelType: createdVehicle.fuelType,
                        coolingType: createdVehicle.coolingType,
                        drivenType: createdVehicle.drivenType
                    });

                    await createComponentsFromTemplates(createdVehicle, templates);
                }
                navigate("/vehicles");
            } catch(err){
                console.error(err);
                setErrorMessage("Failed to submit form.");
                return handleShow();
            }
        }
    }

    const exitErrorModal = () => {
        setErrorMessage("");
        handleClose();
    }


    useEffect(() => {
        if (vehicle) {
            setId(vehicle.id);
            setName(vehicle.name);
            setYear(vehicle.year);
            setTotalKilometers(vehicle.totalKilometers);
            setType(vehicle.type);
            if(vehicle.fuelType != null){
                setFuelType(vehicle.fuelType);
            }
            if(vehicle.coolingType != null){
                setCoolingType(vehicle.coolingType);
            }
            if(vehicle.drivenType != null){
                setDrivenType(vehicle.drivenType);
            }
            setCondition(vehicle.condition);
            setInsertPeriodType(vehicle.insertPeriodType);
        }
    }, [vehicle]);

    return (
        <>
            <Modal
                show={show}
                onHide={exitErrorModal}
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
            {isMobile && (
                <>
                    <Row className="mb-4">
                        <Form.Group as={Col} controlId="name">
                            <Form.Label>Name</Form.Label>
                            <Form.Control required type="text" placeholder="Name" value={name} onChange={(e) => setName(e.target.value)}/>
                        </Form.Group>
                    </Row>
                    <Row className="mb-4">
                        <Form.Group as={Col} controlId="year">
                            <Form.Label>Year</Form.Label>
                            <Form.Control required type="text" placeholder="Year" value={year} onChange={(e) => setYear(e.target.value)}/>
                        </Form.Group>

                        <Form.Group as={Col} xs={6} sm={6} md={6} lg={3} controlId="totalKilometers">
                            <Form.Label>Total Kilometers</Form.Label>
                            <Form.Control required type="text" placeholder="Example: 100000" value={totalKilometers} onChange={(e) => setTotalKilometers(e.target.value)}/>
                        </Form.Group>
                    </Row>
                    <Row className="mb-4">
                        <Form.Group as={Col} xs={6} sm={6} md={6} lg={3} controlId="type">
                            <Form.Label>Type</Form.Label>
                            <Form.Select
                                key={isEditMode ? "edit" : "create"}
                                required
                                value={type ?? ""}
                                onChange={(e) => {
                                    if (isEditMode) return;
                                    setType(e.target.value || null)
                                }}
                                disabled={isEditMode}
                                style={isEditMode ? { pointerEvents: "none", backgroundColor: "#e9ecef" } : {}}
                            >
                                <option value="">-- Select Type --</option>
                                <option value="CAR">Car</option>
                                <option value="BUS">Bus</option>
                                <option value="TRUCK">Truck</option>
                                <option value="MOTORCYCLE">Motorcycle</option>
                            </Form.Select>
                        </Form.Group>
                        {type==="CAR" && (
                            <Form.Group as={Col} controlId="fuelType">
                                <Form.Label>Fuel Type</Form.Label>
                                <Form.Select
                                    value={fuelType ?? ""}
                                    onChange={(e) => setFuelType(e.target.value || null)}
                                >
                                    <option value="">-- Select Fuel --</option>
                                    <option value="PETROL">Petrol</option>
                                    <option value="DIESEL">Diesel</option>
                                </Form.Select>
                            </Form.Group>
                        )}
                        {type==="MOTORCYCLE" && (
                            <Form.Group as={Col} controlId="coolingType">
                                <Form.Label>Cooling Type</Form.Label>
                                <Form.Select
                                    value={coolingType ?? ""}
                                    onChange={(e) => setCoolingType(e.target.value || null)}
                                >
                                    <option value="">-- Select Cooling --</option>
                                    <option value="AIR_COOLED">Air</option>
                                    <option value="WATER_COOLED">Water</option>
                                </Form.Select>
                            </Form.Group>
                        )}
                    </Row>
                    {type==="MOTORCYCLE" && (
                        <Row className="mb-4">
                            <Form.Group as={Col} xs={8} sm={8} md={4} lg={3} controlId="drivenType">
                                <Form.Label>Driven Type</Form.Label>
                                <Form.Select
                                    value={drivenType ?? ""}
                                    onChange={(e) => setDrivenType(e.target.value || null)}
                                >
                                    <option value="">-- Select Component --</option>
                                    <option value="BELT_DRIVEN">Belt-Driven</option>
                                    <option value="CHAIN_DRIVEN">Chain-Driven</option>
                                </Form.Select>
                            </Form.Group>
                        </Row>
                    )}
                </>
            )}
            {!isMobile && (
                <>
                <Row className="mb-4">
                    <Form.Group as={Col} xs={6} sm={6} md={7} lg={7} controlId="name">
                        <Form.Label>Name</Form.Label>
                        <Form.Control required type="text" placeholder="Name" value={name} onChange={(e) => setName(e.target.value)}/>
                    </Form.Group>

                    <Form.Group as={Col} xs={3} sm={3} md={2} lg={2} controlId="year">
                        <Form.Label>Year</Form.Label>
                        <Form.Control required type="text" placeholder="Year" value={year} onChange={(e) => setYear(e.target.value)}/>
                    </Form.Group>
                </Row>
                <Row className="mb-4">
                    <Form.Group as={Col} xs={3} sm={6} md={4} lg={2} controlId="totalKilometers">
                        <Form.Label>Total Kilometers</Form.Label>
                        <Form.Control required type="text" placeholder="Example: 100000" value={totalKilometers} onChange={(e) => setTotalKilometers(e.target.value)}/>
                    </Form.Group>

                    <Form.Group as={Col} xs={4} sm={6} md={4} lg={3} controlId="type">
                        <Form.Label>Type</Form.Label>
                        <Form.Select
                            key={isEditMode ? "edit" : "create"}
                            required
                            value={type ?? ""}
                            onChange={(e) => {
                                if (isEditMode) return;
                                setType(e.target.value || null)
                            }}
                            disabled={isEditMode}
                            style={isEditMode ? { pointerEvents: "none", backgroundColor: "#e9ecef" } : {}}
                        >
                            <option value="">-- Select Vehicle Type --</option>
                            <option value="CAR">Car</option>
                            <option value="BUS">Bus</option>
                            <option value="TRUCK">Truck</option>
                            <option value="MOTORCYCLE">Motorcycle</option>
                        </Form.Select>
                    </Form.Group>
                    {type==="CAR" && (
                        <Form.Group as={Col} xs={4} sm={4} md={4} lg={3} controlId="fuelType">
                            <Form.Label>Fuel Type</Form.Label>
                            <Form.Select
                                value={fuelType ?? ""}
                                onChange={(e) => setFuelType(e.target.value || null)}
                            >
                                <option value="">-- Select Fuel Type --</option>
                                <option value="PETROL">Petrol</option>
                                <option value="DIESEL">Diesel</option>
                            </Form.Select>
                        </Form.Group>
                    )}
                    {!isSmallTablet && !isBigTablet && type==="MOTORCYCLE" && (
                        <>
                        <Form.Group as={Col} xs={4} sm={4} md={4} lg={3} controlId="coolingType">
                            <Form.Label>Cooling Type</Form.Label>
                            <Form.Select
                                value={coolingType ?? ""}
                                onChange={(e) => setCoolingType(e.target.value || null)}
                            >
                                <option value="">-- Select Cooling Type --</option>
                                <option value="AIR_COOLED">Air</option>
                                <option value="WATER_COOLED">Water</option>
                            </Form.Select>
                        </Form.Group>
                        <Form.Group as={Col} xs={4} sm={4} md={4} lg={3} controlId="drivenType">
                            <Form.Label>Driven Type</Form.Label>
                            <Form.Select
                                value={drivenType ?? ""}
                                onChange={(e) => setDrivenType(e.target.value || null)}
                            >
                                <option value="">-- Select Component --</option>
                                <option value="BELT_DRIVEN">Belt-Driven</option>
                                <option value="CHAIN_DRIVEN">Chain-Driven</option>
                            </Form.Select>
                        </Form.Group>
                        </>
                    )}
                    {isBigTablet && type==="MOTORCYCLE" && (
                        <Form.Group as={Col} xs={4} sm={4} md={4} lg={3} controlId="coolingType">
                            <Form.Label>Cooling Type</Form.Label>
                            <Form.Select
                                value={coolingType ?? ""}
                                onChange={(e) => setCoolingType(e.target.value || null)}
                            >
                                <option value="">-- Select Cooling Type --</option>
                                <option value="AIR_COOLED">Air</option>
                                <option value="WATER_COOLED">Water</option>
                            </Form.Select>
                        </Form.Group>
                    )}
                </Row>
                </>
            )}
            {isBigTablet && type==="MOTORCYCLE" && (
                <Row className="mb-4">
                    <Form.Group as={Col} xs={4} sm={4} md={4} lg={3} controlId="drivenType">
                        <Form.Label>Driven Type</Form.Label>
                        <Form.Select
                            value={drivenType ?? ""}
                            onChange={(e) => setDrivenType(e.target.value || null)}
                        >
                            <option value="">-- Select Component --</option>
                            <option value="BELT_DRIVEN">Belt-Driven</option>
                            <option value="CHAIN_DRIVEN">Chain-Driven</option>
                        </Form.Select>
                    </Form.Group>
                </Row>
            )}
            {isSmallTablet && type==="MOTORCYCLE" && (
                <>
                <Row className="mb-4">
                    <Form.Group as={Col} xs={4} sm={6} md={6} lg={3} controlId="coolingType">
                        <Form.Label>Cooling Type</Form.Label>
                        <Form.Select
                            value={coolingType ?? ""}
                            onChange={(e) => setCoolingType(e.target.value || null)}
                        >
                            <option value="">-- Select Cooling Type --</option>
                            <option value="AIR_COOLED">Air</option>
                            <option value="WATER_COOLED">Water</option>
                        </Form.Select>
                    </Form.Group>
                    <Form.Group as={Col} xs={4} sm={6} md={6} lg={3} controlId="drivenType">
                        <Form.Label>Driven Type</Form.Label>
                        <Form.Select
                            value={drivenType ?? ""}
                            onChange={(e) => setDrivenType(e.target.value || null)}
                        >
                            <option value="">-- Select Component --</option>
                            <option value="BELT_DRIVEN">Belt-Driven</option>
                            <option value="CHAIN_DRIVEN">Chain-Driven</option>
                        </Form.Select>
                    </Form.Group>
                </Row>
                </>
            )}

            {isMobile && (
                    <Row className="mb-4">
                        <Form.Group as={Col} controlId="insertPeriodType">
                            <Form.Label>Insert Period Type</Form.Label>
                            <Form.Select
                                value={insertPeriodType}
                                onChange={(e) => setInsertPeriodType(e.target.value)}
                            >
                                <option value="MONTHLY">Monthly</option>
                                <option value="WEEKLY">Weekly</option>
                            </Form.Select>
                        </Form.Group>
                    </Row>
            )}
            {!isMobile && (
                <Row className="mb-4">
                    <Form.Group as={Col} md={4} lg={4} controlId="insertPeriodType">
                        <Form.Label>Insert Period Type</Form.Label>
                        <Form.Select
                            value={insertPeriodType}
                            onChange={(e) => setInsertPeriodType(e.target.value)}
                        >
                            <option value="MONTHLY">Monthly</option>
                            <option value="WEEKLY">Weekly</option>
                        </Form.Select>
                    </Form.Group>
                </Row>
            )}
            <Button variant="primary" className="mb-5" type="button" onClick={checkErrorsOrSubmit}>
                Submit
            </Button>
        </Form>
        </>
    )
}

export default VehicleCreateUpdateForm;