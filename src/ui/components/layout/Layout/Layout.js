import "./Layout.css";
import React from 'react';
import Header from "../Header/Header";
import Container from 'react-bootstrap/Container';
import {Alert} from "react-bootstrap";
import { isLoggedIn } from "../../../../service/AuthService";
const Layout = () => {
    return (
        <>
            <Header/>
            <Container>
                <Alert variant="primary">
                    <Alert.Heading>Vehicle Monitoring Solution</Alert.Heading>
                    <p>
                        This application is for the purpose of monitoring the condition of multiple vehicles, by inserting
                        distance driven and fuel burnt in a weekly/monthly basis, and managing their regular/urgent services.
                    </p>
                    <hr />
                    {isLoggedIn() && (
                        <p className="mb-0">
                            The vehicles and checks can be found in the "Vehicles" and "Checks" tabs on the top (or in the "Menu" if you're on mobile).
                        </p>
                    )}
                    {!isLoggedIn() && (
                        <p className="mb-0">
                            To use the features from this app, please log in with your account, or register if you haven't created one.
                        </p>
                    )}
                </Alert>
            </Container>
        </>
    );
}

export default Layout;