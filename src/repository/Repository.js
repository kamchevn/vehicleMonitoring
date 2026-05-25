import axiosInstance from "./axios";

const Repository = {
    findPageVehicles: async (department, type, pageNum = "1", pageSize = "4") => {
        const params = new URLSearchParams();

        if (department) params.append("name", department);
        if (type) params.append("type", type);

        params.append("pageNum", pageNum);
        params.append("pageSize", pageSize);

        return await axiosInstance.get(`/vehicle/page?${params.toString()}`);
    },
    findAll: async () => {
        return await axiosInstance.get(`/vehicle`);
    },
    findById: async (id) => {
        return await axiosInstance.get(`/vehicle/${id}`);
    },
    create: async (data) => {
        return await axiosInstance.post("/vehicle/create", data, {
            headers: { "Content-Type": "multipart/form-data" }
        });
    },
    edit: async (id, data) => {
        return await axiosInstance.post(`/vehicle/edit/${id}`, data, {
            headers: { "Content-Type": "multipart/form-data" }
        });
    },
    delete: async (id) => {
        return await axiosInstance.get(`/vehicle/delete/${id}`);
    },
    insertInterval: async (vehicleId,data) => {
        return await axiosInstance.post(`/vehicle/insertInterval/${vehicleId}`, data, {
            headers: { "Content-Type": "multipart/form-data" }
        });
    },
};

export default Repository;


