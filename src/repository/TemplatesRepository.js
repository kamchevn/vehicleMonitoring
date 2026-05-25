import axiosInstance from "./axios";

const TemplatesRepository = {
    findByVehicleType: async ({ vehicleType, fuelType, coolingType, drivenType }) => {
        return await axiosInstance.get(`/template/${vehicleType}`, {
            params: {
                fuelType: fuelType || undefined,
                coolingType: coolingType || undefined,
                drivenType: drivenType || undefined
            }
        });
    },
    findByVehicleTypeAndComponentType: async (vehicleType,componentType) => {
        return await axiosInstance.get(`/template/${vehicleType}/${componentType}`);
    }
};

export default TemplatesRepository;