import "./EmailVerificationNote.css";
import Button from "react-bootstrap/Button";
import {useNavigate} from "react-router";
import {Card} from "react-bootstrap";
import {useMediaQuery} from "../../../../hooks/useMediaQuery";

const EmailVerificationNote = () => {
    const isMobile = useMediaQuery("(max-width: 575px)");
    const navigate = useNavigate();

    const redirectLogin = () => {
        navigate("/login");
    };

    return (
        <Card border="dark" className={isMobile ? "mobile" : "pc"}>
            <Card.Header>Verify your email</Card.Header>
            <Card.Body>
                <Card.Title>Confirmation link sent</Card.Title>
                <Card.Text>
                    We have sent a confirmation link to your email address.
                    Please open the link to activate your account before logging in.
                </Card.Text>
                <Card.Text className="text-muted mb-4">
                    The link will expire in 24 hours.
                </Card.Text>
                <Button variant="primary" onClick={redirectLogin}>
                    Go to login
                </Button>
            </Card.Body>
        </Card>
    );
};

export default EmailVerificationNote;
