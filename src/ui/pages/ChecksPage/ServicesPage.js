import React, {useEffect, useState} from 'react';
import Header from "../../components/layout/Header/Header";
import Spinner from "react-bootstrap/Spinner";
import Container from "react-bootstrap/Container";
import ChecksSearch from "../../components/checks/ServicesSearch/ServicesSearch";
import useServices from "../../../hooks/useServices";
import ServicesGrid from "../../components/checks/ServicesGrid/ServicesGrid";
import PagePagination from "../../components/pagination/PagePagination";
import {isLoggedIn} from "../../../service/AuthService";
import NotFoundPage from "../NotFoundPage/NotFoundPage";
const ServicesPage = () => {
    const {services, loading, pageInfo, fetchServicesPage} = useServices();
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);

    const [filterVehicle, setFilterVehicle] = useState("");
    const [filterServiceType, setFilterServiceType] = useState("");

    const loadPage = async (p, vehicleId, serviceType) => {
        const response = await fetchServicesPage(vehicleId, serviceType, p);
        setPage(p);
        setTotalPages(response.totalPages);
    };

    const handleSearch = (vehicleId, serviceType) => {
        setFilterVehicle(vehicleId);
        setFilterServiceType(serviceType);
        loadPage(1, vehicleId, serviceType);
    };

    useEffect(() => {
        if(isLoggedIn()){
            loadPage(1);
        }
    }, []);
    return (
        <>
            <Header/>
            {isLoggedIn() && (
                <Container>
                    <ChecksSearch onSearch={handleSearch}/>
                    {loading && (
                        <Spinner animation="grow" variant="dark" />
                    )}
                    {!loading && (
                        <>
                            <ServicesGrid services={services}/>
                            {totalPages > 1 && (
                                <PagePagination page={page}
                                                totalPages={totalPages}
                                                onPageChange={(newPage) => loadPage(newPage)}/>
                            )}
                        </>
                    )}
                </Container>
            )}
            {!isLoggedIn() && (
                <NotFoundPage/>
            )}
        </>
    );
}

export default ServicesPage;