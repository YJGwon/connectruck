import React from 'react';
import './OwnersMain.css';

import TopBar from '../../../component/topbar/TopBar';
import AuthRouter from '../../../router/AuthRouter';

import { Routes, Route } from 'react-router-dom';
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

    return (
        <>
            <AuthRouter shouldLogin={true} root='/owners' />
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
