import "./Login.css"
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import {useEffect, useState} from "react";
import {useLocation, useNavigate} from "react-router";
import {Alert, Card, Form} from "react-bootstrap";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import {useMediaQuery} from "../../../../hooks/useMediaQuery";

const Login = ({login}) => {
    const isMobile = useMediaQuery("(max-width: 575px)");
    const moveButtons = useMediaQuery("(max-width: 575px)");
    const [username,setUsername] = useState("");
    const [password,setPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [show, setShow] = useState(false);
    const [alertVariant, setAlertVariant] = useState("");
    const [alertMessage, setAlertMessage] = useState("");
    const navigate = useNavigate();
    const location = useLocation();

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    useEffect(() => {
        if (location.state?.alertMessage) {
            setAlertVariant(location.state.alertVariant || "info");
            setAlertMessage(location.state.alertMessage);
            navigate(location.pathname, { replace: true, state: {} });
        }
    }, [location, navigate]);

    const handleSubmit = async (e) => {
        e.preventDefault();

        const payload = {
            username,
            password
        };

        try {
            await login(payload);
            navigate("/");
        } catch (err) {
            const message =
                err.response?.data?.message ||
                "Login failed. Please try again.";

            setErrorMessage(message);
            handleShow();
        }
    };

    const redirectRegister = () => {
        navigate("/register");
    };
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
            <Card border="dark" className={isMobile ? "mobile" : "pc"}>
                <Card.Header>Login</Card.Header>
                <Card.Body>
                    {alertMessage && (
                        <Alert
                            className="border-dark mb-3"
                            variant={alertVariant}
                            onClose={() => setAlertMessage("")}
                            dismissible
                        >
                            {alertMessage}
                        </Alert>
                    )}
                    <Form>
                        {!isMobile && (
                            <Row className="mb-4">
                                <Form.Group as={Col} md={6} lg={6} controlId="username">
                                    <Form.Label>Username <span style={{ color: "red"}}>*</span></Form.Label>
                                    <Form.Control type="text" value={username} onChange={(e) => setUsername(e.target.value)}/>
                                </Form.Group>
                                <Form.Group as={Col} md={6} lg={6} controlId="password">
                                    <Form.Label>Password <span style={{ color: "red"}}>*</span></Form.Label>
                                    <Form.Control type="password" value={password} onChange={(e) => setPassword(e.target.value)}/>
                                </Form.Group>
                            </Row>
                        )}
                        {isMobile && (
                            <>
                                <Row className="mb-4">
                                    <Form.Group as={Col} md={6} lg={6} controlId="username">
                                        <Form.Label>Username <span style={{ color: "red"}}>*</span></Form.Label>
                                        <Form.Control type="text" value={username} onChange={(e) => setUsername(e.target.value)}/>
                                    </Form.Group>
                                </Row>
                                <Row className="mb-4">
                                    <Form.Group as={Col} md={6} lg={6} controlId="password">
                                        <Form.Label>Password <span style={{ color: "red"}}>*</span></Form.Label>
                                        <Form.Control type="password" value={password} onChange={(e) => setPassword(e.target.value)}/>
                                    </Form.Group>
                                </Row>
                            </>
                        )}
                        {!moveButtons && (
                            <div style={{ display: "flex", justifyContent: "space-between"}}>
                                <Button variant="primary" className="mb-2" type="button" onClick={handleSubmit}>
                                    Login
                                </Button>
                                <div style={{ display: "flex"}}>
                                    <p className="mt-2 me-1">Don't have an account?</p>
                                    <Button className="mb-2 ms-2" variant="outline-secondary" onClick={redirectRegister}>
                                        Register
                                    </Button>
                                </div>
                            </div>
                        )}
                        {moveButtons && (
                            <div>
                                <Button variant="primary" className="mb-2" type="button" onClick={handleSubmit}>
                                    Login
                                </Button>
                                <div style={{ display: "flex"}}>
                                    <Button className="mt-2 me-3" variant="outline-secondary" onClick={redirectRegister}>
                                        Register
                                    </Button>
                                    <p className="mb-1" style={{ marginTop: "15px"}}>Don't have an account?</p>
                                </div>
                            </div>
                        )}
                    </Form>
                </Card.Body>
            </Card>
        </>
    )
};

export default Login;