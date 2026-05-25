import { useCallback, useState } from "react";
import templatesRepository from "../repository/TemplatesRepository";

const initialState = {
    templates: [],
    loading: false,
    error: null
};

const useTemplates = () => {
    const [state, setState] = useState(initialState);

    const findByVehicleType = useCallback(async ({ vehicleType, fuelType, coolingType, drivenType }) => {
        setState(prev => ({ ...prev, loading: true, error: null }));

        try {
            const response = await templatesRepository.findByVehicleType({
                vehicleType,
                fuelType,
                coolingType,
                drivenType
            });

            setState({
                templates: response.data,
                loading: false,
                error: null
            });
            return response.data;

        } catch (err) {
            console.error(err);
            setState(prev => ({
                ...prev,
                loading: false,
                error: "Failed to fetch templates"
            }));
            return [];
        }
    }, []);

    return {
        ...state,
        findByVehicleType
    };
};

export default useTemplates;