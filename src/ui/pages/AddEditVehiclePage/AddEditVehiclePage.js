import './AddEditVehiclePage.css'
import React, {useEffect} from 'react';
import Header from "../../components/layout/Header/Header";
import useVehicles from "../../../hooks/useVehicles";
import Container from "react-bootstrap/Container";
import VehicleCreateUpdateForm from "../../components/vehicles/VehicleCreateUpdateForm/VehicleCreateUpdateForm";
import Button from "react-bootstrap/Button";
import {useNavigate} from "react-router";
import {useParams} from "react-router";
import {getUser, isLoggedIn} from "../../../service/AuthService";
import NotFoundPage from "../NotFoundPage/NotFoundPage";
const AddEditVehiclePage = () => {
    const {vehicleToEdit, findByIdToEdit, onAdd, onEdit, exitEditing} = useVehicles();
    const { id } = useParams();
    useEffect(() => {
        if (id) {
            findByIdToEdit(id);
        }
    }, [id, findByIdToEdit]);
    const navigate = useNavigate();

    const goBack = () => {
        exitEditing();
        navigate("/vehicles");
    };
    return (
        <>
            <Header/>
            {isLoggedIn() && (
                <Container>
                    {!id && (
                        <>
                            <Button variant="secondary" className="mb-3" onClick={goBack}>← Go back</Button>
                            <h1 className="formHeader">Add vehicle</h1>
                            <VehicleCreateUpdateForm vehicle={null} onAdd={onAdd} onEdit={onEdit}/>
                        </>
                    )}
                    {id && vehicleToEdit && (vehicleToEdit && (vehicleToEdit.username === getUser().username || getUser().roles?.includes("ROLE_ADMIN"))) && (
                        <>
                            <Button variant="secondary" className="mb-3" onClick={goBack}>← Go back</Button>
                            <h1 className="formHeader">Edit vehicle</h1>
                            <VehicleCreateUpdateForm vehicle={vehicleToEdit} onAdd={onAdd} onEdit={onEdit}/>
                        </>
                    )}
                </Container>
            )}
            {(!isLoggedIn() || (vehicleToEdit && (vehicleToEdit.username !== getUser().username && getUser().roles?.includes("ROLE_USER")))) && (
                <NotFoundPage/>
            )}
        </>
    )
}

export default AddEditVehiclePage;