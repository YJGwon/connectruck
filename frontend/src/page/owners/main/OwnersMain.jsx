import React, {useContext} from 'react';
import './OwnersMain.css';

import {UserContext} from '../../../context/UserContext';
import TopBar from '../../../component/topbar/TopBar';
import AuthRouter from '../../../router/AuthRouter';

import { Routes, Route } from 'react-router-dom';
import LoginForm from '../../../component/loginform/LoginForm';
import OwnersSignup from '../signup/OwnersSignup';
import SideBar from '../../../component/sidebar/SideBar';

export default function OwnersMain() {
    const {isLogin} = useContext(UserContext);

    // topbar props
    const title = '사장님 서비스 🚚';
    const home = "/owners";
    const topButtons = [
        {
            link: '/',
            name: 'connectruck'
        }
    ];

    // sidebar props
    const sideButtonsLoggedOut = [
        {
            link: '/owners/signin',
            name: '로그인'
        },
        {
            link: '/owners/signup',
            name: '회원가입'
        }
    ];

    const sideButtonsLoggedIn = [
        {
            link: '/logout',
            name: '로그아웃'
        }
    ];

    return (
        <>
            <TopBar title={title} home={home} buttons={topButtons} />
            <div className='owners-main'>
                <SideBar buttons={isLogin ? sideButtonsLoggedIn : sideButtonsLoggedOut} />
                <div className='content'>
                    <Routes>
                        <Route element={<AuthRouter shouldLogin={true} root='/owners' />}>
                            <Route exact='exact' path='/' element="사장님 페이지"/>
                        </Route>
                        <Route element={<AuthRouter shouldLogin={false} root='/owners' />}>
                            <Route path='/signin' element={<LoginForm home='/owners'/>}/>
                            <Route path='/signup' element={<OwnersSignup />}/>
                        </Route>
                    </Routes>
                </div>
            </div>
        </>
    );
}
