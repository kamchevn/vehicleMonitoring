import axiosInstance from "./axios";

const UserRepository = {
    register: async (data) => {
        return await axiosInstance.post("/user/register", data, {
            headers: { "Content-Type": "application/json" }
        });
    },
    login: async (data) => {
        return await axiosInstance.post(`/user/login`, data, {
            headers: { "Content-Type": "application/json" }
        });
    },
};

export default UserRepository;