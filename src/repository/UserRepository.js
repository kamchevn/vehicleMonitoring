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
    confirmEmail: async (token) => {
        return await axiosInstance.get("/user/confirm", {
            params: { token },
            headers: { "Content-Type": "application/json" }
        });
    },
};

export default UserRepository;