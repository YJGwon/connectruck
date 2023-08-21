import React, {useState, useEffect, useContext} from 'react';
import {Routes, Route} from 'react-router-dom';

import {UserContext} from '../../../context/UserContext';

import TopBar from '../../../component/topbar/TopBar';
import BadgedMenuIcon from '../../../component/topbar/button/BadgedMenuIcon';
import SimpleSideBarButton from '../../../component/topbar/button/SimpleSideBarButton';
import BadgedSideBarButton from '../../../component/topbar/button/BadgedSideBarButton';

import AuthRouter from '../../../router/AuthRouter';
import LoginForm from '../../../component/loginform/LoginForm';
import SignupForm from '../../../component/signupform/SignupForm';
import {OwnerMain} from '../main/OwnerMain';
import {OwnerOrderAccept} from '../orders/OwnerOrderAccept';
import {OwnerMenuList} from '../menus/OwnerMenuList';

import './OwnerIndex.css';

export default function OwnerIndex() {
    const [newOrders, setNewOrders] = useState([]);

    const {isLogin} = useContext(UserContext);
    const root = "/owner";
    
    useEffect(() => {
        const rawNewOrders = localStorage.getItem('newOrders');
        if (rawNewOrders !== null) {
            setNewOrders(JSON.parse(rawNewOrders));
        }
    }, []);

    useEffect(() => {
        localStorage.setItem('newOrders', JSON.stringify(newOrders));
    }, [newOrders]);

    const removeFromNewOrders = (orderId) => {
        const updatedNewOrders = newOrders.filter(newOrderId => newOrderId !== orderId);
        setNewOrders(updatedNewOrders);
    }

    const onOrder = (order) => {
        if (order.status === 'CREATED') {
            setNewOrders(prevNewOrders => [...prevNewOrders, order.id]);
        } else if (order.status === 'CANCELED') {
            removeFromNewOrders(order.id);
        }
    }

    // topbar props
    const title = '사장님 서비스 🚚';

    const menuIcon = (
        <BadgedMenuIcon badgeContent={newOrders.length}/>
    );

    const sideButtonsLoggedOut  = (
        <React.Fragment>
            <SimpleSideBarButton index={0} link={`${root}/signin`} name='로그인'/>
            <SimpleSideBarButton index={1} link={`${root}/signup`} name='회원가입'/>
        </React.Fragment>
    );

    const sideButtonsLoggedIn  = (
        <React.Fragment>
            <BadgedSideBarButton 
                index={0} 
                link={`${root}/accept`} 
                name='주문 접수' 
                badgeContent={newOrders.length}/>
            <SimpleSideBarButton index={1} link={`${root}/menus`} name='메뉴 관리'/>
            <SimpleSideBarButton index={2} link={`/logout`} name='로그아웃'/>
        </React.Fragment>
    );

    return (
        <>
            <TopBar title={title} root={root} icon={menuIcon} buttons={isLogin ? sideButtonsLoggedIn : sideButtonsLoggedOut}/>
            <div className='owners-main'>
                <div className='content'>
                    <Routes>
                        <Route element={<AuthRouter shouldLogin={true} root={root} />}>
                            <Route exact='exact' path='/' element={<OwnerMain onOrder={onOrder}/>}/>
                            <Route path='/accept' element={<OwnerOrderAccept newOrders={newOrders} handleOnOrderClick={removeFromNewOrders}/>}/>
                            <Route path='/menus' element={<OwnerMenuList/>}/>
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
