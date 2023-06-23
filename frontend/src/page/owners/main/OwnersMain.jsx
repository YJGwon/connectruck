import React, { useEffect } from 'react';
import './OwnersMain.css';

import TopBar from '../../../component/topbar/TopBar';

import { Routes, Route, useNavigate } from 'react-router-dom';
import LoginForm from '../../../component/loginform/LoginForm';

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
    useEffect(() => {
        const accessToken = localStorage.getItem('accessToken');
        if (!accessToken) {
            navigate('/owners/signin');
        }
    }, [navigate]);
  
    return (
        <>
            <TopBar title={title} home={home} buttons = {buttons} />
            <div className='owners-main'>
                <div className="content">
                    <Routes>
                        <Route exact='exact' path='/' element="사장님 페이지"/>
                        <Route path='/signin' element={<LoginForm url = '/api/owners/signin' />}/>
                    </Routes>
                </div>
            </div>
        </>
    );
}
