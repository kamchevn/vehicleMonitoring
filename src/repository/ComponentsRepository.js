import axiosInstance from "./axios";

const ComponentsRepository = {
    findPageComponents: async (vehicleId, measuringUnit, condition, pageNum = "1", pageSize = "4") => {
        const params = new URLSearchParams();
        params.append("vehicleId", vehicleId);
        if (measuringUnit) params.append("measuringUnit", measuringUnit);
        if (condition) params.append("condition", condition);

        params.append("pageNum", pageNum);
        params.append("pageSize", pageSize);

        return await axiosInstance.get(`/component/page?${params.toString()}`);
    },
    findById: async (id) => {
        return await axiosInstance.get(`/component/${id}`);
    },
    create: async (data) => {
        return await axiosInstance.post("/component/create", data, {
            headers: { "Content-Type": "multipart/form-data" }
        });
    },
    edit: async (id, data) => {
        return await axiosInstance.post(`/component/edit/${id}`, data, {
            headers: { "Content-Type": "multipart/form-data" }
        });
    },
    delete: async (id) => {
        return await axiosInstance.get(`/component/delete/${id}`);
    },
    findByVehicle: async (vehicleId) => {
        return await axiosInstance.get(`/component/findByVehicle/${vehicleId}`);
    },
    findByVehicleAndCondition: async (vehicleId,condition) => {
        return await axiosInstance.get(`/component/findByVehicle/${vehicleId}/${condition}`);
    }
};

export default ComponentsRepository;