import './VehicleInfoPage.css'
import React, {useEffect, useState} from 'react';
import Header from "../../components/layout/Header/Header";
import Container from "react-bootstrap/Container";
import {useNavigate, useParams} from "react-router";
import useVehicles from "../../../hooks/useVehicles";
import Button from "react-bootstrap/Button";
import useComponents from "../../../hooks/useComponents";
import Spinner from "react-bootstrap/Spinner";
import PagePagination from "../../components/pagination/PagePagination";
import ComponentsGrid from "../../components/vehicleComponents/ComponentsGrid/ComponentsGrid";
import ComponentSearch from "../../components/vehicleComponents/ComponentSearch/ComponentsSearch";
import {getUser, isLoggedIn} from "../../../service/AuthService";
import NotFoundPage from "../NotFoundPage/NotFoundPage";

const VehicleInfoPage = () => {
    const [vehicle,setVehicle] = useState(null);
    const [notFound, setNotFound] = useState(false);
    const [loadingVehicle, setLoadingVehicle] = useState(true);
    const {components, loading, fetchPage} = useComponents();
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);

    const [filterMeasuringUnit, setFilterMeasuringUnit] = useState("");
    const [filterCondition, setFilterCondition] = useState("");
    const {findById} = useVehicles();
    const { id } = useParams();
    const navigate = useNavigate();

    const goBack = () => {
        navigate("/vehicles");
    };

    const handleSearch = async (measuringUnit, condition) => {
        setFilterMeasuringUnit(measuringUnit);
        setFilterCondition(condition);

        const response = await fetchPage(vehicle.id, measuringUnit, condition, 1, 4);
        setPage(1);
        setTotalPages(response.totalPages)
    };

    useEffect(() => {
        if (!id || !isLoggedIn()) {
            setNotFound(true);
            setLoadingVehicle(false);
            return;
        }

        const loadData = async () => {
            try {
                const vehicleData = await findById(id);

                if (!vehicleData) {
                    setNotFound(true);
                    return;
                }

                setVehicle(vehicleData);

                const response = await fetchPage(vehicleData.id, "", "", 1, 4);
                setPage(1);
                setTotalPages(response.totalPages);
            } catch (error) {
                setNotFound(true);
            } finally {
                setLoadingVehicle(false);
            }
        };

        loadData();
    }, [id, findById,fetchPage]);

    const handlePageChange = (newPage) => {
        fetchPage(vehicle.id, filterMeasuringUnit, filterCondition, newPage, 4);
        setPage(newPage);
    };

    const user = getUser();
    const isAdmin = vehicle && isLoggedIn() && user.roles?.includes("ROLE_ADMIN");
    const isOwnerOrAdmin = vehicle && isLoggedIn() && (user.username === vehicle.username || user.roles?.includes("ROLE_ADMIN"));
    const showNotFound = !vehicle || !isLoggedIn() || (user.username !== vehicle.username && user.roles?.includes("ROLE_USER"));

    return(
        <>
            <Header/>
            {loadingVehicle && (
                <Container>
                    <Spinner animation="grow" variant="dark" />
                </Container>
            )}
            {!loadingVehicle && !notFound && isOwnerOrAdmin && (
                <Container>
                    <Button variant="secondary" className="mb-3 backButton" onClick={goBack}>← Go back</Button>
                    <br/>
                    <h1>{vehicle.name}</h1>
                    <br/>
                    {vehicle.type === "CAR" && (
                        <h3>Vehicle Type: Car</h3>
                    )}
                    {vehicle.type === "BUS" && (
                        <h3>Vehicle Type: Bus</h3>
                    )}
                    {vehicle.type === "TRUCK" && (
                        <h3>Vehicle Type: Truck</h3>
                    )}
                    {vehicle.type === "MOTORCYCLE" && (
                        <h3>Vehicle Type: Motorcycle</h3>
                    )}
                    <h3>Year: {vehicle.year}</h3>
                    <h3>Total Kilometers: {vehicle.totalKilometers}km</h3>
                    {vehicle.fuelType && vehicle.fuelType === "DIESEL" && (
                        <h3>Fuel Type: Diesel</h3>
                    )}
                    {vehicle.fuelType && vehicle.fuelType === "PETROL" && (
                        <h3>Fuel Type: Petrol</h3>
                    )}
                    {vehicle.coolingType && vehicle.coolingType === "AIR_COOLED" && (
                        <h3>Cooling Type: Air</h3>
                    )}
                    {vehicle.coolingType && vehicle.coolingType === "WATER_COOLED" && (
                        <h3>Cooling Type: Water</h3>
                    )}
                    {vehicle.condition === "VERY_GOOD" && (
                        <h3>Condition: <span style={{color: "#146e43"}}>Very Good</span></h3>
                    )}
                    {vehicle.condition === "GOOD" && (
                        <h3>Condition: <span style={{color: "#0a54b9"}}>Good</span></h3>
                    )}
                    {vehicle.condition === "POOR" && (
                        <h3>Condition: <span style={{color: "#9f7605"}}>Poor</span></h3>
                    )}
                    {vehicle.condition === "OOS" && (
                        <h3>Condition: <span style={{color: "#9a2435"}}>Out Of Service</span></h3>
                    )}
                    {vehicle.condition === "UNKNOWN" && (
                        <h3>Condition: <span style={{color: "#3b3b3b"}}>Unknown</span></h3>
                    )}
                    {vehicle.insertPeriodType === "MONTHLY" && (
                        <h3>Insert Period: <span style={{color: "#0a54b9"}}>Monthly</span></h3>
                    )}
                    {vehicle.insertPeriodType === "WEEKLY" && (
                        <h3>Insert Period: <span style={{color: "#146e43"}}>Weekly</span></h3>
                    )}
                    {isAdmin && (
                        <h3>Owned by: {vehicle.username}</h3>
                    )}
                    <br/>
                    <ComponentSearch onSearch={handleSearch}/>
                    {loading && (
                        <Spinner animation="grow" variant="dark" />
                    )}
                    {!loading &&  (
                        <>
                            <ComponentsGrid components={components}/>
                            {totalPages > 1 && (
                                <PagePagination page={page}
                                                totalPages={totalPages}
                                                onPageChange={handlePageChange}/>
                            )}
                        </>
                    )}
                </Container>
            )}
            {!loadingVehicle && (notFound || showNotFound) && (
                <NotFoundPage/>
            )}
        </>
    )
}
export default VehicleInfoPage;