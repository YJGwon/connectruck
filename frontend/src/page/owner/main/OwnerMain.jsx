import React, {useContext} from 'react';
import './OwnerMain.css';

import {UserContext} from '../../../context/UserContext';
import TopBar from '../../../component/topbar/TopBar';
import SideBar from '../../../component/sidebar/SideBar';

import { Routes, Route } from 'react-router-dom';
import AuthRouter from '../../../router/AuthRouter';
import LoginForm from '../../../component/loginform/LoginForm';
import SignupForm from '../../../component/signupform/SignupForm';
import {OwnerOrderAccept} from '../orders/OwnerOrderAccept';

export default function OwnerMain() {
    const {isLogin} = useContext(UserContext);
    const root = "/owner";

    // topbar props
    const title = '사장님 서비스 🚚';

    // sidebar props
    const sideButtonsLoggedOut = [
        {
            link: `${root}/signin`,
            name: '로그인'
        },
        {
            link: `${root}/signup`,
            name: '회원가입'
        }
    ];

    const sideButtonsLoggedIn = [
        {
            link: `${root}/accept`,
            name: '주문 접수'
        },
        {
            link: '/logout',
            name: '로그아웃'
        }
    ];

    return (
        <>
            <TopBar title={title} root={root} />
            <div className='owners-main'>
                <SideBar buttons={isLogin ? sideButtonsLoggedIn : sideButtonsLoggedOut} />
                <div className='content'>
                    <Routes>
                        <Route element={<AuthRouter shouldLogin={true} root={root} />}>
                            <Route exact='exact' path='/' element="사장님 페이지"/>
                            <Route path='/accept' element={<OwnerOrderAccept/>}/>
                        </Route>
                        <Route element={<AuthRouter shouldLogin={false} root={root} />}>
                            <Route path='/signin' element={<LoginForm root={root}/>}/>
                            <Route path='/signup' element={<SignupForm root={root} role='OWNER' />}/>
                        </Route>
                    </Routes>
                </div>
            </div>
        </>
    );
}
