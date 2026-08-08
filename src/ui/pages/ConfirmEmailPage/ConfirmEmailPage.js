import Container from "react-bootstrap/Container";
import ConfirmEmail from "../../components/authentication/confirmEmail/ConfirmEmail";
import useAuth from "../../../hooks/useAuth";

const ConfirmEmailPage = () => {
    const { confirmEmail } = useAuth();

    return (
        <Container style={{ marginTop: "150px", marginBottom: "50px" }}>
            <ConfirmEmail confirmEmail={confirmEmail} />
        </Container>
    );
};

export default ConfirmEmailPage;
