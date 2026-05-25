import axios from "axios";
import { getToken, logout } from "../service/AuthService";
import { jwtDecode } from "jwt-decode";
const axiosInstance = axios.create({
    baseURL: "https://vehicle-monitoring.duckdns.org/api",
    headers: {
        "Content-Type": "application/json",
    },
});

axiosInstance.interceptors.request.use(config => {
    const token = getToken();
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

axiosInstance.interceptors.response.use(
    res => res,
    err => {
        const token = getToken();
        const url = err.config?.url;
        const isAuthEndpoint = url?.includes("/user/");
        if (err.response?.status === 401 && !isAuthEndpoint && token) {
            try {
                const decoded = jwtDecode(token);
                if (decoded.exp * 1000 < Date.now()) {
                    logout();
                    window.location.href = "/login";
                }
            } catch {
                logout();
                window.location.href = "/login";
            }
        }
        return Promise.reject(err);
    }
);

export default axiosInstance;