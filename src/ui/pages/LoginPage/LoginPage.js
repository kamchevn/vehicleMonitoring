import Container from "react-bootstrap/Container";
import Login from "../../components/authentication/login/Login";
import useAuth from "../../../hooks/useAuth";

const LoginPage = () => {
    const { login } = useAuth();
    return (
        <>
            <Container style={{ marginTop: "150px"}}>
                <Login login={login}/>
            </Container>
        </>
    )
};

export default LoginPage;