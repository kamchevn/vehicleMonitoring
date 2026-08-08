import "./Register.css";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import {useState} from "react";
import {useNavigate} from "react-router";
import {Card, Form} from "react-bootstrap";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import {useMediaQuery} from "../../../../hooks/useMediaQuery";

const Register = ({register}) => {
    const isMobile = useMediaQuery("(max-width: 575px)");
    const [username,setUsername] = useState("");
    const [password,setPassword] = useState("");
    const [repeatPassword,setRepeatPassword] = useState("");
    const [name,setName] = useState("");
    const [surname, setSurname] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [show, setShow] = useState(false);
    const navigate = useNavigate();

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const hasUppercase = /[A-Z]/.test(password);
    const hasNumber = /\d/.test(password);
    const hasSymbol = /[^A-Za-z0-9]/.test(password);
    const handleSubmit = async (e) => {
        e.preventDefault();

        const payload = {
            username,
            password,
            repeatPassword,
            name,
            surname,
            role: "ROLE_USER"
        };

        try {
            await register(payload);
            navigate("/email-verification");
        } catch (err) {
            const message = err.response?.data?.message || "Registration failed. Please try again.";

            setErrorMessage(message);
            handleShow();
        }
    };

    const redirectLogin = () => {
        navigate("/login");
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
                <Card.Header>Register</Card.Header>
                <Card.Body>
                    <Form>
                        {!isMobile && (
                            <Row className="mb-4">
                                <Form.Group as={Col} md={6} lg={6} controlId="name">
                                    <Form.Label>Name <span style={{ color: "red"}}>*</span></Form.Label>
                                    <Form.Control type="text" value={name} onChange={(e) => setName(e.target.value)}/>
                                </Form.Group>
                                <Form.Group as={Col} md={6} lg={6} controlId="surname">
                                    <Form.Label>Surname <span style={{ color: "red"}}>*</span></Form.Label>
                                    <Form.Control type="text" value={surname} onChange={(e) => setSurname(e.target.value)}/>
                                </Form.Group>
                            </Row>
                        )}
                        {isMobile && (
                            <>
                                <Row className="mb-4">
                                    <Form.Group as={Col} md={6} lg={6} controlId="name">
                                        <Form.Label>Name <span style={{ color: "red"}}>*</span></Form.Label>
                                        <Form.Control type="text" value={name} onChange={(e) => setName(e.target.value)}/>
                                    </Form.Group>
                                </Row>
                                <Row className="mb-4">
                                    <Form.Group as={Col} md={6} lg={6} controlId="surname">
                                        <Form.Label>Surname <span style={{ color: "red"}}>*</span></Form.Label>
                                        <Form.Control type="text" value={surname} onChange={(e) => setSurname(e.target.value)}/>
                                    </Form.Group>
                                </Row>
                            </>
                        )}
                        <Row className="mb-4">
                            <Form.Group as={Col} md={6} lg={6} controlId="username">
                                <Form.Label>Username <span style={{ color: "red"}}>*</span></Form.Label>
                                <Form.Control type="text" value={username} onChange={(e) => setUsername(e.target.value)}/>
                            </Form.Group>
                        </Row>
                        {!isMobile && (
                            <Row className="mb-4">
                                <Form.Group as={Col} md={6} lg={6} controlId="password">
                                    <Form.Label>Password <span style={{ color: "red"}}>*</span></Form.Label>
                                    <Form.Control type="password" value={password} onChange={(e) => setPassword(e.target.value)}/>
                                </Form.Group>
                                <Form.Group as={Col} md={6} lg={6} controlId="repeatPassword">
                                    <Form.Label>Confirm Password <span style={{ color: "red" }}>*</span></Form.Label>
                                    <Form.Control type="password" value={repeatPassword} onChange={(e) => setRepeatPassword(e.target.value)}/>
                                </Form.Group>
                            </Row>
                        )}
                        {isMobile && (
                            <>
                                <Row className="mb-4">
                                    <Form.Group as={Col} md={6} lg={6} controlId="password">
                                        <Form.Label>Password <span style={{ color: "red"}}>*</span></Form.Label>
                                        <Form.Control type="password" value={password} onChange={(e) => setPassword(e.target.value)}/>
                                    </Form.Group>
                                </Row>
                                <Row className="mb-4">
                                    <Form.Group as={Col} md={6} lg={6} controlId="repeatPassword">
                                        <Form.Label>Confirm Password <span style={{ color: "red" }}>*</span></Form.Label>
                                        <Form.Control type="password" value={repeatPassword} onChange={(e) => setRepeatPassword(e.target.value)}/>
                                    </Form.Group>
                                </Row>
                            </>
                        )}
                        <Row className="mb-2">
                            <p>Password must include <span style={{ color: hasUppercase ? "green" : "red" }}>1 uppercase letter</span>, <span style={{ color: hasNumber ? "green" : "red"}}>1 number</span> and <span style={{ color: hasSymbol ? "green" : "red" }}>1 symbol</span>.</p>
                        </Row>
                        <div style={{ display: "flex", justifyContent: "space-between"}}>
                            <Button variant="primary" className="mb-2" type="button" onClick={handleSubmit}>
                                Register
                            </Button>
                            <Button className="mb-2 ms-2" variant="outline-secondary" onClick={redirectLogin}>
                                Back to login
                            </Button>
                        </div>
                    </Form>
                </Card.Body>
            </Card>
        </>
    )
}

export default Register;