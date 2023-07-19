import React, {createContext, useState} from 'react';

export const UserContext = createContext();

export const UserProvider = ({children}) => {
    const [isInitialized, setIsInitialized] = useState(false);
    const [isLogin, setIsLogin] = useState(false);
    const [accessToken, setAccessToken] = useState(null);

    const loadSavedToken = () => {
        const token = localStorage.getItem('accessToken');
        login(token);
    }

    const login = (token) => {
        if (!token) {
            setIsLogin(false);
            return;
        }
        localStorage.setItem('accessToken', token);
        setAccessToken(token);
        setIsLogin(true);
        setIsInitialized(true);
    };

    const logout = () => {
        localStorage.removeItem('accessToken');
        setAccessToken(null);
    };

    return (
        <UserContext.Provider
            value={{
                isLogin,
                isInitialized,
                accessToken,
                loadSavedToken,
                login,
                logout
            }}>
            {children}
        </UserContext.Provider>
    );
};
