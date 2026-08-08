import Container from "react-bootstrap/Container";
import EmailVerificationNote from "../../components/authentication/emailVerificationNote/EmailVerificationNote";

const EmailVerificationNotePage = () => {
    return (
        <Container style={{ marginTop: "150px", marginBottom: "50px" }}>
            <EmailVerificationNote />
        </Container>
    );
};

export default EmailVerificationNotePage;
