import React, { useEffect } from 'react';
import './OwnersMain.css';

import TopBar from '../../../component/topbar/TopBar';

import { Routes, Route, useNavigate, useLocation } from 'react-router-dom';
import OwnersLogin from '../login/OwnersLogin';
import OwnersSignup from '../signup/OwnersSignup';

export default function OwnersMain() {
    // topbar props
    const title = '사장님 서비스 🚚';
    const home = "/owners";
    const buttons = [
        {
            link: '/',
            name: 'connectruck'
        }
    ];

    // 로그인 안했을 때 로그인 페이지로 redirect
    const navigate = useNavigate();
    const location = useLocation();
    useEffect(() => {
        const accessToken = localStorage.getItem('accessToken');
        const currentPath = location.pathname;

        if (!accessToken && currentPath !== '/owners/signup') {
            // Redirect to '/login' if access token doesn't exist and current path is not
            // '/signup'
            navigate('/owners/signin');
        }
    }, [navigate, location.pathname]);
   
    return (
        <>
            <TopBar title={title} home={home} buttons = {buttons} />
            <div className='owners-main'>
                <Routes>
                    <Route exact='exact' path='/' element="사장님 페이지"/>
                    <Route path='/signin' element={<OwnersLogin />}/>
                    <Route path='/signup' element={<OwnersSignup />}/>
                </Routes>
            </div>
        </>
    );
}
