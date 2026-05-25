import { jwtDecode } from "jwt-decode";

const TOKEN_KEY = "jwt";

export const login = (token) => {
    localStorage.setItem(TOKEN_KEY, token);
};

export const logout = () => {
    localStorage.removeItem(TOKEN_KEY);
};

export const getToken = () => {
    return localStorage.getItem(TOKEN_KEY);
};

export const getUser = () => {
    const token = getToken();
    if (!token) return null;

    try {
        const decoded = jwtDecode(token);
        return {
            username: decoded.sub,
            roles: decoded.roles
        };
    } catch {
        return null;
    }
};

export const isLoggedIn = () => {
    try {
        const token = getToken();
        if (!token) return false;

        const decoded = jwtDecode(token);
        if (decoded.exp * 1000 < Date.now()) {
            logout();
            return false;
        }

        return true;
    }
    catch {
        logout();
        return false;
    }
};

export const hasRole = (role) => {
    const user = getUser();
    return user?.roles?.includes(role);
};