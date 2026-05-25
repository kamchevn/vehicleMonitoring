import {useCallback} from "react";
import userRepository from "../repository/UserRepository";
import { login as storeToken } from "../service/AuthService";
const useAuth = () => {
    const register = useCallback(async (data) => {
        try {
            const response = await userRepository.register(data);
            return response.data;
        } catch (error) {
            console.log(error);
            throw error;
        }
    }, []);

    const login = useCallback(async (data) => {
        try {
            const response = await userRepository.login(data);
            const { token } = response.data;
            storeToken(token);

            return response.data;
        } catch (error) {
            console.log(error);
            throw error;
        }
    }, []);

    return {register: register, login: login}
}

export default useAuth;