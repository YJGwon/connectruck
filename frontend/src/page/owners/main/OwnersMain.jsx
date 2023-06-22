import React from 'react';
import './OwnersMain.css';

import TopBar from '../../../component/topbar/TopBar';

import {Routes, Route} from 'react-router-dom';
import LoginForm from '../../../component/loginform/LoginForm';

export default function OwnersMain() {
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
            <TopBar title={title} home={home} buttons={buttons}/>
            <div className='owners-main'>
                <div className="content">
                    <Routes>
                        <Route exact='exact' path='/' element={<LoginForm url='/api/owners/signin' />}/>
                    </Routes>
                </div>
            </div>
        </>
    );
}
