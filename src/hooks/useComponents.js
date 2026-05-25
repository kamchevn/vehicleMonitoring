import {useCallback, useState} from "react";
import componentsRepository from "../repository/ComponentsRepository";
import {isLoggedIn} from "../service/AuthService";

const initialState = {
    "components": [],
    "loading": true,
    pageInfo: null
};

const useComponents = () => {
    const [state, setState] = useState(initialState);

    const [filters, setFilters] = useState({
        vehicleId: "",
        measuringUnit: "",
        condition: ""
    });

    const fetchPage = useCallback((vehicleId, measuringUnit, condition, pageNum = 1, pageSize = 4) => {
        if (!isLoggedIn()) {
            return;
        }

        setState(prev => ({ ...prev, loading: true }));

        setFilters({ vehicleId , measuringUnit, condition });

        return componentsRepository.findPageComponents(vehicleId, measuringUnit, condition, pageNum, pageSize)
            .then(response => {
                setState({
                    components: response.data.content,
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
            await componentsRepository.create(data);
            console.log("Successfully added a new component.");
            await fetchPage();
        } catch (error) {
            console.log(error);
        }
    }, [fetchPage]);

    const onEdit = useCallback(async (id, data) => {
        try {
            await componentsRepository.edit(id, data);
            console.log(`Successfully edited component ${id}`);
            await fetchPage();
        } catch (error) {
            console.log(error);
        }
    }, [fetchPage]);

    const onDelete = useCallback((id) => {
        componentsRepository
            .delete(id)
            .then(() => {
                console.log(`Successfully deleted the component with id ${id}.`);
                fetchPage();
            })
            .catch((error) => console.log(error));
    }, [fetchPage]);

    const findById = useCallback((id) => {
        return componentsRepository.findById(id)
    },[]);

    const findByVehicle = useCallback((vehicleId) => {
        return componentsRepository.findByVehicle(vehicleId)
    },[]);

    const findByVehicleAndCondition = useCallback((vehicleId, condition) => {
        return fetchPage(vehicleId,null, condition,1,4)
    },[]);

    return {...state, fetchPage: fetchPage, onAdd: onAdd, onEdit: onEdit, onDelete: onDelete, findById: findById, findByVehicleAndCondition: findByVehicleAndCondition};
};

export default useComponents;
