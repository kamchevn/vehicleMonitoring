import {useCallback, useEffect, useState} from "react";
import servicesRepository from "../repository/ServicesRepository";

const initialState = {
    "services": [],
    "loading": true,
    pageInfo: null
};

const useServices = () => {
    const [state, setState] = useState(initialState);

    const [serviceFilters, setServiceFilters] = useState({
        vehicleId: "",
        serviceType: ""
    });

    const fetchServicesPage = useCallback((vehicleId = serviceFilters.vehicleId, serviceType = serviceFilters.serviceType, pageNum = 1, pageSize = 20) => {
        setState(prev => ({ ...prev, loading: true }));

        setServiceFilters({ vehicleId, serviceType });

        return servicesRepository.findServicesPage(vehicleId, serviceType, pageNum, pageSize)
            .then(response => {
                setState({
                    services: response.data.content,
                    pageInfo: response.data,
                    loading: false,
                });
                return response.data;
            })
            .catch(err => err => {
                console.log(err);
                throw err;
            });
    }, []);

    const findById = useCallback((id) => {
        return servicesRepository.findById(id)
            .then((response) => {
                return response.data;
            })
            .catch((error) => console.log(error));
    },[]);

    const checkComponentsOrServiceVehicle = async (vehicleId, data) => {
        return servicesRepository.checkConditionForVehicle(vehicleId, data);
    };

    useEffect(() => {
        fetchServicesPage();
    }, [fetchServicesPage]);

    return {...state, fetchServicesPage: fetchServicesPage, findById: findById, checkComponentsOrServiceVehicle: checkComponentsOrServiceVehicle};
};

export default useServices;