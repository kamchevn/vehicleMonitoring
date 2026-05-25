import React from "react";
import Register from "../../components/authentication/register/Register";
import Container from "react-bootstrap/Container";
import useAuth from "../../../hooks/useAuth";

const RegisterPage = () => {
    const { register } = useAuth();
    return (
        <>
            <Container style={{ marginTop: '50px', marginBottom: '50px'}}>
                <Register register={register}/>
            </Container>
        </>
    )
};

export default RegisterPage;
