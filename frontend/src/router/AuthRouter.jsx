import React, {useEffect, useContext} from 'react';
import {Outlet, useNavigate} from 'react-router-dom';

import {UserContext} from '../context/UserContext';

export default function AuthRouter({shouldLogin, root}) {
    const navigate = useNavigate();
    const {isLogin, isInitialized, loadSavedToken} = useContext(UserContext);

    useEffect(() => {
        loadSavedToken();
    }, []);

    useEffect(() => {
        if (!isInitialized) {
            return;
        }

        if (shouldLogin && !isLogin) {
            console.log('redirect to login page');
            navigate(`${root}/signin`);
        }
        if (!shouldLogin && isLogin) {
            console.log('redirect to root page');
            navigate(root);
        }
    }, [navigate, isLogin, isInitialized]);

    return <Outlet/>;
};
