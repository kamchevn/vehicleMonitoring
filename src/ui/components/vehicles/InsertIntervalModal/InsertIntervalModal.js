import { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import {Alert} from "react-bootstrap";

const InsertIntervalModal = ({vehicle,unit,insertIntervalForVehicle}) => {
    const [amount, setAmount] = useState("");
    const [error, setError] = useState("");
    const [show, setShow] = useState(false);

    const handleClose = () => {
        setShow(false);
        setAmount("");
        setError("");
    };

    const handleShow = () => setShow(true);

    const calculateLateFlag = () => {
        const now = new Date();

        // JS: 0=Sunday, 1=Monday, ..., 6=Saturday
        const dayOfWeek = now.getDay();

        if (vehicle.insertPeriodType === "WEEKLY") {
            // Sat (6), Sun (0) → NOT late
            if (dayOfWeek === 6 || dayOfWeek === 0) {
                return false;
            }

            // Mon (1), Tue (2) → late
            if (dayOfWeek === 1 || dayOfWeek === 2) {
                return true;
            }
        }

        if (vehicle.insertPeriodType === "MONTHLY") {
            const dayOfMonth = now.getDate();
            const lastDayOfMonth = new Date(
                now.getFullYear(),
                now.getMonth() + 1,
                0
            ).getDate();

            // Last 4 days → NOT late
            if (dayOfMonth >= lastDayOfMonth - 3) {
                return false;
            }

            // First 4 days → late
            if (dayOfMonth <= 4) {
                return true;
            }
        }

        // Fallback safety (should never happen if buttons are shown correctly)
        return false;
    };

    const late = calculateLateFlag();
    const handleInsert = async (e) => {
        e.preventDefault();

        const formData = new FormData();
        formData.append("unitType",unit);
        formData.append("amount",amount);
        formData.append("late", late);
        formData.append("insertTime", new Date().toISOString().slice(0, -1));

        try {
            await insertIntervalForVehicle(vehicle.id, formData);
            handleClose();
        }
        catch(err){
            console.error(err);
        }
    }

    return (
        <>
            <Button className="mb-2" variant="outline-primary" onClick={handleShow}>
                {unit === "KILOMETERS" ? "⏲ Insert kilometers" : "⛽ Insert fuel"}
            </Button>

            <Modal show={show} onHide={handleClose} backdrop="static" keyboard={false} bg="dark" aria-labelledby="contained-modal-title-vcenter" centered>
                <Modal.Header>
                    <Modal.Title id="contained-modal-title-vcenter">Insert {unit === "KILOMETERS" ? "Kilometers" : "Fuel"}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {error && <Alert variant="danger">{error}</Alert>}

                    <Form.Group>
                        <Form.Label>
                            Amount ({unit === "KILOMETERS" ? "km" : "liters"})
                        </Form.Label>
                        <Form.Control type="number" min="1" value={amount} onChange={(e) => setAmount(e.target.value)}/>
                    </Form.Group>
                </Modal.Body>
                    {late && (
                        <Modal.Footer style={{ display: "flex", justifyContent: "space-between"}}>
                            <Alert className="border-dark p-2" key="warning" variant="warning">
                                Warning: This insert will be sent as late.
                            </Alert>
                            <div style={{ display: "flex"}}>
                                <Button variant="outline-danger" className="me-2" onClick={handleClose}>
                                    Cancel
                                </Button>
                                <Button variant="primary" onClick={handleInsert}>
                                    Insert
                                </Button>
                            </div>
                        </Modal.Footer>
                    )}
                    {!late && (
                        <Modal.Footer>
                            <Button variant="outline-danger" onClick={handleClose}>
                                Cancel
                            </Button>
                            <Button variant="primary" onClick={handleInsert}>
                                Insert
                            </Button>
                        </Modal.Footer>
                    )}
            </Modal>
        </>
    );
}

export default InsertIntervalModal;