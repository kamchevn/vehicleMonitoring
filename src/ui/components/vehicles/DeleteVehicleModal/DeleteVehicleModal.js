import { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';

const DeleteVehicleModal = ({vehicle,onDelete}) => {
    const [show, setShow] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const handleDelete = () => {
        onDelete(vehicle.id)
        handleClose()
    }

    return (
        <>
            <Button variant="danger" onClick={handleShow}>
                🗑️ Delete
            </Button>

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
                    <Modal.Title id="contained-modal-title-vcenter">Delete vehicle</Modal.Title>
                </Modal.Header>
                <Modal.Body>Are you sure that you want to delete {vehicle.name}?</Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Cancel
                    </Button>
                    <Button variant="danger" onClick={handleDelete}>
                        Delete
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default DeleteVehicleModal;