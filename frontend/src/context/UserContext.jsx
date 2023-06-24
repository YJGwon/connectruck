import React, {createContext, useState} from 'react';

export const UserContext = createContext();

export const UserProvider = ({children}) => {
    const [isLogin, setIsLogin] = useState(false);

    const login = (token) => {
        if (!token) {
            setIsLogin(false);
            return;
        }
        setIsLogin(true);
        localStorage.setItem('accessToken', token);
    };

    const logout = () => {
        localStorage.removeItem('accessToken');
    };

    return (
        <UserContext.Provider
            value={{
                isLogin,
                login,
                logout
            }}>
            {children}
        </UserContext.Provider>
    );
};
