import React from 'react';
import "./Header.css";
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import {useMediaQuery} from "../../../../hooks/useMediaQuery";
import {DropdownButton, Dropdown} from "react-bootstrap";
import Button from "react-bootstrap/Button";
import {useNavigate} from "react-router";
import { logout, isLoggedIn, getUser } from "../../../../service/AuthService";

const Header = () => {
    const isMobile = useMediaQuery("(max-width: 575px)");
    const hideUser = useMediaQuery("(min-width: 575px) and (max-width: 768px)");
    const user = getUser()
    const navigate = useNavigate();

    const redirectLogin = () => {
        navigate("/login");
    };

    const redirectRegister = () => {
        navigate("/register");
    };

    const handleLogout = () => {
        logout();
        if (window.location.pathname === "/") {
            window.location.reload();
        } else {
            navigate("/");
        }
    }

    return (
        <Navbar bg="dark" data-bs-theme="dark" className="mb-3">
            <Container style={{ display: "flex", justifyContent: "space-between"}}>
                {!isMobile && (
                    <div style={{ display: "flex"}}>
                        <Navbar.Brand className="ms-1" href="/">🖥 vehicleMonitor</Navbar.Brand>
                        {isLoggedIn() && (
                            <Nav className="me-auto">
                                <Nav.Link href="/vehicles">Vehicles</Nav.Link>
                                <Nav.Link href="/services">Services</Nav.Link>
                            </Nav>
                        )}
                    </div>
                )}
                {isMobile && isLoggedIn() && (
                    <div>
                        <Navbar.Brand className="mt-4" href="/">🖥 vehicleMonitor</Navbar.Brand>
                        <p className="mt-2 mb-0" style={{ color: "white" }}>Welcome {user.username}!</p>
                    </div>
                )}
                {isMobile && !isLoggedIn() && (
                    <Navbar.Brand className="ms-1" href="/">🖥 vehicleMonitor</Navbar.Brand>
                )}
                <div style={{ display: "flex" }}>
                    {isMobile && isLoggedIn() && (
                        <>
                            <DropdownButton className="justify-content-end" id="dropdown-basic-button" variant="secondary" align="end" title="Menu">
                                <Dropdown.Item href="/vehicles">Vehicles</Dropdown.Item>
                                <Dropdown.Item href="/services">Services</Dropdown.Item>
                                <Dropdown.Item onClick={handleLogout}>Logout</Dropdown.Item>
                            </DropdownButton>
                        </>
                    )}
                    {isMobile && !isLoggedIn() && (
                        <DropdownButton className="justify-content-end" id="dropdown-basic-button" variant="secondary" align="end" title="Menu">
                            <Dropdown.Item href="/login">Login</Dropdown.Item>
                            <Dropdown.Item href="/register">Register</Dropdown.Item>
                        </DropdownButton>
                    )}
                    {!isMobile && !isLoggedIn() && (
                        <>
                            <Button className="me-2" variant="outline-secondary" onClick={redirectLogin}>Login</Button>
                            <Button variant="outline-secondary" onClick={redirectRegister}>Register</Button>
                        </>
                    )}
                    {!isMobile && isLoggedIn() && (
                        <>
                            {!hideUser && (
                                <p className="mb-0" style={{ color: "white", marginTop: "7px" }}>Welcome {user.username}!</p>
                            )}
                            <Button className="ms-3 me-2" variant="outline-secondary" onClick={handleLogout}>Logout</Button>
                        </>
                    )}
                </div>
            </Container>
        </Navbar>
    );
}

export default Header;