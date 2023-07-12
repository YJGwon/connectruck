import React, {createContext, useState} from 'react';

export const UserContext = createContext();

export const UserProvider = ({children}) => {
    const [isInitialized, setIsInitialized] = useState(false);
    const [isLogin, setIsLogin] = useState(false);

    const login = (token) => {
        if (!token) {
            setIsLogin(false);
            return;
        }
        setIsLogin(true);
        localStorage.setItem('accessToken', token);
        setIsInitialized(true);
    };

    const logout = () => {
        localStorage.removeItem('accessToken');
    };

    return (
        <UserContext.Provider
            value={{
                isLogin,
                isInitialized,
                login,
                logout
            }}>
            {children}
        </UserContext.Provider>
    );
};
