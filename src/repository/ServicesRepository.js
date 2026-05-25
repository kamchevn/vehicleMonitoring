import axiosInstance from "./axios";

const ServicesRepository = {
    findServicesPage: async (vehicleId, serviceType, pageNum = "1", pageSize = "20") => {
        const params = new URLSearchParams();

        if (vehicleId) params.append("vehicleId", vehicleId);
        if (serviceType) params.append("serviceType", serviceType);

        params.append("pageNum", pageNum);
        params.append("pageSize", pageSize);

        return await axiosInstance.get(`/service/page?${params.toString()}`);
    },
    findById: async (id) => {
        return await axiosInstance.get(`/service/${id}`);
    },
    checkConditionForVehicle: async (vehicleId, data) => {
        return await axiosInstance.post(`/service/${vehicleId}`, data, {
            headers: { "Content-Type": "application/json" }
        });
    }
};

export default ServicesRepository