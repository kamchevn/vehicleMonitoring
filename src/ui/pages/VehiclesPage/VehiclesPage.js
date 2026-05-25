import React, {useEffect, useState} from 'react';
import { getUser, isLoggedIn } from "../../../service/AuthService";
import Header from "../../components/layout/Header/Header";
import VehiclesSearch from "../../components/vehicles/VehiclesSearch/VehiclesSearch";
import useVehicles from "../../../hooks/useVehicles";
import Spinner from "react-bootstrap/Spinner";
import Container from "react-bootstrap/Container";
import VehiclesGrid from "../../components/vehicles/VehiclesGrid/VehiclesGrid";
import PagePagination from "../../components/pagination/PagePagination";
import NotFoundPage from "../NotFoundPage/NotFoundPage";

const VehiclesPage = () => {
    const {vehicles, loading, fetchPage, onDelete, insertIntervalForVehicle} = useVehicles();
    const [page, setPage] = useState(1);
    const [user, setUser] = useState(null);
    const [totalPages, setTotalPages] = useState(1);

    const [filterDept, setFilterDept] = useState("");
    const [filterType, setFilterType] = useState("");

    useEffect(() => {
        if (!isLoggedIn()) {
            // optional: redirect to login
            return;
        }

        const loggedUser = getUser();
        setUser(loggedUser);
    }, []);

    const loadPage = async (p = 1, name, type) => {
        const response = await fetchPage(name, type, p);
        setPage(p);
        setTotalPages(response.totalPages);
    };

    const handleSearch = (dept, type) => {
        setFilterDept(dept);
        setFilterType(type);
        loadPage(1, dept, type);
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
                    <VehiclesSearch onSearch={handleSearch}/>
                    {loading && (
                        <Spinner animation="grow" variant="dark" />
                    )}
                    {!loading && (
                        <>
                            <VehiclesGrid vehicles={vehicles} onDelete={onDelete} insertIntervalForVehicle={insertIntervalForVehicle}/>
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

export default VehiclesPage;