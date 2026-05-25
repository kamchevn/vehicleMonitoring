import {useCallback, useEffect, useState} from "react";
import vehicleRepository from "../repository/Repository";
import { isLoggedIn } from "../service/AuthService";
const initialState = {
    "vehicles": [],
    "loading": true,
    pageInfo: null
};

const useVehicles = () => {
    const [state, setState] = useState(initialState);
    const [vehicleToEdit, setVehicleToEdit] = useState(null);

    const [filters, setFilters] = useState({
        name: "",
        type: ""
    });

    const fetchPage = useCallback((name = filters.name, type = filters.type, pageNum = 1, pageSize = 4) => {
        setState(prev => ({ ...prev, loading: true }));

        setFilters({ name, type });

        return vehicleRepository.findPageVehicles(name, type, pageNum, pageSize)
            .then(response => {
                setState({
                    vehicles: response.data.content,
                    pageInfo: response.data,
                    loading: false,
                });
                return response.data;
            })
            .catch(err => {
                console.log(err);
                throw err;
            });
    }, []);

    const onAdd = useCallback(async (data) => {
        try {
            const response = await vehicleRepository.create(data);
            await fetchPage();
            return response.data;
        } catch (error) {
            console.log(error);
            throw error;
        }
    }, [fetchPage]);

    const onEdit = useCallback(async (id, data) => {
        try {
            const response = await vehicleRepository.edit(id, data);
            console.log(`Successfully edited vehicle ${id}`);
            setVehicleToEdit(null);
            await fetchPage();
            return response.data;
        } catch (error) {
            console.log(error);
            throw error;
        }
    }, [fetchPage]);

    const onDelete = useCallback((id) => {
        vehicleRepository
            .delete(id)
            .then(() => {
                console.log(`Successfully deleted the vehicle with internal code ${id}.`);
                fetchPage();
            })
            .catch((error) => console.log(error));
    }, [fetchPage]);

    const findAll = useCallback(() => {
        return vehicleRepository.findAll()
            .then((response) => {
                return response.data;
            })
            .catch((error) => console.log(error));
    },[]);

    const findById = useCallback((id) => {
        return vehicleRepository.findById(id)
            .then((response) => {
                return response.data;
            })
            .catch((error) => console.log(error));
    },[]);

    const findByIdToEdit = useCallback((id) => {
        return vehicleRepository.findById(id)
            .then((response) => {
                setVehicleToEdit(response.data);
                return response.data;
            })
            .catch((error) => console.log(error));
    },[]);

    const exitEditing = useCallback(() => {
        setVehicleToEdit(null)
    },[])

    const insertIntervalForVehicle = useCallback(async (vehicleId, data) => {
        try {
            await vehicleRepository.insertInterval(vehicleId, data);
            await fetchPage();
        } catch (error) {
            console.log(error);
        }
    },[fetchPage])

    const checkConditionForVehicle = useCallback(async (id, data) => {
        try {
            await vehicleRepository.checkConditionForVehicle(id, data);
            await fetchPage();
        } catch (error) {
            console.log(error);
        }
    },[fetchPage])

    return {...state, vehicleToEdit, fetchPage: fetchPage, onAdd: onAdd, onEdit: onEdit, onDelete: onDelete, findAll:findAll, findById: findById, findByIdToEdit: findByIdToEdit, exitEditing: exitEditing, insertIntervalForVehicle: insertIntervalForVehicle, checkConditionForVehicle: checkConditionForVehicle};
};

export default useVehicles;
