import React, {useEffect, useContext} from 'react';
import {Outlet, useNavigate} from 'react-router-dom';

import {UserContext} from '../context/UserContext';

export default function AuthRouter({shouldLogin, root}) {
    const navigate = useNavigate();
    const {isLogin, login} = useContext(UserContext);

    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        login(token);
    }, []);

    useEffect(() => {
        if (shouldLogin && !isLogin) {
            navigate(`${root}/signin`);
        }
    }, [navigate]);

    return <Outlet/>;
};
