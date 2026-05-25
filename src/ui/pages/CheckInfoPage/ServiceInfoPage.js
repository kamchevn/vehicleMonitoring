import "./ServiceInfoPage.css"
import Header from "../../components/layout/Header/Header";
import Container from "react-bootstrap/Container";
import Button from "react-bootstrap/Button";
import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router";
import useServices from "../../../hooks/useServices";
import Spinner from "react-bootstrap/Spinner";
import Row from "react-bootstrap/Row";
import ServiceComponentCard from "../../components/checks/ServiceComponentCard/ServiceComponentCard";
import Col from "react-bootstrap/Col";
import {Pagination} from "react-bootstrap";
import {getUser, isLoggedIn} from "../../../service/AuthService";
import NotFoundPage from "../NotFoundPage/NotFoundPage";

const ServiceInfoPage = () => {
    const itemsPerPage = 4;
    const [page, setPage] = useState(1);
    const navigate = useNavigate();
    const { id } = useParams();
    const {findById} = useServices();
    const [service, setService] = useState(null);
    const [notFound, setNotFound] = useState(false);
    const [loadingService, setLoadingService] = useState(true);
    const goBack = () => {
        navigate('/services');
    }

    const componentDetails = service?.componentDetails || [];
    const totalPages = Math.ceil(componentDetails.length / itemsPerPage);
    const pagedComponentDetails = componentDetails.slice((page - 1) * itemsPerPage, page * itemsPerPage);

    useEffect(() => {
        if (!id || !isLoggedIn()) {
            setNotFound(true);
            setLoadingService(false);
            return;
        }

        const loadData = async () => {
            try {
                const response = await findById(id);

                if (!response) {
                    setNotFound(true);
                    return;
                }

                setService(response);
            }
            catch (error) {
                setNotFound(true);
            }
            finally {
                setLoadingService(false);
            }
        };

        loadData()
    },[id,findById])

    const user = getUser();
    const isAdmin = service && isLoggedIn() && user.roles?.includes("ROLE_ADMIN");
    const isOwnerOrAdmin = service && isLoggedIn() && (user.username === service.vehicle.username || user.roles?.includes("ROLE_ADMIN"));
    const showNotFound = !service || !isLoggedIn() || (user.username !== service.vehicle.username && user.roles?.includes("ROLE_USER"));
    return (
        <>
            <Header/>
            {loadingService && (
                <Container>
                    <Spinner animation="grow" variant="dark" />
                </Container>
            )}
            {!loadingService && !notFound && isOwnerOrAdmin && (
                <Container>
                    <Button variant="secondary" className="mb-3 backButton" onClick={goBack}>← Go back</Button>
                    <br/>
                    <h1>Service details</h1>
                    <br/>
                    {isAdmin && (
                        <h3>Vehicle: {service.vehicle.name} (Owned by: {service.vehicle.username})</h3>
                    )}
                    {!isAdmin && (
                        <h3>Vehicle: {service.vehicle.name}</h3>
                    )}
                    {service.serviceType === "REGULAR" && (
                        <h3>Service Type: Regular</h3>
                    )}
                    {service.serviceType === "URGENT" && (
                        <h3>Service Type: Urgent</h3>
                    )}
                    {service.previousCondition === "VERY_GOOD" && (
                        <h3>Previous condition: <span style={{color: "#146e43"}}>Very Good</span></h3>
                    )}
                    {service.previousCondition === "GOOD" && (
                        <h3>Previous condition: <span style={{color: "#0a54b9"}}>Good</span></h3>
                    )}
                    {service.previousCondition === "POOR" && (
                        <h3>Previous condition: <span style={{color: "#9f7605"}}>Poor</span></h3>
                    )}
                    {service.previousCondition === "OOS" && (
                        <h3>Previous condition: <span style={{color: "#9a2435"}}>Out Of Service</span></h3>
                    )}
                    {service.previousCondition === "UNKNOWN" && (
                        <h3>Previous condition: <span style={{color: "#3b3b3b"}}>Unknown</span></h3>
                    )}
                    {service.currentCondition === "VERY_GOOD" && (
                        <h3>Current condition: <span style={{color: "#146e43"}}>Very Good</span></h3>
                    )}
                    {service.currentCondition === "GOOD" && (
                        <h3>Current condition: <span style={{color: "#0a54b9"}}>Good</span></h3>
                    )}
                    {service.currentCondition === "POOR" && (
                        <h3>Current condition: <span style={{color: "#9f7605"}}>Poor</span></h3>
                    )}
                    {service.currentCondition === "OOS" && (
                        <h3>Current condition: <span style={{color: "#9a2435"}}>Out Of Service</span></h3>
                    )}
                    {service.currentCondition === "UNKNOWN" && (
                        <h3>Current condition: <span style={{color: "#3b3b3b"}}>Unknown</span></h3>
                    )}
                    {!service.note && (
                        <h3>Note: No note</h3>
                    )}
                    {service.note && (
                        <h3>Note: {service.note}</h3>
                    )}
                    <br/>
                    <h4>Components:</h4>
                    <br/>
                    {pagedComponentDetails.length === 0 && (
                        <p>No components found.</p>
                    )}
                    {pagedComponentDetails.length > 0 && (
                        <Container className="mb-4">
                            <Row xs={1} sm={2} md={2} lg={4} className="g-3">
                                {pagedComponentDetails.map(detail => (
                                    <Col key={detail.id} className="mb-4">
                                        <ServiceComponentCard componentDetail={detail}/>
                                    </Col>
                                ))}
                            </Row>
                            {totalPages > 1 && (
                                <div className="d-flex justify-content-center mt-4 mb-4">
                                    <Pagination>
                                        <Pagination.First
                                            disabled={page === 1}
                                            onClick={() => setPage(1)}
                                        />

                                        <Pagination.Prev
                                            disabled={page === 1}
                                            onClick={() => setPage(p => p - 1)}
                                        />

                                        {[...Array(totalPages)].map((_, i) => {
                                            const pageNumber = i + 1;
                                            return (
                                                <Pagination.Item
                                                    key={pageNumber}
                                                    active={pageNumber === page}
                                                    onClick={() => setPage(pageNumber)}
                                                >
                                                    {pageNumber}
                                                </Pagination.Item>
                                            );
                                        })}

                                        <Pagination.Next
                                            disabled={page === totalPages}
                                            onClick={() => setPage(p => p + 1)}
                                        />

                                        <Pagination.Last
                                            disabled={page === totalPages}
                                            onClick={() => setPage(totalPages)}
                                        />
                                    </Pagination>
                                </div>
                            )}
                        </Container>
                    )}
                </Container>
            )}
            {!loadingService && (notFound || showNotFound) && (
                <NotFoundPage/>
            )}
        </>
    )
}

export default ServiceInfoPage;